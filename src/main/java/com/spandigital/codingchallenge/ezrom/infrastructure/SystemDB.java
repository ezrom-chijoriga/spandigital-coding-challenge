package com.spandigital.codingchallenge.ezrom.infrastructure;

import com.spandigital.codingchallenge.ezrom.model.TeamStats;

import java.util.HashMap;
import java.util.Map;

public class SystemDB {

    private static SystemDB dbInstance = null;

    public static final Map<String, TeamStats> teamStatsList = new HashMap<>();

    private SystemDB() {
    }

    public static SystemDB getDbInstance() {
        if (dbInstance == null)
            dbInstance = new SystemDB();
        return dbInstance;
    }
}
