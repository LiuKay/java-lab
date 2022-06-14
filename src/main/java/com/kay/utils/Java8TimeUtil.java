package com.kay.utils;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by kay on 2018/5/28.
 */
@Log4j2
public class Java8TimeUtil {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(Date date) {
        LocalDateTime localDateTime = LocalDateTime
                .ofInstant(date.toInstant(), ZoneId.systemDefault());
        return localDateTime.format(df);
    }

    public static Date parse(String str) {
        LocalDateTime dateTime = LocalDateTime.parse(str, df);
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static void main(String[] args) {
        LocalDate nowDate = LocalDate.now();
        log.info("nowDate=" + nowDate);
        LocalTime nowTime = LocalTime.now();
        log.info("nowTime=" + nowTime);

        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String landing = dateTime.format(format);
        log.info(("date to str :" + landing));

        LocalDateTime dateTime2 = LocalDateTime.parse("2018-05-30 18:31:55", format);
        log.info("str to date :" + dateTime2);

        final String now = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
        log.info(now);

        final String s = new Date().toString();
        log.info(s);

        final String s1 = Instant.now().toString();
        log.info(s1);

        testPeriod();

        testDuration();
    }

    private static void testPeriod() {
        final LocalDate date1 = LocalDate.now();
        final LocalDate date2 = date1.plus(1, ChronoUnit.MONTHS);

        final Period period = Period.between(date1, date2);
        log.info("period:" + period);
    }

    private static void testDuration() {
        final LocalTime time1 = LocalTime.now();
        final LocalTime time2 = time1.plus(1, ChronoUnit.MINUTES);

        final Duration duration = Duration.between(time1, time2);
        log.info("duration:" + duration);
    }
}
