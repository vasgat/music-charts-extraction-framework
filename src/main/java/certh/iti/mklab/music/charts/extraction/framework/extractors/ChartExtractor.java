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
package certh.iti.mklab.music.charts.extraction.framework.extractors;

import certh.iti.mklab.music.charts.extraction.framework.chartUtils.Chart;
import certh.iti.mklab.music.charts.extraction.framework.chartUtils.ChartEntry;
import certh.iti.mklab.music.charts.extraction.framework.chartUtils.ChartEntryType;
import certh.iti.mklab.music.charts.extraction.framework.httpUtils.BrowserEmulator;
import certh.iti.mklab.music.charts.extraction.framework.httpUtils.Fetcher;
import certh.iti.mklab.music.charts.extraction.framework.httpUtils.StaticHTMLFetcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author vasgat
 */
public class ChartExtractor {

    public static class Builder {

        private String source;
        private String chart_id;
        private String date;
        private String table_rows;
        private String chart_name;
        private ChartEntryType type;
        private String country;
        private String chart_label;
        private HashMap<String, Object> entriesSelectors;
        private Document html;
        private boolean isDynamic;
        private Fetcher fetcher;

        public Builder(String source, boolean dynamic)
                throws URISyntaxException, IOException, InterruptedException {
            this.source = source;
            this.entriesSelectors = new HashMap();
            this.isDynamic = dynamic;
            if (!dynamic) {
                this.fetcher = new StaticHTMLFetcher(source);
                this.html = this.fetcher.getHTMLDocument();
            } else {
                this.fetcher = new BrowserEmulator(source);
                this.html = this.fetcher.getHTMLDocument();
            }
        }

        public Builder click_event(String cssSelector)
                throws InterruptedException {
            if (this.isDynamic) {
                ((BrowserEmulator) this.fetcher).clickEvent(cssSelector);
                this.html = this.fetcher.getHTMLDocument();
            }
            return this;
        }

        public Builder chart_id(String chart_id) {
            this.chart_id = chart_id;
            return this;
        }

        public Builder custom_country(String country) {
            this.country = country;
            return this;
        }

        public Builder chart_label(String label) {
            this.chart_label = label;
            return this;
        }

        public Builder country(String cssSelector) {
            this.country = this.country;
            return this;
        }

        public Builder table_rows(String cssSelector) {
            this.table_rows = cssSelector;
            return this;
        }

        public Builder type(ChartEntryType type) {
            this.type = type;
            return this;
        }

        public Builder date(String cssSelector) {
            this.date = this.html.select(cssSelector).text();
            return this;
        }

        public Builder date(String cssSelector, String attr) {
            this.date = this.html.select(cssSelector).attr(attr);
            return this;
        }

        public Builder chart_name(String cssSelector) {
            this.chart_name = this.html.select(cssSelector).text();
            return this;
        }

        public Builder chart_name(String cssSelector, String attr) {
            this.chart_name = this.html.select(cssSelector).attr(attr);
            return this;
        }

        public Builder title(String cssSelector) {
            this.entriesSelectors.put("title", cssSelector);
            return this;
        }

        public Builder artist(String cssSelector) {
            this.entriesSelectors.put("artist", cssSelector);
            return this;
        }

        public Builder position(String cssSelector) {
            this.entriesSelectors.put("position", cssSelector);
            return this;
        }

        public Builder title(String cssSelector, String attr) {
            this.entriesSelectors.put("title", new Pair(cssSelector, attr));
            return this;
        }

        public Builder artist(String cssSelector, String attr) {
            this.entriesSelectors.put("artist", new Pair(cssSelector, attr));
            return this;
        }

        public Builder position(String cssSelector, String attr) {
            this.entriesSelectors.put("position", new Pair(cssSelector, attr));
            return this;
        }

        public Builder additionalInfo(String name, String cssSelector) {
            this.entriesSelectors.put(name, cssSelector);
            return this;
        }

        public Builder additionalInfo(String name, String cssSelector, String attr) {
            this.entriesSelectors.put(name, new Pair(cssSelector, attr));
            return this;
        }

        public Chart build()
                throws UnsupportedEncodingException, URISyntaxException, IOException {
            if (this.isDynamic) {
                ((BrowserEmulator) this.fetcher).close();
            }
            Chart chart = new Chart(this.source, this.chart_name, this.date, this.country, this.chart_label, this.html);
            chart.setType(this.type);

            Elements rows = this.html.select(this.table_rows);
            for (Element row : rows) {
                String title = "";
                if (this.entriesSelectors.get("title") != null) {
                    title = extract_content(row, this.entriesSelectors.get("title"));
                }
                String artist = "";
                if (this.entriesSelectors.get("artist") != null) {
                    artist = extract_content(row, this.entriesSelectors.get("artist"));
                }
                String position = extract_content(row, this.entriesSelectors.get("position"));

                HashMap<String, String> additionalInfo = new HashMap();
                for (Map.Entry<String, Object> entry : entriesSelectors.entrySet()) {
                    if (!entry.getKey().equals("title") && !entry.getKey().equals("artist") && !entry.getKey().equals("chart_id") && !entry.getKey().equals("position")) {
                        String extracted_content = extract_content(row, entry.getValue());
                        if (!extracted_content.equals("")) {
                            additionalInfo.put(entry.getKey(), extracted_content.trim());
                        }

                    }
                }
                try {
                    HashMap chart_entry = new ChartEntry.Builder()
                            .chart_id(this.chart_id)
                            .title(title.trim())
                            .artist(artist.trim())
                            .position(Integer.parseInt(position.trim()))
                            .additionalInfo(additionalInfo)
                            .build();
                    chart.addChartEntry(chart_entry);
                } catch (NumberFormatException ex) {
                    HashMap chart_entry = new ChartEntry.Builder()
                            .chart_id(this.chart_id)
                            .title(title.trim())
                            .artist(artist.trim())
                            .position(position.trim())
                            .additionalInfo(additionalInfo)
                            .build();
                    chart.addChartEntry(chart_entry);
                }


            }
            return chart;
        }

        private String extract_content(Element e, Object s) {
            if ((s instanceof Pair)) {
                Pair<String, String> pair = (Pair) s;
                return e.select((String) pair.getKey()).attr((String) pair.getValue());
            }
            return e.select((String) s).text();
        }
    }
}
