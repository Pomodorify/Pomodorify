package com.example.pomodorify;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        //zaladuj statystyki
        fetchStatistics(view);
        return view;
    }

    public void fetchStatistics(View view){
        //variables to display data
        ArrayList<String> myDataList = new ArrayList<>();
        ListView myListView = view.findViewById(R.id.statList);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, myDataList);

        //read from database
        GetStatistics handleStatistics = new DBHelper(getActivity());

        List<StatRecord> statRecords = handleStatistics.getStatisticsData();
        for(StatRecord record : statRecords){
            myDataList.add(record.toString());
        }

        myListView.setAdapter(myAdapter);
    }

}