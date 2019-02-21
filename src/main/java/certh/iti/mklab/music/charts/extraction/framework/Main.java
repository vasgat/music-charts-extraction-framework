/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package certh.iti.mklab.music.charts.extraction.framework;

import certh.iti.mklab.music.charts.extraction.framework.chartUtils.Chart;
import certh.iti.mklab.music.charts.extraction.framework.chartUtils.ChartEntryType;
import certh.iti.mklab.music.charts.extraction.framework.extractors.ChartExtractor;
import certh.iti.mklab.music.charts.extraction.framework.stringUtils.DateFormatter;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author vasgat
 */
public class Main {

    public static void main(String[] args)
            throws URISyntaxException, IOException, InterruptedException {

        Chart ifpi_gr = new ChartExtractor.Builder("http://www.ifpi.gr/charts_el.html", false)
                .chart_id("123")
                .custom_country("GR")
                .chart_label("ifpi_gr")
                .type(ChartEntryType.ALBUM)
                .date("#container > div.Object652 > div.Object648 > p:nth-child(5) > span")
                .chart_name("div.Object648")
                .table_rows("table.MsoNormalTable tbody tr")
                .title("td:nth-child(3)")
                .position("td:nth-child(1)")
                .artist("td:nth-child(2)")
                //.additionalInfo("label", "td:nth-child(4)")
                .build();

        ifpi_gr.setName(ifpi_gr.getName().replaceAll("Εβδομάδα:.*", "").trim());
        String week = ifpi_gr.getDate().replace("Εβδομάδα:", "").trim().split("/")[0];
        String year = ifpi_gr.getDate().replace("Εβδομάδα:", "").trim().split("/")[1];
        ifpi_gr.setDate(DateFormatter.transformWeekYearToDateSpan(week, year, 1));
        ifpi_gr.getChartEntries().remove(0);

        System.out.println(ifpi_gr.toJSON().toJson());
    }
}
