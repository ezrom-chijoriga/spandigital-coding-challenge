package com.spandigital.codingchallenge.ezrom;

import com.spandigital.codingchallenge.ezrom.exception.InvalidMatchResultException;
import com.spandigital.codingchallenge.ezrom.exception.InvalidTeamScoreException;
import com.spandigital.codingchallenge.ezrom.model.Match;
import com.spandigital.codingchallenge.ezrom.model.MatchOutcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}