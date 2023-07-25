package com.easteregg.storage;

public class EasterEggsData {

    private String url;
    private String location;
    private String id;

    public EasterEggsData(){

    }

    public EasterEggsData(final String url,final String location, final String id){
        this.url = url;
        this.location = location;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }
}
