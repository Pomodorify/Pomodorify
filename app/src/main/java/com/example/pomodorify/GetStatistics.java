package com.example.pomodorify;

import java.util.List;

@FunctionalInterface
public interface GetStatistics {
    public List<StatRecord> getStatisticsData();//do odczytania statystyk
}
