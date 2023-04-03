package com.example.alcoholsafe.Deco;

import android.graphics.Color;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class DateDecorator implements DayViewDecorator {

    int color;
    HashSet<CalendarDay> dates;

    String TAG = "recordDrink";

    public DateDecorator(int color, Collection<CalendarDay> dates){
        this.color = color;
        this.dates = new HashSet<>(dates);
    }



    @Override
    public boolean shouldDecorate(CalendarDay day) {
//        Log.d(TAG, "shouldDecorate: " + dates.contains(day));
        return dates.contains(day);

    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10, color));
        Log.d(TAG, "decorate: ");

    }


}
