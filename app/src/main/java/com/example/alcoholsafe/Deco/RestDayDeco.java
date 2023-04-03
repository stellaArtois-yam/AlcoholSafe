package com.example.alcoholsafe.Deco;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;


import java.util.ArrayList;

public class RestDayDeco implements DayViewDecorator {

    ArrayList<CalendarDay> restday;

    public RestDayDeco(ArrayList<CalendarDay> restInfoList) {
        this.restday = new ArrayList<>(restInfoList);
    }



    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return restday.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));

    }
}
