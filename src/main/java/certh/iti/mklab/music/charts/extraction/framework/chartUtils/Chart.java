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
 * @author vasgat
 */
public class Chart {

    private ArrayList<HashMap> chartEntries;
    private String name;
    private String date;
    private org.bson.Document json;
    private String country;
    private ChartEntryType type;
    private String source;
    private String label;
    public org.jsoup.nodes.Document html;

    public Chart(String source, String name, String date, String country, String label, org.jsoup.nodes.Document html) {
        this.name = name;
        this.date = date;
        this.country = country;
        this.source = source;
        this.chartEntries = new ArrayList();
        this.label = label;
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

    public ChartEntryType getType() { return this.type; }

    public void setType(ChartEntryType type) {  this.type = type; }

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
        this.json.append("country", this.country);
        this.json.append("link", this.source);
        if (this.type != null) {
            this.json.append("type", this.type.toString());
        }
        if (this.label != null) {
            this.json.append("label", this.label);
        }

        ArrayList<org.bson.Document> entries = new ArrayList();
        for (HashMap<String, Object> chartEntry : this.chartEntries) {
            if (((!chartEntry.get("title").equals("")) && (!chartEntry.get("artist").equals(""))) || (type.equals(ChartEntryType.ARTIST) && (!chartEntry.get("artist").equals(""))) || (type.equals(ChartEntryType.VIDEO) && (!chartEntry.get("title").equals("")))) {
                org.bson.Document chart_entry = new org.bson.Document();
                for (Map.Entry<String, Object> entry : chartEntry.entrySet()) {
                    if (entry.getValue() != null && !entry.getValue().equals("")) {
                        chart_entry.append((String) entry.getKey(), entry.getValue());
                    }

                    if (this.date.contains("-")) {
                        chart_entry.append("since", this.date.split(" - ")[0]);
                        chart_entry.append("until", this.date.split(" - ")[1]);
                    } else {
                        chart_entry.append("since", this.date);
                        chart_entry.append("until", this.date);
                    }
                    chart_entry.append("source", this.source);
                    chart_entry.append("country", this.country);
                    chart_entry.append("source", this.source);
                    if (this.type != null) {
                        chart_entry.append("type", this.type.toString());
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
