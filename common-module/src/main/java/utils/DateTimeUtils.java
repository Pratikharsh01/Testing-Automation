package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {

    public static Date getDateFromFormat(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static long compareDate(Date dateAfter, Date dateBefore) {
        long timeDiff = Math.abs(dateAfter.getTime() - dateBefore.getTime());
        long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
        return daysDiff;
    }

    public static void main(String[] args) {
        Date date = getDateFromFormat("2023-07-21T06:24:31Z", "yyyy-MM-dd'T'HH:mm:ssXXX");
        System.out.println(date.toString());

        String currentDate = getCurrentDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.println(currentDate);

        System.out.println("Diff " + compareDate(date, new Date()));

        String estimatedTime = getModifiedDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", 1);
        System.out.println(estimatedTime);


    }

    public static String getCurrentDate(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }

    public static String getModifiedDate(String dateFormat, int hour) {

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        Date date = calendar.getTime();
        String str = sdf.format(date);
        return str;
    }
}
