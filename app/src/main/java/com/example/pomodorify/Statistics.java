package com.example.pomodorify;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Statistics extends Fragment {


    public Statistics() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        
        loadStatistics(view);

        return view;
    }

    public void loadStatistics(View view){
        RecyclerView recyclerView = view.findViewById(R.id.statisticsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        GetStatistics getStatistics = DBHelper.getInstance(getActivity());
        List<StatisticsRecord> statisticsRecords = getStatistics.getStatisticsData();

        //Connect record with its card state
        List<StatisticsRecordCard> statisticsRecordCards = new ArrayList<>(statisticsRecords.size());
        for(StatisticsRecord record : statisticsRecords){
            statisticsRecordCards.add(new StatisticsRecordCard(record, false));
        }

        RemoveSelectedStatistic removeSelectedStatistic = DBHelper.getInstance(getActivity());

        ListStatisticsAdapter listStatisticsAdapter = new ListStatisticsAdapter(statisticsRecordCards, removeSelectedStatistic);
        recyclerView.setAdapter(listStatisticsAdapter);
    }

}
