package com.example.galleonsup.model;

public class Tmr {
    String name, imageString, id, area, team, strikeRate, totalStrikeRate, evaluated, totalEvaluation ,status;
    public Tmr(String name, String imageString, String id, String area, String team, String strikeRate,String totalStrikeRate, String evaluated, String totalEvaluation, String status)
    {
        this.name = name;
        this.imageString = imageString;
        this.id = id;
        this.area = area;
        this.team = team;
        this.strikeRate = strikeRate;
        this.totalStrikeRate = totalStrikeRate;
        this.evaluated = evaluated;
        this.totalEvaluation = totalEvaluation;
        this.status = status;
    }

    public String getEvaluated() {
        return evaluated;
    }

    public void setEvaluated(String evaluated) {
        this.evaluated = evaluated;
    }

    public String getTotalEvaluation() {
        return totalEvaluation;
    }

    public void setTotalEvaluation(String totalEvaluation) {
        this.totalEvaluation = totalEvaluation;
    }

    public String getTotalStrikeRate() {
        return totalStrikeRate;
    }

    public void setTotalStrikeRate(String totalStrikeRate) {
        this.totalStrikeRate = totalStrikeRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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



}
