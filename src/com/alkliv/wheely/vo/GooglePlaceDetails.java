package com.alkliv.wheely.vo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/12/13
 * Time: 9:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class GooglePlaceDetails extends GooglePlaceBaseModel {
    private List<AddressComponent> Address_Components;
    private String Formatted_Address;
    private String Formatted_Phone_Number;
    private String International_Phone_Number;
    private List<Review> Reviews;
    private String Url;
    private String Utc_Offset;
    private String Website;

    public List<AddressComponent> getAddress_Components() {
        return Address_Components;
    }

    public void setAddress_Components(List<AddressComponent> address_Components) {
        Address_Components = address_Components;
    }

    public String getFormatted_Address() {
        return Formatted_Address;
    }

    public void setFormatted_Address(String formatted_Address) {
        Formatted_Address = formatted_Address;
    }

    public String getFormatted_Phone_Number() {
        return Formatted_Phone_Number;
    }

    public void setFormatted_Phone_Number(String formatted_Phone_Number) {
        Formatted_Phone_Number = formatted_Phone_Number;
    }

    public String getInternational_Phone_Number() {
        return International_Phone_Number;
    }

    public void setInternational_Phone_Number(String international_Phone_Number) {
        International_Phone_Number = international_Phone_Number;
    }

    public List<Review> getReviews() {
        return Reviews;
    }

    public void setReviews(List<Review> reviews) {
        Reviews = reviews;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getUtc_Offset() {
        return Utc_Offset;
    }

    public void setUtc_Offset(String utc_Offset) {
        Utc_Offset = utc_Offset;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }
}
