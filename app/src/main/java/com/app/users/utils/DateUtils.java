package com.app.users.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MMM-dd";

    public static String generateDateFromTimeStamp(long timeStamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        Date date = new Date(timeStamp);
        return simpleDateFormat.format(date);
    }

    public static Long getTodayDate() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
            Date date = new Date(System.currentTimeMillis());
            String dateString = simpleDateFormat.format(date);
            Date d = simpleDateFormat.parse(dateString.toLowerCase());
            return d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static Long getTomorrowDate() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
            String dateString = simpleDateFormat.format(calendar.getTime());
            Date d = simpleDateFormat.parse(dateString.toLowerCase());
            return d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
