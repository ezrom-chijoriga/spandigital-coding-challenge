package com.spandigital.codingchallenge.ezrom.infrastructure.repository;

import com.spandigital.codingchallenge.ezrom.model.TeamStats;

public interface TeamStatsRepository {

    TeamStats findTeamStats(String teamName);

    void insertTeamStats(String teamName, TeamStats teamStats);

    void updateTeamStats(String teamName, TeamStats teamStats);
}
