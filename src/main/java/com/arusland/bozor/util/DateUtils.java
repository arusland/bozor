package com.arusland.bozor.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ruslan on 28.07.2014.
 */
public final class DateUtils {
    private final static long MILLIS_IN_MINUTE = 60 * 1000;
    private final static SimpleDateFormat DF_FULL = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
    private final static SimpleDateFormat DF_SHORT = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private final static SimpleDateFormat DF_MONTH = new SimpleDateFormat("yyyyMM", Locale.ENGLISH);
    private final static SimpleDateFormat DF_FULL_UTC = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);

    static {
        DF_FULL_UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static DateFormat getFullDateFormat() {
        return DF_FULL;
    }

    public static String toStringFull(Date date) {
        return DF_FULL.format(date);
    }

    public static String toStringShort(Date date) {
        return DF_SHORT.format(date);
    }

    public static Date parseFull(String str) {
        try {
            return DF_FULL.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static Date parseTime(String str) {
        if (str.length() != 8) {
            throw new IllegalStateException("Invalid time format");
        }

        try {
            return DF_SHORT.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static Date parseMonth(String str) {
        if (str.length() != 6) {
            throw new IllegalStateException("Invalid time format");
        }

        try {
            return DF_MONTH.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static Date getMinTimeOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);

        return cal.getTime();
    }

    public static Date getMaxTimeOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);

        return cal.getTime();
    }

    public static Date getMinTimeOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1,
                0, 0, 0);

        return cal.getTime();
    }

    public static Date getMaxTimeOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int currentMonth = cal.get(Calendar.MONTH);
        cal.set(cal.get(Calendar.YEAR), currentMonth, 28, 23, 59, 59);
        Calendar result;

        do {
            result = cal;
            cal = (Calendar) cal.clone();
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.get(Calendar.MONTH) == currentMonth);

        return result.getTime();
    }

    public static boolean isToday(Date date, Integer timeOffset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        Calendar calNow = Calendar.getInstance();
        calNow.setTime(DateUtils.toUTCTime(new Date(), timeOffset));

        return year == calNow.get(Calendar.YEAR) &&
                month == calNow.get(Calendar.MONTH) &&
                day == calNow.get(Calendar.DAY_OF_MONTH);
    }

    public static Date toUTCTime(Date input, Integer timeOffset) {
        Date result = toUTCTime(input);

        if (timeOffset != null) {
            return addMinutes(result, -timeOffset);
        }

        return result;
    }

    public static Date toUTCTime(Date input) {
        String inputStr = DF_FULL_UTC.format(input);

        try {
            return DF_FULL.parse(inputStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static Date addMinutes(Date time, long mins) {
        return new Date(time.getTime() + mins * MILLIS_IN_MINUTE);
    }
}
