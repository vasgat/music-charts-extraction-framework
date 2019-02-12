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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vasgat
 */
public class ChartEntry {

    public static class Builder {

        private HashMap chart_entry;

        public Builder() {
            this.chart_entry = new HashMap();
        }

        public Builder title(String title) {
            this.chart_entry.put("title", title);
            return this;
        }

        public Builder chart_id(String chart_id) {
            this.chart_entry.put("chart_id", chart_id);
            return this;
        }

        public Builder artist(String artist) {
            this.chart_entry.put("artist", artist);
            return this;
        }

        public Builder position(String position) {
            this.chart_entry.put("position", position);
            return this;
        }

        public Builder country(String country) {
            this.chart_entry.put("country", country);
            return this;
        }

        public Builder source(String source) {
            this.chart_entry.put("source", source);
            return this;
        }

        public Builder additionalInfo(HashMap<String, String> snippets) {
            for (Map.Entry info : snippets.entrySet()) {
                this.chart_entry.put(info.getKey(), info.getValue());
            }
            return this;
        }

        public HashMap build() {
            return this.chart_entry;
        }
    }
}
