package com.spandigital.codingchallenge.ezrom.model;

public class TeamStats {
    private String teamName;
    private int points;

    public TeamStats(String teamName, int points) {
        this.teamName = teamName;
        this.points = points;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        String tag = points == 1 ? "pt" : "pts";
        return teamName + ", " + points + " " + tag;
    }
}
