package com.alkliv.wheely.vo;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/11/13
 * Time: 7:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class GooglePlace extends GooglePlaceBaseModel {
    private OpeningHour Opening_Hours;
    private int Price_Level;

    public OpeningHour getOpening_Hours() {
        return Opening_Hours;
    }

    public void setOpening_Hours(OpeningHour opening_Hours) {
        Opening_Hours = opening_Hours;
    }


    public int getPrice_Level() {
        return Price_Level;
    }

    public void setPrice_Level(int price_Level) {
        Price_Level = price_Level;
    }
}
