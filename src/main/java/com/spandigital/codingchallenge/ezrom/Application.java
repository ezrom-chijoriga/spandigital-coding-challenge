package com.spandigital.codingchallenge.ezrom;

import com.spandigital.codingchallenge.ezrom.exception.InvalidMatchResultException;
import com.spandigital.codingchallenge.ezrom.exception.InvalidTeamScoreException;
import com.spandigital.codingchallenge.ezrom.infrastructure.SystemDB;
import com.spandigital.codingchallenge.ezrom.infrastructure.repository.TeamStatsRepositoryImpl;
import com.spandigital.codingchallenge.ezrom.model.Match;
import com.spandigital.codingchallenge.ezrom.model.TeamStats;
import com.spandigital.codingchallenge.ezrom.service.League;
import com.spandigital.codingchallenge.ezrom.util.FileUtil;

import java.io.IOException;
import java.util.List;

public class Application {

    public static void main(String[] args) throws IOException {
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Welcome to SPAN digital");
        System.out.println("This is a simple application that calculates the ranking table for a league");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("\n");

        League league = new League(new TeamStatsRepositoryImpl(SystemDB.getDbInstance()));
        List<String> matchResults = FileUtil.readFile(args[0]);

        System.out.println("Sample inputs:");
        matchResults.forEach(System.out::println);

        matchResults.forEach(matchResult -> {
            Match match = null;
            try {
                match = league.getMatch(matchResult);
            } catch (InvalidMatchResultException e) {
                System.out.println("Error: Please enter two teams");
            } catch (InvalidTeamScoreException e) {
                System.out.println("Error: Please enter a team with their respective score");
            }
            if (match != null) {
                league.updateTeamStats(match);
            }
        });

        System.out.println("\n");
        System.out.println("Expected output:");
        int count = 1;
        for (TeamStats teamStats : league.rankTeams(SystemDB.teamStatsList)) {
            System.out.println(count + ". " + teamStats);
            count++;
        }
    }
}
