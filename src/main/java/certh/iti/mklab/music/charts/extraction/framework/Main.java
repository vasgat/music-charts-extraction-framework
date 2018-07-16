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
        
        Chart top40 = new ChartExtractor.Builder("http://top40-charts.com/chart.php?cid=44", false)
                .custom_country("global").type(ChartEntryType.TRACK)
                .chart_name("font.biggerblue")
                .date("select[name=date] option[selected]")
                .table_rows("tr.latc_song")
                .artist("td:nth-child(4) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(3) > a:nth-child(2)")
                .title("td:nth-child(4) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(3) > div:nth-child(1)")
                .position("td:nth-child(1)")
                .additionalInfo("youtube_id", "td:nth-child(4) td.videolink", "style")
                .build();

        String[] dateFields = top40.getDate().split("\\-");
        top40.setDate(DateFormatter.transformEndDateToDateSpan(dateFields[0], dateFields[1], dateFields[2]));
        top40.process_entry("youtube_id", ".*img.youtube.com/vi/", "");
        top40.process_entry("youtube_id", "/.*", "");

        System.out.println(top40.toJSON().toJson());
    }
}
