package com.spandigital.codingchallenge.ezrom.model;

public class Match {
    private String teamOneName;
    private int teamOneScore;
    private String teamTwoName;
    private int teamTwoScore;

    public Match(String teamOneName, int teamOneScore, String teamTwoName, int teamTwoScore) {
        this.teamOneName = teamOneName;
        this.teamOneScore = teamOneScore;
        this.teamTwoName = teamTwoName;
        this.teamTwoScore = teamTwoScore;
    }

    public String getTeamOneName() {
        return teamOneName;
    }

    public void setTeamOneName(String teamOneName) {
        this.teamOneName = teamOneName;
    }

    public int getTeamOneScore() {
        return teamOneScore;
    }

    public void setTeamOneScore(int teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public String getTeamTwoName() {
        return teamTwoName;
    }

    public void setTeamTwoName(String teamTwoName) {
        this.teamTwoName = teamTwoName;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }

    public void setTeamTwoScore(int teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }

    @Override
    public String toString() {
        return "Match{teamOneName: ".concat(getTeamOneName())
                .concat(", teamOneScore: ").concat(String.valueOf(getTeamOneScore()))
                .concat(", teamTwoName: ").concat(getTeamTwoName())
                .concat(", teamTwoScore: ").concat(String.valueOf(getTeamTwoScore()));
    }
}
