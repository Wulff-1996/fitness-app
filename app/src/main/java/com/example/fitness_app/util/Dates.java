package com.example.fitness_app.util;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Dates {

    public static boolean isSameDay(Calendar firstDate, Calendar secondDate){
        return firstDate.get(Calendar.YEAR) == secondDate.get(Calendar.YEAR) &&
                firstDate.get(Calendar.MONTH) == secondDate.get(Calendar.MONTH) &&
                firstDate.get(Calendar.DAY_OF_MONTH) == secondDate.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isToday(Long timestamp){
        return DateUtils.isToday(timestamp);
    }

    public static String formatDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String formatDate(Long timestamp){
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String formatDate(int year, int month, int day) {
        String correctYear = String.valueOf(year);
        String correctMonth;
        String correctDay;
        if (month < 10){
            correctMonth = "0" + month;
        } else {
            correctMonth = String.valueOf(month);
        }
        if (day < 10){
            correctDay = "0" + day;
        } else {
            correctDay = String.valueOf(day);
        }
        return correctYear + "-" + correctMonth + "-" + correctDay;
    }

    public static boolean isDeadlineReached(Long deadline){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        Date currentDate = new GregorianCalendar(currentYear, currentMonth, currentDay).getTime();
        GregorianCalendar deadlineGreCal = new GregorianCalendar();
        deadlineGreCal.setTimeInMillis(deadline);
        Date deadlineDate = deadlineGreCal.getTime();

        return currentDate.after(deadlineDate);
    }
}
