package com.example.pomodorify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/*
    Adapter fills views.
    ViewHolder is just single view.
 */

public class ListStatisticsAdapter extends RecyclerView.Adapter<ListStatisticsViewHolder> {
    public List<String> statisticsData;

    public ListStatisticsAdapter(List<String> statisticsData) {
        this.statisticsData = statisticsData;
    }

    @Override
    public ListStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_item, parent, false);

        return new ListStatisticsViewHolder(view, this);
    }
    @Override
    public void onBindViewHolder(ListStatisticsViewHolder holder, int position) {
        holder.getTextView().setText(statisticsData.get(position));
    }
    @Override
    public int getItemCount() {
        return statisticsData.size();
    }

}
