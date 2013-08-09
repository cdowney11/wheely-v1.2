package com.alkliv.wheely.vo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/12/13
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddressComponent {
    private String Long_Name;
    private String Short_Name;
    private List<GooglePlaceType> Types;

    public String getLong_Name() {
        return Long_Name;
    }

    public void setLong_Name(String long_Name) {
        Long_Name = long_Name;
    }

    public String getShort_Name() {
        return Short_Name;
    }

    public void setShort_Name(String short_Name) {
        Short_Name = short_Name;
    }

    public List<GooglePlaceType> getTypes() {
        return Types;
    }

    public void setTypes(List<GooglePlaceType> types) {
        Types = types;
    }
}
