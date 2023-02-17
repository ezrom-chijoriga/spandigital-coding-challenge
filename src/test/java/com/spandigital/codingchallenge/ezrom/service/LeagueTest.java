package com.spandigital.codingchallenge.ezrom.service;

import com.spandigital.codingchallenge.ezrom.exception.InvalidMatchResultException;
import com.spandigital.codingchallenge.ezrom.exception.InvalidTeamScoreException;
import com.spandigital.codingchallenge.ezrom.infrastructure.SystemDB;
import com.spandigital.codingchallenge.ezrom.infrastructure.repository.TeamStatsRepositoryImpl;
import com.spandigital.codingchallenge.ezrom.model.Match;
import com.spandigital.codingchallenge.ezrom.model.MatchOutcome;
import com.spandigital.codingchallenge.ezrom.model.TeamStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LeagueTest {
    private static final int WINNING_POINTS = 3;
    private static final int LOSING_POINTS = 0;

    private SystemDB systemDB;
    private TeamStatsRepositoryImpl teamStatsRepository;
    private League league;

    @BeforeEach
    void setUp() {
        systemDB = SystemDB.getDbInstance();
        teamStatsRepository = new TeamStatsRepositoryImpl(systemDB);
        league = new League(teamStatsRepository);
    }

    @Test
    @DisplayName("Test a win match outcome")
    void getMatchOutcome_Win() {
        //Given:
        int teamOneScore = 3;
        int teamTwoScore = 1;

        //When:
        MatchOutcome matchOutcome = league.getMatchOutcome(teamOneScore, teamTwoScore);

        //Then:
        assertEquals(MatchOutcome.WIN, matchOutcome);
    }

    @Test
    @DisplayName("Test a loss match outcome")
    void getMatchOutcome_Loss() {
        //Given:
        int teamOneScore = 1;
        int teamTwoScore = 6;

        //When:
        MatchOutcome matchOutcome = league.getMatchOutcome(teamOneScore, teamTwoScore);

        //Then:
        assertEquals(MatchOutcome.LOSS, matchOutcome);
    }

    @Test
    @DisplayName("Test a draw match outcome")
    void getMatchOutcome_Draw() {
        //Given:
        int teamOneScore = 4;
        int teamTwoScore = 4;

        //When:
        MatchOutcome matchOutcome = league.getMatchOutcome(teamOneScore, teamTwoScore);

        //Then:
        assertEquals(MatchOutcome.DRAW, matchOutcome);
    }

    @Test
    @DisplayName("Test a valid match is returned")
    void getMatch_Success() throws InvalidTeamScoreException, InvalidMatchResultException {
        //Given:
        String teamOneName = "Tarantulas";
        int teamOneScore = 1;

        String teamTwoName = "FC Awesome";
        int teamTwoScore = 0;

        String matchResult = "Tarantulas 1, FC Awesome 0";

        //When:
        Match match = league.getMatch(matchResult);

        //Then:
        assertEquals(teamOneName, match.getTeamOneName());
        assertEquals(teamOneScore, match.getTeamOneScore());
        assertEquals(teamTwoName, match.getTeamTwoName());
        assertEquals(teamTwoScore, match.getTeamTwoScore());
    }

    @Test
    @DisplayName("Test that we are throwing InvalidMatchResultException when two team haven't been entered")
    void getMatch_InvalidMatchResultException() {
        //Given:
        String matchResult = "Tarantulas 1";

        //When:

        //Then:
        assertThrows(InvalidMatchResultException.class, () -> league.getMatch(matchResult));
    }

    @Test
    @DisplayName("Test that we are throwing InvalidTeamScoreException when a team with no score  has been entered")
    void getMatch_InvalidTeamScoreException() {
        //Given:
        String matchResult = "Tarantulas 1, FC Awesome";

        //When:

        //Then:
        assertThrows(InvalidTeamScoreException.class, () -> league.getMatch(matchResult));
    }

    @Test
    @DisplayName("Test that team stats gets update for a respective match when there is no initial points")
    void updateTeamStats_WithoutInitialPoints() {
        //Given:
        String teamOneName = "Tarantulas";
        int teamOneScore = 1;

        String teamTwoName = "FC Awesome";
        int teamTwoScore = 0;
        Match match = new Match(teamOneName, teamOneScore, teamTwoName, teamTwoScore);

        //When:
        league.updateTeamStats(match);

        //Then:
        TeamStats teamOneStats =teamStatsRepository.findTeamStats(teamOneName);
        assertEquals(teamOneName, teamOneStats.getTeamName());
        assertEquals(WINNING_POINTS, teamOneStats.getPoints());

        TeamStats teamTwoStats = teamStatsRepository.findTeamStats(teamTwoName);
        assertEquals(teamTwoName, teamTwoStats.getTeamName());
        assertEquals(LOSING_POINTS, teamTwoStats.getPoints());
    }

    @Test
    @DisplayName("Test that team stats gets update for a respective match when there is initial points")
    void updateTeamStats_WithInitialPoints() {
        //Given:
        String teamOneName = "Tarantulas";
        int teamOneScore = 1;
        int teamOneInitialPoints = 3;

        String teamTwoName = "FC Awesome";
        int teamTwoScore = 0;
        int teamTwoInitialPoints = 1;

        Match match = new Match(teamOneName, teamOneScore, teamTwoName, teamTwoScore);

        teamStatsRepository.insertTeamStats(teamOneName, new TeamStats(teamOneName, teamOneInitialPoints));
        teamStatsRepository.insertTeamStats(teamTwoName, new TeamStats(teamTwoName, teamTwoInitialPoints));

        //When:
        league.updateTeamStats(match);

        //Then:
        TeamStats teamOneStats = teamStatsRepository.findTeamStats(teamOneName);
        assertEquals(teamOneName, teamOneStats.getTeamName());
        assertEquals(teamOneInitialPoints + WINNING_POINTS, teamOneStats.getPoints());

        TeamStats teamTwoStats = teamStatsRepository.findTeamStats(teamTwoName);
        assertEquals(teamTwoName, teamTwoStats.getTeamName());
        assertEquals(teamTwoInitialPoints + LOSING_POINTS, teamTwoStats.getPoints());
    }

    @Test
    @DisplayName("Test that teams are ranked in order of highest points and alphabetically for equal points teams")
    void rankTeams() {
        //Given:
        TeamStats firstTeam = new TeamStats("Tarantulas", 6);
        TeamStats lastTeam = new TeamStats("Grouches", 0);
        Map<String, TeamStats> teamStatsList = Map.of(
                "Tarantulas", new TeamStats("Tarantulas", 6),
                "FC Awesome", new TeamStats("FC Awesome", 1),
                "Lions", new TeamStats("Lions", 5),
                "Grouches", new TeamStats("Grouches", 0),
                "Snakes", new TeamStats("Snakes", 1));


        //When:
        List<TeamStats> teamsStats = league.rankTeams(teamStatsList);

        //Then:
        assertEquals(teamStatsList.size(), teamsStats.size());
        assertEquals(firstTeam.getTeamName(), teamsStats.get(0).getTeamName());
        assertEquals(firstTeam.getPoints(), teamsStats.get(0).getPoints());
        assertEquals(lastTeam.getTeamName(), teamsStats.get(teamsStats.size() - 1).getTeamName());
        assertEquals(lastTeam.getPoints(), teamsStats.get(teamsStats.size() - 1).getPoints());
    }

}