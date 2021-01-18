package com.example.galleonsup.model;

public class Tmr {
    String name, imageString, id, area, team, strikeRate, totalVisit;
    public Tmr(String name, String imageString, String id, String area, String team, String strikeRate, String totalVisit)
    {
        this.name = name;
        this.imageString = imageString;
        this.id = id;
        this.area = area;
        this.team = team;
        this.strikeRate = strikeRate;
        this.totalVisit = totalVisit;
    }

    public String getArea() {
        return area;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(String strikeRate) {
        this.strikeRate = strikeRate;
    }

    public String getTotalVisit() {
        return totalVisit;
    }

    public void setTotalVisit(String totalVisit) {
        this.totalVisit = totalVisit;
    }

}
