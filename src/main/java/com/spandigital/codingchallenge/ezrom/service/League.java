package com.spandigital.codingchallenge.ezrom.service;

import com.spandigital.codingchallenge.ezrom.exception.InvalidMatchResultException;
import com.spandigital.codingchallenge.ezrom.exception.InvalidTeamScoreException;
import com.spandigital.codingchallenge.ezrom.infrastructure.repository.TeamStatsRepository;
import com.spandigital.codingchallenge.ezrom.model.Match;
import com.spandigital.codingchallenge.ezrom.model.MatchOutcome;
import com.spandigital.codingchallenge.ezrom.model.TeamStats;
import com.spandigital.codingchallenge.ezrom.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class League {

    private static final int WINNING_POINTS = 3;
    private static final int LOSING_POINTS = 0;
    private static final int DRAW_POINTS = 1;

    private TeamStatsRepository teamStatsRepository;

    public League(TeamStatsRepository teamStatsRepository) {
        this.teamStatsRepository = teamStatsRepository;
    }

    /**
     * Updates team statistics i.e points for every team that participated in a match
     *
     * @param match details of the match
     */
    public void updateTeamStats(Match match) {
        MatchOutcome matchOutcome = getMatchOutcome(match.getTeamOneScore(), match.getTeamTwoScore());

        TeamStats teamOneStats = teamStatsRepository.findTeamStats(match.getTeamOneName());
        TeamStats teamTwoStats = teamStatsRepository.findTeamStats(match.getTeamTwoName());
        switch (matchOutcome) {
            case WIN -> {
                if (teamOneStats != null) {
                    teamOneStats.setPoints(teamOneStats.getPoints() + WINNING_POINTS);
                    teamStatsRepository.updateTeamStats(match.getTeamOneName(), teamOneStats);
                } else {
                    teamStatsRepository.insertTeamStats(match.getTeamOneName(), new TeamStats(match.getTeamOneName(), WINNING_POINTS));
                }
                if (teamTwoStats == null) {
                    teamStatsRepository.insertTeamStats(match.getTeamTwoName(), new TeamStats(match.getTeamTwoName(), LOSING_POINTS));
                }
            }
            case DRAW -> {
                if (teamOneStats != null) {
                    teamOneStats.setPoints(teamOneStats.getPoints() + DRAW_POINTS);
                    teamStatsRepository.updateTeamStats(match.getTeamOneName(), teamOneStats);
                } else {
                    teamStatsRepository.insertTeamStats(match.getTeamOneName(), new TeamStats(match.getTeamOneName(), DRAW_POINTS));
                }
                if (teamTwoStats != null) {
                    teamTwoStats.setPoints(teamTwoStats.getPoints() + DRAW_POINTS);
                    teamStatsRepository.updateTeamStats(match.getTeamTwoName(), teamTwoStats);
                } else {
                    teamStatsRepository.insertTeamStats(match.getTeamTwoName(), new TeamStats(match.getTeamTwoName(), DRAW_POINTS));
                }
            }
            case LOSS -> {
                if (teamOneStats == null) {
                    teamStatsRepository.insertTeamStats(match.getTeamTwoName(), new TeamStats(match.getTeamTwoName(), LOSING_POINTS));
                }
                if (teamTwoStats != null) {
                    teamTwoStats.setPoints(teamTwoStats.getPoints() + WINNING_POINTS);
                    teamStatsRepository.updateTeamStats(match.getTeamTwoName(), teamTwoStats);
                } else {
                    teamStatsRepository.insertTeamStats(match.getTeamTwoName(), new TeamStats(match.getTeamTwoName(), WINNING_POINTS));
                }
            }
        }
    }

    /**
     * Gets the match object from a string value
     *
     * @param matchResult result of the match in string format
     */
    public Match getMatch(String matchResult) throws InvalidMatchResultException, InvalidTeamScoreException {
        String[] match = matchResult.replaceAll("\\s+", " ").trim().split(", ");
        if (match.length != 2) {
            throw new InvalidMatchResultException("Invalid match result entered");
        }

        String teamOneStr = match[0];
        int lastIndexOf = teamOneStr.lastIndexOf(" ");
        String[] teamOneMatchResult = {teamOneStr.substring(0, lastIndexOf), teamOneStr.substring(lastIndexOf + 1)};

        String teamTwoStr = match[1];
        lastIndexOf = teamTwoStr.lastIndexOf(" ");
        String[] teamTwoMatchResult = {teamTwoStr.substring(0, lastIndexOf), teamTwoStr.substring(lastIndexOf + 1)};

        int teamOneScore;
        int teamTwoScore;
        try {
            teamOneScore = Integer.parseInt(teamOneMatchResult[1]);
            teamTwoScore = Integer.parseInt(teamTwoMatchResult[1]);
        } catch (NumberFormatException e) {
            throw new InvalidTeamScoreException("Invalid team score entered");
        }
        return new Match(teamOneMatchResult[0], teamOneScore, teamTwoMatchResult[0], teamTwoScore);
    }

    /**
     * Ranks the team based on points and name
     * 1st - by points
     * 2nd - by name alphabetically when two or more teams have the same rank
     *
     * @param teamStatsList a hashmap of team statistics
     */
    public List<TeamStats> rankTeams(Map<String, TeamStats> teamStatsList) {
        Comparator<TeamStats> compareByTeamName = Comparator.comparing(TeamStats::getTeamName);
        Comparator<TeamStats> compareByTeamPoints = Comparator.comparing(TeamStats::getPoints).reversed();
        Comparator<TeamStats> compareByTeamNameAndPoints = compareByTeamPoints.thenComparing(compareByTeamName);
        return teamStatsList.values().stream().sorted(compareByTeamNameAndPoints).collect(Collectors.toList());
    }

    /**
     * Gets match outcome based on the teams score
     *
     * @param teamOneScore score of one team
     * @param teamTwoScore score of the other team
     */
    public MatchOutcome getMatchOutcome(int teamOneScore, int teamTwoScore) {
        if (teamOneScore == teamTwoScore) {
            return MatchOutcome.DRAW;
        } else if (teamOneScore > teamTwoScore) {
            return MatchOutcome.WIN;
        } else {
            return MatchOutcome.LOSS;
        }
    }
}
