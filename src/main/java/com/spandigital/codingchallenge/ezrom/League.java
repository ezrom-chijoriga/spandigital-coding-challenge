package com.spandigital.codingchallenge.ezrom;

import com.spandigital.codingchallenge.ezrom.exception.InvalidMatchResultException;
import com.spandigital.codingchallenge.ezrom.exception.InvalidTeamScoreException;
import com.spandigital.codingchallenge.ezrom.model.Match;
import com.spandigital.codingchallenge.ezrom.model.MatchOutcome;
import com.spandigital.codingchallenge.ezrom.model.TeamStats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class League {

    public static final Map<String, TeamStats> teamStatsList = new HashMap<>();

    private static final int WINNING_POINTS = 3;
    private static final int LOSING_POINTS = 0;
    private static final int DRAW_POINTS = 1;

    public static void updateTeamStats(Match match) {
        MatchOutcome matchOutcome = getMatchOutcome(match.getTeamOneScore(), match.getTeamTwoScore());

        TeamStats teamOneStats = teamStatsList.get(match.getTeamOneName());
        TeamStats teamTwoStats = teamStatsList.get(match.getTeamTwoName());
        switch (matchOutcome) {
            case WIN -> {
                if (teamOneStats != null) {
                    teamOneStats.setPoints(teamOneStats.getPoints() + WINNING_POINTS);
                    teamStatsList.replace(match.getTeamOneName(), teamOneStats);
                } else {
                    teamStatsList.put(match.getTeamOneName(), new TeamStats(match.getTeamOneName(), WINNING_POINTS));
                }
                if (teamTwoStats == null) {
                    teamStatsList.put(match.getTeamTwoName(), new TeamStats(match.getTeamTwoName(), LOSING_POINTS));
                }
            }
            case DRAW -> {
                if (teamOneStats != null) {
                    teamOneStats.setPoints(teamOneStats.getPoints() + DRAW_POINTS);
                    teamStatsList.replace(match.getTeamOneName(), teamOneStats);
                } else {
                    teamStatsList.put(match.getTeamOneName(), new TeamStats(match.getTeamOneName(), DRAW_POINTS));
                }
                if (teamTwoStats != null) {
                    teamTwoStats.setPoints(teamTwoStats.getPoints() + DRAW_POINTS);
                    teamStatsList.replace(match.getTeamTwoName(), teamTwoStats);
                } else {
                    teamStatsList.put(match.getTeamTwoName(), new TeamStats(match.getTeamTwoName(), DRAW_POINTS));
                }
            }
            case LOSS -> {
                if (teamOneStats == null) {
                    teamStatsList.put(match.getTeamTwoName(), new TeamStats(match.getTeamTwoName(), LOSING_POINTS));
                }
                if (teamTwoStats != null) {
                    teamTwoStats.setPoints(teamTwoStats.getPoints() + WINNING_POINTS);
                    teamStatsList.replace(match.getTeamTwoName(), teamTwoStats);
                } else {
                    teamStatsList.put(match.getTeamTwoName(), new TeamStats(match.getTeamTwoName(), WINNING_POINTS));
                }
            }
        }
    }

    public static Match getMatch(String matchResult) throws InvalidMatchResultException, InvalidTeamScoreException {
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

    public static void rankTeams(Map<String, TeamStats> teamStatsList) {
        Comparator<TeamStats> compareByTeamName = Comparator.comparing(TeamStats::getTeamName);
        Comparator<TeamStats> compareByTeamPoints = Comparator.comparing(TeamStats::getPoints).reversed();
        Comparator<TeamStats> compareByTeamNameAndPoints = compareByTeamPoints.thenComparing(compareByTeamName);
        teamStatsList.entrySet().stream().sorted(Map.Entry.comparingByValue(compareByTeamNameAndPoints)).collect(Collectors.toList()).forEach(System.out::println);
    }

    public static MatchOutcome getMatchOutcome(int teamOneScore, int teamTwoScore) {
        if (teamOneScore == teamTwoScore) {
            return MatchOutcome.DRAW;
        } else if (teamOneScore > teamTwoScore) {
            return MatchOutcome.WIN;
        } else {
            return MatchOutcome.LOSS;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Welcome to SPAN digital");
        System.out.println("This is a simple application that calculates the ranking table for a league");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("\n");
        System.out.println("Please enter on a separate line match results in the format of: \"team1 score, team2 score\"");
        System.out.println("Example:");
        System.out.println("\t\tTarantulas 1, FC Awesome 0");
        System.out.println("Type 'exit' to calculate the ranking table for the matches entered");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();
        while (!input.equalsIgnoreCase("exit")) {
            Match match = null;
            try {
                match = getMatch(input);
            } catch (InvalidMatchResultException e) {
                System.out.println("Error: Please enter two teams");
            } catch (InvalidTeamScoreException e) {
                System.out.println("Error: Please enter a team with their respective score");
            }

            if (match != null) {
                updateTeamStats(match);
            }
            input = bufferedReader.readLine();
        }
        rankTeams(teamStatsList);
        bufferedReader.close();
    }
}
