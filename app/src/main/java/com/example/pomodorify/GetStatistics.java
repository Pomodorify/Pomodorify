package com.example.pomodorify;

import java.util.List;

public interface GetStatistics {
    public List<StatRecord> getStatisticsData();
    public List<String> getStatisticsDataFormatted();
}
