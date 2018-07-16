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
package certh.iti.mklab.music.charts.extraction.framework.stringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;

/**
 *
 * @author vasgat
 */
public abstract class DateFormatter {

    public static String transformWeekYearToDateSpan(String week, String year, int startDayofWeek) {
        DateTime startOfTheWeek = new DateTime().withYear(Integer.parseInt(year)).withWeekOfWeekyear(Integer.parseInt(week)).withDayOfWeek(startDayofWeek).withTimeAtStartOfDay();
        DateTime endOfTheWeek = startOfTheWeek.plusDays(7);
        return transform(startOfTheWeek) + " - " + transform(endOfTheWeek);
    }

    public static String transformEndDateToDateSpan(String dayOfMonth, String monthOfYear, String year) {
        DateTime endOfTheWeek = new DateTime().withYear(Integer.parseInt(year)).withMonthOfYear(Integer.parseInt(monthOfYear)).withDayOfMonth(Integer.parseInt(dayOfMonth)).withTimeAtStartOfDay();
        DateTime startOfTheWeek = endOfTheWeek.minusDays(7);
        return transform(startOfTheWeek) + " - " + transform(endOfTheWeek);
    }

    public static String textToDate(String format, String dateInString, Locale locale)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
        Date date = formatter.parse(dateInString);
        SimpleDateFormat formatter_2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return formatter_2.format(date);
    }

    public static String getCurrentDate() {
        DateTime date = new DateTime();
        return transform(date);
    }

    public static String getCurrentDateMinus(int minus) {
        DateTime date = new DateTime().minusDays(minus);
        return transform(date);
    }

    private static String transform(DateTime date) {
        String month = date.getMonthOfYear() < 10 ? "0" + date.getMonthOfYear() : date.monthOfYear().getAsString();
        String day_of_month = date.dayOfMonth().get() < 10 ? "0" + date.dayOfMonth().get() : date.dayOfMonth().getAsText();
        String year = date.year().getAsText();
        return day_of_month + "/" + month + "/" + year;
    }
}
