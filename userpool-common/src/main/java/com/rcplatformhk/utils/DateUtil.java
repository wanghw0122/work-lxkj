package com.rcplatformhk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateUtil {

    public static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd");

    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static String TIME_PATTERN = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
    private static String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";

    /**
     * 判断一个时间是否在另一个时间之前
     * @param time1 第一个时间
     * @param time2 第二个时间
     * @return 判断结果
     */
    public static boolean before(String time1, String time2) {
        try {
            Date dateTime1 = TIME_FORMAT.parse(time1);
            Date dateTime2 = TIME_FORMAT.parse(time2);

            if(dateTime1.before(dateTime2)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("DateUtil before Error!{}",e.getMessage(),e);
        }
        return false;
    }


    public static Date parseToDate(String pattern){
        try {
            return TIME_FORMAT.parse(pattern);
        } catch (ParseException e) {
            logger.error("DateUtil parseToDate Error!{}",e.getMessage(),e);
        }
        return null;
    }

    public static Long parseToTimeStemp(String time){
        Date date = parseToDate(time);
        if(Objects.isNull(date)) return (long) -1;
        return date.getTime();
    }

    /**
     * 判断一个时间是否在另一个时间之后
     * @param time1 第一个时间
     * @param time2 第二个时间
     * @return 判断结果
     */
    public static boolean after(String time1, String time2) {
        try {
            Date dateTime1 = TIME_FORMAT.parse(time1);
            Date dateTime2 = TIME_FORMAT.parse(time2);

            if(dateTime1.after(dateTime2)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("DateUtil after Error!{}",e.getMessage(),e);
        }
        return false;
    }

    /**
     * 计算时间差值（单位为秒）
     * @param time1 时间1
     * @param time2 时间2
     * @return 差值
     */
    public static long minus(String time1, String time2) {
        try {
            Date datetime1 = TIME_FORMAT.parse(time1);
            Date datetime2 = TIME_FORMAT.parse(time2);

            long millisecond = datetime1.getTime() - datetime2.getTime();

            return millisecond;
        } catch (Exception e) {
            logger.error("DateUtil minus Error!{}",e.getMessage(),e);
        }
        return -1;
    }

    public static long minus(String time1){
        return minus(formatTime(new Date()),time1);
    }

    /**
     * 获取年月日和小时
     * @param datetime 时间（yyyy-MM-dd HH:mm:ss）
     * @return 结果
     */
    public static String getDateHour(String datetime) {
        String date = datetime.split(" ")[0];
        String hourMinuteSecond = datetime.split(" ")[1];
        String hour = hourMinuteSecond.split(":")[0];
        return date + "_" + hour;
    }

    /**
     * 获取当天日期（yyyy-MM-dd）
     * @return 当天日期
     */
    public static String getTodayDate() {
        return DATE_FORMAT.format(new Date());
    }

    /**
     * 获取昨天的日期（yyyy-MM-dd）
     * @return 昨天的日期
     */
    public static String getYesterdayDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -1);

        Date date = cal.getTime();

        return DATE_FORMAT.format(date);
    }

    /**
     * 格式化日期（yyyy-MM-dd）
     * @param date Date对象
     * @return 格式化后的日期
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }


    public static String getLastNDayStartTime(int n){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, - n + 1);
       return DATE_FORMAT.format(calendar.getTime()).concat(" 00:00:00");
    }

    /**
     * 格式化时间（yyyy-MM-dd HH:mm:ss）
     * @param date Date对象
     * @return 格式化后的时间
     */
    public static String formatTime(Date date) {
        return TIME_FORMAT.format(date);
    }


    public static String getLastNHoursStartTime(int n){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, - n);
        return TIME_FORMAT.format(calendar.getTime());
    }

}