package com.tarzan.nav.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期工具类
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Slf4j
@UtilityClass
public class DateUtil {

    public static final long ONE_DAY_SECONDS = 86400;
    public static final String SHORT_FORMAT = "yyyyMMdd";
    public static final String LONG_FORMAT = "yyyyMMddHHmmss";
    public static final String concurrentFormat = "yyyyMMddHHmmssSSS";
    public static final String shortConcurrentFormat = "yyMMddHHmmssSSS";
    public static final String webFormat = "yyyy-MM-dd";
    public static final String webMonthFormat = "yyyy-MM";
    public static final String timeFormat = "HH:mm:ss";
    public static final String monthFormat = "yyyyMM";
    public static final String chineseDtFormat = "yyyy年MM月dd日";
    public static final String chineseYMFormat = "yyyy年MM月";
    public static final String datetimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String noSecondFormat = "yyyy-MM-dd HH:mm";
    public static final String MdFormat = "MM-dd";
    public static final long ONE_DAY_MILL_SECONDS = 86400000;

    public static Date now() {
        return new Date();
    }

    public static DateFormat getNewDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df;
    }

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String format(String dateStr, String oldFormat, String newFormat) throws ParseException {
        String result;
        DateFormat oldDateFormat = new SimpleDateFormat(oldFormat);
        DateFormat newDateFormat = new SimpleDateFormat(newFormat);
        Date date = oldDateFormat.parse(dateStr);
        result = newDateFormat.format(date);
        return result;
    }

    public static Date parseDateNoTime(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(SHORT_FORMAT);
        return dateFormat.parse(sDate);
    }

    public static Date parseDateNoTime(String sDate, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            throw new ParseException("Null format. ", 0);
        }

        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(sDate);
    }

    public static Date parseDateNoTimeWithDelimit(String sDate, String delimit) throws ParseException {
        sDate = sDate.replaceAll(delimit, "");
        DateFormat dateFormat = new SimpleDateFormat(SHORT_FORMAT);
        return dateFormat.parse(sDate);
    }

    public static Date parseDateLongFormat(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(LONG_FORMAT);
        return dateFormat.parse(sDate);
    }

    public static Date parseDateNewFormat(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(datetimeFormat);
        dateFormat.setLenient(false);
        return dateFormat.parse(sDate);
    }

    public static Date parseDateNoSecondFormat(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(noSecondFormat);
        dateFormat.setLenient(false);
        return dateFormat.parse(sDate);
    }

    public static Date parseDateWebFormat(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(webFormat);
        dateFormat.setLenient(false);
        return dateFormat.parse(sDate);
    }

    public static Date parseDateWebMonthFormat(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(webMonthFormat);
        dateFormat.setLenient(false);
        return dateFormat.parse(sDate);
    }

    /**
     * 计算当前时间几小时之后的时间
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, long hours) {
        return addMinutes(date, hours * 60);
    }

    /**
     * 计算当前时间几分钟之后的时间
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, long minutes) {
        return addSeconds(date, minutes * 60);
    }

    /**
     * @param date1
     * @param secs
     * @return
     */

    public static Date addSeconds(Date date1, long secs) {
        return new Date(date1.getTime() + secs * 1000);
    }

    /**
     * 判断输入的字符串是否为合法的小时
     *
     * @param hourStr
     * @return true/false
     */
    public static boolean isValidHour(String hourStr) {
        if (!StringUtils.isEmpty(hourStr) && StringUtils.isNumeric(hourStr)) {
            int hour = Integer.parseInt(hourStr);
            return hour >= 0 && hour <= 23;
        }
        return false;
    }

    /**
     * 判断输入的字符串是否为合法的分或秒
     *
     * @param str
     * @return true/false
     */
    public static boolean isValidMinuteOrSecond(String str) {
        if (!StringUtils.isEmpty(str) && StringUtils.isNumeric(str)) {
            int hour = Integer.parseInt(str);
            return hour >= 0 && hour <= 59;
        }
        return false;
    }

    /**
     * 取得新的日期
     *
     * @param date1 日期
     * @param days  天数
     * @return 新的日期
     */
    public static Date addDays(Date date1, long days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.add(Calendar.DATE, (int) days);
        return cal.getTime();
    }

    public static String getTomorrowDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);

        aDate = addSeconds(aDate, ONE_DAY_SECONDS);

        return getDateString(aDate);
    }

    public static String getTomorrowDateNewFMTString(String sDate) throws ParseException {
        Date aDate = parseDateWebFormat(sDate);
        aDate = addDays(aDate, 1);
        return getWebDateString(aDate);
    }

    public static String getTomorrowDateNewFormatString(String sDate) throws ParseException {
        Date aDate = parseDateNewFormat(sDate);
        aDate = addDays(aDate, 1);
        return getWebDateString(aDate);
    }

    public static String getLongDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(LONG_FORMAT);

        return getDateString(date, dateFormat);
    }

    public static String getNewFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(datetimeFormat);
        return getDateString(date, dateFormat);
    }

    public static String getWebFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(webFormat);
        return getDateString(date, dateFormat);
    }

    public static String getConcurrentFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(concurrentFormat);
        return getDateString(date, dateFormat);
    }

    public static String getDateString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return null;
        }

        return dateFormat.format(date);
    }

    public static String getYesterDayDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);

        aDate = addSeconds(aDate, -ONE_DAY_SECONDS);

        return getDateString(aDate);
    }

    /**
     * @return 当天的时间格式化为"yyyyMMdd"
     */
    public static String getDateString(Date date) {
        DateFormat df = getNewDateFormat(SHORT_FORMAT);

        return df.format(date);
    }

    public static String getWebDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(webFormat);

        return getDateString(date, dateFormat);
    }

    /**
     * 取得“X年X月X日”的日期格式
     *
     * @param date
     * @return
     */
    public static String getChineseDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(chineseDtFormat);

        return getDateString(date, dateFormat);
    }

    public static String getTodayString() {
        DateFormat dateFormat = getNewDateFormat(SHORT_FORMAT);

        return getDateString(new Date(), dateFormat);
    }

    public static String getTomorrowString() {
        DateFormat dateFormat = getNewDateFormat(SHORT_FORMAT);

        return getDateString(DateUtil.addDays(new Date(), 1), dateFormat);
    }

    public static String getTimeString(Date date) {
        DateFormat dateFormat = getNewDateFormat(timeFormat);

        return getDateString(date, dateFormat);
    }

    public static String getBeforeDayString(int days) {
        Date date = new Date(System.currentTimeMillis() - ONE_DAY_MILL_SECONDS * days);
        DateFormat dateFormat = getNewDateFormat(SHORT_FORMAT);

        return getDateString(date, dateFormat);
    }

    /**
     * 取得两个日期间隔毫秒数（日期1-日期2）
     *
     * @param one 日期1
     * @param two 日期2
     * @return 间隔秒数
     */
    public static long getDiffMillis(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return sysDate.getTimeInMillis() - failDate.getTimeInMillis();
    }

    /**
     * 取得两个日期间隔秒数（日期1-日期2）
     *
     * @param one 日期1
     * @param two 日期2
     * @return 间隔秒数
     */
    public static long getDiffSeconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
    }

    /**
     * 取得两个日期间隔分钟数（日期1-日期2）
     *
     * @param one 日期1
     * @param two 日期2
     * @return 间隔秒数
     */
    public static long getDiffMinutes(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (60 * 1000);
    }

    /**
     * 取得两个日期的间隔天数
     *
     * @param one
     * @param two
     * @return 间隔天数
     */
    public static long getDiffDays(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 取得两个日期相差的自然日
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDiffNaturalDays(Date date1, Date date2) throws ParseException {
        return Math.abs(getDiffNaturalDayNotAbs(date1, date2));
    }

    /**
     * 取得两个日期相差的自然日
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDiffNaturalDayNotAbs(Date date1, Date date2) throws ParseException {

        long diffDays;
        DateFormat dateFormat = new SimpleDateFormat(webFormat);

        //去掉时分秒
        String dateStr1 = dateFormat.format(date1);
        String dateStr2 = dateFormat.format(date2);
        diffDays = (dateFormat.parse(dateStr1).getTime() - dateFormat.parse(dateStr2).getTime()) / (24 * 60 * 60 * 1000);

        return diffDays;
    }

    public static String getBeforeDayString(String dateString, int days) throws ParseException {
        DateFormat df = getNewDateFormat(SHORT_FORMAT);
        Date date = df.parse(dateString);
        date = new Date(date.getTime() - ONE_DAY_MILL_SECONDS * days);

        return df.format(date);
    }

    public static boolean isValidShortDateFormat(String strDate) {
        if (strDate == null || strDate.length() != SHORT_FORMAT.length()) {
            return false;
        }

        try {
            // ---- 避免日期中输入非数字 ----
            Integer.parseInt(strDate);
        } catch (NumberFormatException e) {
            return false;
        }

        DateFormat df = getNewDateFormat(SHORT_FORMAT);

        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidShortDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidShortDateFormat(temp);
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     *
     * @param strDate
     * @return
     */
    public static boolean isValidLongDateFormat(String strDate) {
        if (strDate.length() != LONG_FORMAT.length()) {
            return false;
        }

        try {
            Long.parseLong(strDate); // ---- 避免日期中输入非数字 ----
        } catch (Exception NumberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat(LONG_FORMAT);

        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     *
     * @param strDate
     * @param delimiter
     * @return
     */
    public static boolean isValidLongDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidLongDateFormat(temp);
    }

    public static String getShortDateString(String strDate) {
        return getShortDateString(strDate, "-|/");
    }

    public static String getShortDateString(String strDate, String delimiter) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        String temp = strDate.replaceAll(delimiter, "");

        if (isValidShortDateFormat(temp)) {
            return temp;
        }

        return null;
    }

    public static String getShortFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        DateFormat df = getNewDateFormat(SHORT_FORMAT);

        return df.format(cal.getTime());
    }

    public static String getWebTodayString() {
        DateFormat df = getNewDateFormat(webFormat);

        return df.format(new Date());
    }

    /**
     * 获取当月首日
     *
     * @return
     */
    public static String getWebFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        DateFormat df = getNewDateFormat(webFormat);

        return df.format(cal.getTime());
    }

    /**
     * 获取当月的总天数
     *
     * @return
     */
    public static int getDaysOfMonth() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        return cal.getActualMaximum(Calendar.DATE);
    }

    public static String convert(String dateString, DateFormat formatIn, DateFormat formatOut) {
        try {
            Date date = formatIn.parse(dateString);

            return formatOut.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public static String convert2WebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(SHORT_FORMAT);
        DateFormat df2 = getNewDateFormat(webFormat);

        return convert(dateString, df1, df2);
    }

    public static String convert2ChineseDtFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(SHORT_FORMAT);
        DateFormat df2 = getNewDateFormat(chineseDtFormat);

        return convert(dateString, df1, df2);
    }

    public static String convertFromWebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(SHORT_FORMAT);
        DateFormat df2 = getNewDateFormat(webFormat);

        return convert(dateString, df2, df1);
    }

    public static boolean webDateNotLessThan(String date1, String date2) {
        DateFormat df = getNewDateFormat(webFormat);

        return dateNotLessThan(date1, date2, df);
    }

    /**
     * @param date1
     * @param date2
     * @param format
     * @return
     */
    public static boolean dateNotLessThan(String date1, String date2, DateFormat format) {
        try {
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);

            return !d1.before(d2);
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getEmailDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

        todayStr = sdf.format(today);
        return todayStr;
    }

    public static String getSmsDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");

        todayStr = sdf.format(today);
        return todayStr;
    }

    public static String formatMonth(Date date) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(monthFormat).format(date);
    }

    /**
     * 获取系统日期的前一天日期，返回Date
     *
     * @return
     */
    public static Date getBeforeDate() {
        Date date = new Date();

        return new Date(date.getTime() - ONE_DAY_MILL_SECONDS);
    }

    /**
     * 获得指定时间当天起点时间
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false);

        String dateString = df.format(date);

        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * 根据Date对象返回今天是星期几
     *
     * @param date
     * @return 1:星期日 2:星期一 3:星期二 4:星期三 5:星期四 6:星期五 7:星期六
     */
    public static int getWeekDayFromDateEntity(Date date) {

        Calendar calendar = Calendar.getInstance();// 获得一个日历
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);

    }

    /**
     * 判断参date上min分钟后，是否小于当前时间
     *
     * @param date
     * @param min
     * @return
     */
    public static boolean dateLessThanNowAddMin(Date date, long min) {
        return addMinutes(date, min).before(new Date());

    }

    public static boolean isBeforeNow(Date date) {
        if (date == null) {
            return false;
        }
        return date.compareTo(new Date()) < 0;
    }

    /**
     * 获得当前月的开始日期
     *
     * @param date
     * @return
     */
    public static Date getMinMonthDate(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat(webFormat);
        calendar.setTime(fmt.parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 获得当前月的结束日期
     *
     * @param date
     * @return
     */
    public static Date getMaxMonthDate(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat(webFormat);
        calendar.setTime(fmt.parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date parseNoSecondFormat(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(noSecondFormat);
        return dateFormat.parse(sDate);
    }

    /*
     *
     * date日期转变成 制定格式字符串
     *
     */
    public static String convertDate2String(Date date, String time_pattern) {
        SimpleDateFormat sf = new SimpleDateFormat(time_pattern);
        return sf.format(date);

    }

    /**
     * 根据Date对象返回天
     *
     * @param date
     */
    public static int getDayFromDateEntity(Date date) {
        Calendar calendar = Calendar.getInstance();// 获得一个日历
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);

    }

    public static int compareDateStr(String dateStr, String anotherDateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat(webMonthFormat);
        Date dt1 = df.parse(dateStr);
        Date dt2 = df.parse(anotherDateStr);
        return Long.compare(dt1.getTime(), dt2.getTime());
    }

    public static String getCurMonth() {
        return format(new Date(), webMonthFormat);
    }

    public static String getChineseYMString(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(chineseYMFormat);
        Date datea = sdf.parse(date);
        DateFormat dateFormat = getNewDateFormat(chineseYMFormat);
        return getDateString(datea, dateFormat);
    }

    public static Date getPreMonthDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(webMonthFormat);
        Date datea = sdf.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(datea);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public static Date getNextMonthDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(webMonthFormat);
        Date datea = sdf.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(datea);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取指定日期的当月的第一天
     *
     * @param date
     * @return
     */
    public static String getAssignedDateFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        DateFormat df = getNewDateFormat(webFormat);
        return df.format(cal.getTime());
    }

    /**
     * 获取指定日期的当月的最后一天
     *
     * @param date
     * @return
     */
    public static String getAssignedDateLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        DateFormat df = getNewDateFormat(webFormat);
        return df.format(cal.getTime());
    }

    public static Date getNextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    public static String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }


    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

}