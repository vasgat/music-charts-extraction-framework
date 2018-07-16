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

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;

/**
 *
 * @author vasgat
 */
public class BrowserEmulator extends Fetcher {

    public WebDriver driver;

    public BrowserEmulator(String baseURL, String relativeURL, String ChromeDriverPath)
            throws InterruptedException {
        this.driver = Selenium.setUpChromeDriver(ChromeDriverPath);
        this.driver.manage().timeouts().implicitlyWait(60L, TimeUnit.SECONDS);
        this.driver.get(baseURL + relativeURL);

        Thread.sleep(10000L);
    }

    public BrowserEmulator(String fullURL, String ChromeDriverPath)
            throws InterruptedException {
        this.driver = Selenium.setUpChromeDriver(ChromeDriverPath);
        this.driver.manage().timeouts().implicitlyWait(60L, TimeUnit.SECONDS);
        this.driver.get(fullURL);

        Thread.sleep(5000L);
    }

    public void clickEvent(String CSSSelector)
            throws InterruptedException {
        this.driver.findElement(By.cssSelector(CSSSelector)).click();
        Thread.sleep(3000L);
    }

    public void scrollDownEvent()
            throws InterruptedException {
        String currentDoc = "";
        do {
            currentDoc = this.driver.getPageSource();
            JavascriptExecutor jse = (JavascriptExecutor) this.driver;
            jse.executeScript("window.scrollTo(1000,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));", new Object[0]);
            Thread.sleep(4000L);
        } while (!currentDoc.equals(this.driver.getPageSource()));
    }

    public void scrollDownEvent(int timesToRepeat)
            throws InterruptedException {
        Random rand = new Random();
        for (int i = 0; i < timesToRepeat; i++) {
            int counter = 0;
            while ((getHTMLDocument().select("._hnn7m").size() > 0) && (counter <= 600000)) {
                Thread.sleep(2000L);
                counter += 2000;
            }
            if (counter > 600000) {
                break;
            }
            JavascriptExecutor jse = (JavascriptExecutor) this.driver;
            jse.executeScript("window.scrollBy(0," + (300 + rand.nextInt(312)) + ")", new Object[]{""});
            int sleep_time = rand.nextInt(3000);
            Thread.sleep(2142 + sleep_time);
        }
    }

    public void scrollUpEvent(int timesToRepeat)
            throws InterruptedException {
        Random rand = new Random();
        for (int i = 0; i < timesToRepeat; i++) {
            int counter = 0;
            while ((getHTMLDocument().select("._hnn7m").size() > 0) && (counter <= 600000)) {
                Thread.sleep(2000L);
                counter = 2000;
            }
            if (counter > 600000) {
                break;
            }
            JavascriptExecutor jse = (JavascriptExecutor) this.driver;
            jse.executeScript("window.scrollBy(0," + (65236 - rand.nextInt(312)) + ")", new Object[]{""});
            int sleep_time = rand.nextInt(3000);
            Thread.sleep(2142 + sleep_time);
        }
    }

    public void close() {
        this.driver.close();
        this.driver.quit();
    }

    public Document getHTMLDocument() {
        Document doc = Jsoup.parse(this.driver.getPageSource());
        return doc;
    }
}
