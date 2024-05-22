package com.example.pomodorify.database;

import com.example.pomodorify.statistics.StatisticsRecord;
import java.util.List;

public interface GetStatistics {
    public List<StatisticsRecord> getStatisticsData();
}
