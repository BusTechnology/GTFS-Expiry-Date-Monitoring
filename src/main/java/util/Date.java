package util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Date {
    public static int getCurrentDate() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/New York"));
        int currentDate = Integer.valueOf(dateFormat.format(date));
        return currentDate;
    }
}
