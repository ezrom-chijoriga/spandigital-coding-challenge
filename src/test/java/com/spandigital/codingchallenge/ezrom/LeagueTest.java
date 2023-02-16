package com.spandigital.codingchallenge.ezrom;

import com.spandigital.codingchallenge.ezrom.exception.InvalidMatchResultException;
import com.spandigital.codingchallenge.ezrom.exception.InvalidTeamScoreException;
import com.spandigital.codingchallenge.ezrom.model.Match;
import com.spandigital.codingchallenge.ezrom.model.MatchOutcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LeagueTest {

    @Test
    @DisplayName("Test a win match outcome")
    void getMatchOutcome_Win() {
        //Given:
        int teamOneScore = 3;
        int teamTwoScore = 1;

        //When:
        MatchOutcome matchOutcome = League.getMatchOutcome(teamOneScore, teamTwoScore);

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
        MatchOutcome matchOutcome = League.getMatchOutcome(teamOneScore, teamTwoScore);

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
        MatchOutcome matchOutcome = League.getMatchOutcome(teamOneScore, teamTwoScore);

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
        Match match = League.getMatch(matchResult);

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
        assertThrows(InvalidMatchResultException.class, () -> League.getMatch(matchResult));
    }

    @Test
    @DisplayName("Test that we are throwing InvalidTeamScoreException when a team with no score  has been entered")
    void getMatch_InvalidTeamScoreException() {
        //Given:
        String matchResult = "Tarantulas 1, FC Awesome";

        //When:

        //Then:
        assertThrows(InvalidTeamScoreException.class, () -> League.getMatch(matchResult));
    }
}