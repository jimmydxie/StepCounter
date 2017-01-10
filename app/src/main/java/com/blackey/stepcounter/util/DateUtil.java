package com.blackey.stepcounter.util;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by blacKey on 2015/9/14.
 */
public class DateUtil {
    static int mYear;
    static int mMonth;
    static int mDay;
    static int day;
    static int hour;
    static int minute;
    static int second;

    public static void init() {
        Calendar calendar= Calendar.getInstance();
        //calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;// 获取当前月份
        mDay = calendar.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        day = calendar.get(Calendar.DAY_OF_WEEK);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }

    public static String getDate() {
        return getYearOfDate() + "年" + getMonthOfDate() + "月" + getDayOfDate() + "日" + "      " + "星期" + getDayOfWeek(getDay() + 1);
    }

    public static int getYearOfDate() {
        init();
        return mYear;
    }

    public static int getMonthOfDate() {
        init();
        return mMonth;
    }

    public static int getDayOfDate() {
        init();
        return mDay;
    }

    public static int getDay() {
        init();
        return day;
    }

    public static int getHour() {
        init();
        return hour;
    }

    public static int getMinute() {
        init();
        return minute;
    }

    public static int getSecond() {
        init();
        return second;
    }






    public static String getDayOfWeek(int day){
        //获取当前时间为本月的第几周
        //int week = calendar.get(Calendar.WEEK_OF_MONTH);
        //获取当前时间为本周的第几天
        if (day==1) {
            day=7;
            //week=week-1;
        } else {
            day=day-1;
        }

        String weekday = "";
        if (day>7)
            day-=7;
        switch (day){
            case 1:
                weekday ="日";
                break;
            case 2:
                weekday ="一";
                break;
            case 3:
                weekday ="二";
                break;
            case 4:
                weekday ="三";
                break;
            case 5:
                weekday ="四";
                break;
            case 6:
                weekday ="五";
                break;
            case 7:
                weekday ="六";
                break;
        }
        //System.out.println("今天是本月的第" + week + "周"+",星期"+(day));
        return weekday;
    }

    public static String getTime() {
        return hour + ":" + minute + ":" + second;
    }

    public static int getWeatherHour() {
        Time time=new Time();
        time.setToNow(); // ȡ��ϵͳʱ�䡣

        int hour = time.hour;
        return hour;
    }

    public static int getDateSub(String beginDateStr,String endDateStr)
    {
        long day=0;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;

        try {
            beginDate = format2.parse(beginDateStr);
            endDate= format2.parse(endDateStr);
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            //System.out.println("相隔的天数="+day);
        return (int) day;
    }

    public static int getDateSub(Date beginDate,Date endDate) {
        long day = 0;

        day = (endDate.getTime() - beginDate.getTime()) / (24*60*60*1000);
        //System.out.println("相隔的天数="+day);
        return (int) day;
    }

    public static long getTimeSubFromNow(String dateString) {
        long temp = 0;
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String curDate = formatter.format(new Date());
        try {
            date = formatter.parse(dateString);
            temp = date.getTime() - formatter.parse(curDate).getTime();    //相差毫秒数
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
