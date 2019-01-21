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
package certh.iti.mklab.music.charts.extraction.framework.httpUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author vasgat
 */
public class StaticHTMLFetcher
        extends Fetcher {

    private Document document;
    private int responseCode;
    private Connection connection;

    public StaticHTMLFetcher(String baseURL, String relativeURL)
            throws URISyntaxException, IOException {
        this.connection = Jsoup.connect(new URI(baseURL + relativeURL).toASCIIString()).followRedirects(true).validateTLSCertificates(false).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3").timeout(60000).ignoreHttpErrors(true);
        this.document = this.connection.get();
        this.responseCode = this.connection.response().statusCode();
    }

    public StaticHTMLFetcher(String fullURL)
            throws URISyntaxException, IOException {
        int counter = 0;
        boolean failed = true;

        while (failed && counter < 3) {
            try {
                this.connection = Jsoup.connect(new URI(fullURL).toASCIIString()).followRedirects(true).validateTLSCertificates(false).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3").timeout(60000).ignoreHttpErrors(true);
                this.document = this.connection.get();
                failed = false;
                this.responseCode = this.connection.response().statusCode();
            } catch (IOException ex) {
                counter++;
                System.out.println(ex.getMessage());
                System.out.println("failed_url:" + fullURL);
            }
        }
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public Document getHTMLDocument() {
        return this.document;
    }
}
