package com.example.pomodorify;
public class StatisticsRecordCard {
    private StatisticsRecord statisticsRecord;
    private Boolean extended;

    public StatisticsRecordCard(StatisticsRecord statisticsRecord, Boolean extended) {
        this.statisticsRecord = statisticsRecord;
        this.extended = extended;
    }

    public StatisticsRecord getStatisticsRecord() {
        return statisticsRecord;
    }
    public Boolean getExtended() {
        return extended;
    }

    public void changeExtended(){
        extended = !extended;
    }
}
