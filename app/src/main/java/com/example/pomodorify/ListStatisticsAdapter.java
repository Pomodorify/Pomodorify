package com.example.pomodorify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
    Adapter fills views.
    ViewHolder is just single view.
 */

public class ListStatisticsAdapter extends RecyclerView.Adapter<ListStatisticsViewHolder> {
    public List<StatRecord> statisticsData;
    private final boolean[] cardStateExpanded;
    public RemoveSelectedStatistic removeSelectedStatistic;

    public ListStatisticsAdapter(List<StatRecord> statisticsData, RemoveSelectedStatistic removeSelectedStatistic) {
        this.statisticsData = statisticsData;
        this.removeSelectedStatistic = removeSelectedStatistic;
        this.cardStateExpanded = new boolean[statisticsData.size()];
    }

    @Override
    public ListStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_item, parent, false);

        return new ListStatisticsViewHolder(view, this);
    }
    @Override
    public void onBindViewHolder(ListStatisticsViewHolder holder, int position) {

        if(cardStateExpanded[position]){
            holder.statisticsDetails.setVisibility(View.VISIBLE);
        }else{
            holder.statisticsDetails.setVisibility(View.GONE);
        }

        String activity = statisticsData.get(position).getActivity();
        if(activity.isEmpty())
            activity = "Focus";

        holder.getItemActivity().setText(activity);
        holder.getItemDuration().setText("Duration: " + statisticsData.get(position).getTime());
        holder.getItemDate().setText("Date: " + statisticsData.get(position).getFormattedDate());
    }
    @Override
    public int getItemCount() {
        return statisticsData.size();
    }

    public void changeCardState(int index){
        cardStateExpanded[index] = !cardStateExpanded[index];
    }
}
