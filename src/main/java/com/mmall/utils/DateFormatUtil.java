package com.mmall.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateFormatUtil.class);

    public static final String PATTERN_YEAR_MONTH = "yyyy年MM月";

    public static Date transStringToDate(String datestr, String format){
        if(StringUtils.isEmpty(datestr) || StringUtils.isEmpty(format)){
            return null;
        }
        SimpleDateFormat sdf;
        try {
            sdf = new SimpleDateFormat(format);
            return sdf.parse(datestr);
        } catch (Exception e){
            logger.error("failed to trans String to Date");
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(Date date){
        return formatDate(date, DateTimeUtil.PATTERN_DEFAULT);
    }
    public static String formatDate(Date date, String pattern){
        if(date == null){
            return "";
        }
        return DateTimeUtil.formatDate(date, pattern);
    }

    /**
     * 获取指定时间当月的开始时间
     *
     * @param date
     * @return
     */
    public static Date getMonthStart(Date date) {
        Calendar _calendar = Calendar.getInstance();
        _calendar.setTime(date);
        _calendar.set(Calendar.YEAR, _calendar.get(Calendar.YEAR));
        _calendar.set(Calendar.MONTH, _calendar.get(Calendar.MONTH));
        _calendar.set(Calendar.DATE, 1);
        _calendar.set(Calendar.HOUR_OF_DAY, 0);
        _calendar.set(Calendar.MINUTE, 0);
        _calendar.set(Calendar.SECOND, 0);
        _calendar.set(Calendar.MILLISECOND, 0);
        return _calendar.getTime();
    }

    /**
     * 获取N年前的日期
     *
     * @param date
     * @param nYearAgo
     * @return
     */
    public static Date getDateNYearAgo(Date date, int nYearAgo) {
        Calendar _calendar = Calendar.getInstance();
        _calendar.setTime(date);
        _calendar.set(Calendar.YEAR, _calendar.get(Calendar.YEAR) - nYearAgo);
        return _calendar.getTime();
    }

    /**
     * 日期加时间后缀
     * yyyy-MM-dd --> formatDate(yyyy-MM-dd HH:mm:ss)
     * @param dateStr
     * @param postfix
     * @return
     */
    public static Date getDateWithTimePostfix(String dateStr, String postfix){
        if (StringUtils.isEmpty(dateStr))
            return null;
        dateStr += postfix;
        return transStringToDate(dateStr, DateTimeUtil.PATTERN_DEFAULT);
    }

    /**
     * 时间转年月 xx年xx月
     * @param date
     * @return
     */
    public static String formatDateToYM(Date date){
        if(date == null){
            return "";
        }
        return formatDate(date, PATTERN_YEAR_MONTH);
    }

    /**
     * 计算距离截止时间还有几天
     * @param date
     * @param limit
     * @return 向上取整，大于零表示还没到截止时间，小于零表示已超过截止时间
     */
    public static int calcRemainDays(Date date, Date limit){
        BigDecimal time = BigDecimal.valueOf(date.getTime());
        BigDecimal limitTime = BigDecimal.valueOf(limit.getTime());
        BigDecimal limitMilliseconds = limitTime.subtract(time);
        int remainDays = limitMilliseconds.divide(BigDecimal.valueOf(86400000D),BigDecimal.ROUND_UP).intValue();
        return remainDays;
    }
}
