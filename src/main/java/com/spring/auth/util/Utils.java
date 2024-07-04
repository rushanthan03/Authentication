package com.spring.auth.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Utils {

    public static Timestamp timeFormat() {
        Calendar time = Calendar.getInstance();
        return new Timestamp(time.getTimeInMillis());
    }

    public static String getCurrentDateAndTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH:mm:ss.SSS");
        return now.format(formatter);
    }

    private Utils() {
    }
}
