package com.example.fitness_app.util;

import android.text.format.DateUtils;

import java.util.Calendar;

public class Dates {

    public static boolean isSameDay(Calendar firstDate, Calendar secondDate){
        return firstDate.get(Calendar.YEAR) == secondDate.get(Calendar.YEAR) &&
                firstDate.get(Calendar.MONTH) == secondDate.get(Calendar.MONTH) &&
                firstDate.get(Calendar.DAY_OF_MONTH) == secondDate.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isToday(Long timestamp){
        return DateUtils.isToday(timestamp);
    }
}
