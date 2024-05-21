package com.example.pomodorify.statistics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodorify.R;
import com.example.pomodorify.database.RemoveSelectedStatistic;

import java.util.List;

public class ListStatisticsAdapter extends RecyclerView.Adapter<ListStatisticsViewHolder> {
    public List<StatisticsRecordCard> statisticsRecordCard;
    public RemoveSelectedStatistic removeSelectedStatistic;

    public ListStatisticsAdapter(List<StatisticsRecordCard> statisticsRecordCard, RemoveSelectedStatistic removeSelectedStatistic) {
        this.statisticsRecordCard = statisticsRecordCard;
        this.removeSelectedStatistic = removeSelectedStatistic;
    }

    @Override
    public ListStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_item, parent, false);

        return new ListStatisticsViewHolder(view, this);
    }
    @Override
    public void onBindViewHolder(ListStatisticsViewHolder holder, int position) {

        if(statisticsRecordCard.get(position).getExtended()){
            holder.getStatisticsDetails().setVisibility(View.VISIBLE);
        }else{
            holder.getStatisticsDetails().setVisibility(View.GONE);
        }

        String activity = statisticsRecordCard.get(position).getStatisticsRecord().getActivity();
        if(activity.isEmpty())
            activity = "Focus";

        holder.getItemActivity().setText(activity);
        holder.getItemDuration().setText("Duration: " + statisticsRecordCard.get(position).getStatisticsRecord().getTime());
        holder.getItemDate().setText("Date: " + statisticsRecordCard.get(position).getStatisticsRecord().getFormattedDate());
    }
    @Override
    public int getItemCount() {
        return statisticsRecordCard.size();
    }
}
