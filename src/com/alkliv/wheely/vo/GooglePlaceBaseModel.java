package com.alkliv.wheely.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/12/13
 * Time: 9:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class GooglePlaceBaseModel implements Serializable {
    private Geometry Geometry;
    private String Icon;
    private String Id;
    private String Name;
    private List<Photo> Photos;
    private double Rating;
    private String Reference;
    private List<GooglePlaceType> Types;
    private String Vicinity;


    public Geometry getGeometry() {
        return Geometry;
    }

    public void setGeometry(Geometry geometry) {
        Geometry = geometry;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Photo> getPhotos() {
        return Photos;
    }

    public void setPhotos(List<Photo> photos) {
        Photos = photos;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public String getVicinity() {
        return Vicinity;
    }

    public void setVicinity(String vicinity) {
        Vicinity = vicinity;
    }

    public List<GooglePlaceType> getTypes() {
        return Types;
    }

    public void setTypes(List<GooglePlaceType> types) {
        Types = types;
    }
}
