/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package certh.iti.mklab.music.charts.extraction.framework;

import certh.iti.mklab.music.charts.extraction.framework.chartUtils.Chart;
import certh.iti.mklab.music.charts.extraction.framework.chartUtils.ChartEntryType;
import certh.iti.mklab.music.charts.extraction.framework.extractors.ChartExtractor;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author vasgat
 */
public class Main {

    public static void main(String[] args)
            throws URISyntaxException, IOException, InterruptedException {

        Chart kworb = new ChartExtractor.Builder("http://kworb.net/youtube/insights/se_daily.html", false)
                .custom_country("global")
                .chart_id("123456")
                .type(ChartEntryType.VIDEO)
                .chart_name("span.pagetitle")
                .date("span.pagetitle")
                .table_rows("#dailytable > tbody tr")
                .title("td:nth-child(3)")
                .position("td:nth-child(1)")
                .additionalInfo("streams_change", "td:nth-child(5)")
                .additionalInfo("streams", "td:nth-child(4)")
                .build();

        String[] date = kworb.getDate().replaceAll(".* - ", "").replaceAll("\\|.*", "").trim().split("/");
        kworb.setDate(date[2] + "/" + date[1] + "/" + date[0]);
        kworb.setName(kworb.getName().replaceAll(" - .*", ""));

        System.out.println(kworb.toJSON().toJson());
    }
}
