package com.spandigital.codingchallenge.ezrom.infrastructure.repository;

import com.spandigital.codingchallenge.ezrom.infrastructure.SystemDB;
import com.spandigital.codingchallenge.ezrom.model.TeamStats;

public class TeamStatsRepositoryImpl implements  TeamStatsRepository{

    private SystemDB systemDB;

    public TeamStatsRepositoryImpl(SystemDB systemDB) {
        this.systemDB = systemDB;
    }

    @Override
    public TeamStats findTeamStats(String teamName) {
        return  systemDB.teamStatsList.get(teamName);
    }

    @Override
    public void insertTeamStats(String teamName, TeamStats teamStats) {
        systemDB.teamStatsList.put(teamName, teamStats);
    }

    @Override
    public void updateTeamStats(String teamName, TeamStats teamStats) {
        systemDB.teamStatsList.replace(teamName, teamStats);
    }
}
