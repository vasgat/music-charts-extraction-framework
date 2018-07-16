/*
 * Copyright 2018 vasgat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package certh.iti.mklab.music.charts.extraction.framework.chartUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author vasgat
 */
public class Chart {

    private ArrayList<HashMap> chartEntries;
    private String name;
    private String date;
    private org.bson.Document json;
    private String country;
    private String source;
    public org.jsoup.nodes.Document html;

    public Chart(String source, String name, String date, String country, org.jsoup.nodes.Document html) {
        this.name = name;
        this.date = date;
        this.country = country;
        this.source = source;
        this.chartEntries = new ArrayList();
        this.html = html;
    }

    public String getDate() {
        return this.date;
    }

    public String getName() {
        return this.name;
    }

    public String getSource() {
        return this.source;
    }

    public String getCountry() {
        return this.country;
    }

    public ArrayList<HashMap> getChartEntries() {
        return this.chartEntries;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void addChartEntry(HashMap chart_entry) {
        this.chartEntries.add(chart_entry);
    }

    public void process_entry(String chart_field, String regex, String replaceWith) {
        for (HashMap entry : this.chartEntries) {
            if (entry.get(chart_field) != null) {
                entry.put(chart_field, entry
                        .get(chart_field).toString().replaceAll(regex, replaceWith).trim());
            }
        }
    }

    public void remove_from(String chart_field, String chart_field_2) {
        for (HashMap entry : this.chartEntries) {
            if ((entry.get(chart_field) != null) && (entry.get(chart_field_2) != null)) {
                entry.put(chart_field, entry
                        .get(chart_field).toString().replace(entry.get(chart_field_2).toString(), "").trim());
            }
        }
    }

    public org.bson.Document toJSON() {
        this.json = new org.bson.Document();
        this.json.append("name", this.name);
        if (this.date.contains("-")) {
            this.json.append("start_date", this.date.split(" - ")[0]);
            this.json.append("end_date", this.date.split(" - ")[1]);
        } else {
            this.json.append("date", this.date);
        }
        this.json.append("country", this.country);
        this.json.append("source", this.source);

        ArrayList<org.bson.Document> entries = new ArrayList();
        for (HashMap<String, Object> chartEntry : this.chartEntries) {
            if (((!chartEntry.get("title").equals("")) && (!chartEntry.get("artist").equals(""))) || ((chartEntry.get("type").equals("artist")) && (!chartEntry.get("artist").equals(""))) || ((chartEntry.get("type").equals("video")) && (!chartEntry.get("title").equals("")))) {
                org.bson.Document chart_entry = new org.bson.Document();
                for (Map.Entry<String, Object> entry : chartEntry.entrySet()) {
                    if (((String) entry.getKey()).equals("stats")) {
                        org.bson.Document stats = new org.bson.Document();
                        HashMap<String, Object> statistics = (HashMap) entry.getValue();
                        if (statistics.size() > 0) {
                            for (Map.Entry<String, Object> statistic : statistics.entrySet()) {
                                stats.append((String) statistic.getKey(), statistic.getValue());
                            }
                            chart_entry.append("stats", stats);
                        }
                    } else if (!entry.getValue().equals("")) {
                        chart_entry.append((String) entry.getKey(), entry.getValue());
                    }
                }
                entries.add(chart_entry);
            }
        }
        this.json.append("chart_entries", entries);
        return this.json;
    }

    public String toString() {
        toJSON();
        return this.json.toJson();
    }
}
