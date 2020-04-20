package com.kalu.mainactivity;

public class EventModel {
    String name;
    String place;
    String time;
    String description;
    double longitude;
    double Latitude;
    String zuserId;
    String zzeventpic;
    String eventkey;

    public String getZuserId() {
        return zuserId;
    }

    public void setZuserId(String zuserId) {
        this.zuserId = zuserId;
    }

    public String getEventkey() {
        return eventkey;
    }

    public void setEventkey(String eventkey) {
        this.eventkey = eventkey;
    }

    public String getZzeventpic() {
        return zzeventpic;
    }

    public void setZzeventpic(String zzeventpic) {
        this.zzeventpic = zzeventpic;
    }

    public String getZuserPhoto() {
        return zuserId;
    }

    public void setZuserPhoto(String zuserPhoto) {
        this.zuserId = zuserPhoto;
    }

    public EventModel() {
    }

    public EventModel(String description, double latitude, double longitude, String name, String place, String time ,String userphoto,String eventpic ) {
        this.name = name;
        this.place = place;
        this.time = time;
        this.description = description;
        this.longitude = longitude;
        Latitude = latitude;
        this.zuserId=userphoto;
        this.zzeventpic=eventpic;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}