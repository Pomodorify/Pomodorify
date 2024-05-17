package com.example.pomodorify;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ListStatisticsViewHolder extends RecyclerView.ViewHolder{

    private final TextView itemActivity;
    private final TextView itemDuration;
    private final TextView itemDate;
    public LinearLayout statisticsDetails;

    public ListStatisticsViewHolder(View view, ListStatisticsAdapter listStatisticsAdapter) {
        super(view);

        CardView cardView = (CardView) view.findViewById(R.id.statisticsCard);

        statisticsDetails = (LinearLayout) view.findViewById(R.id.statisticsDetails);

        itemActivity = (TextView) view.findViewById(R.id.statisticsActivity);
        itemDuration = (TextView) view.findViewById(R.id.statisticsDuration);
        itemDate = (TextView) view.findViewById(R.id.statisticsDate);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listStatisticsAdapter.changeCardState(getAdapterPosition());

                listStatisticsAdapter.notifyItemChanged(getAdapterPosition());
            }
        });

        view.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove from database
                listStatisticsAdapter.removeSelectedStatistic.removeSelectedStatistic(listStatisticsAdapter.statisticsData.get(getAdapterPosition()).getId());

                //update adapter (UI)
                listStatisticsAdapter.statisticsData.remove(getAdapterPosition());
                listStatisticsAdapter.notifyItemRemoved(getAdapterPosition());

            }
        });
    }

    public TextView getItemActivity() {
        return itemActivity;
    }
    public TextView getItemDuration() {
        return itemDuration;
    }
    public TextView getItemDate() {
        return itemDate;
    }
}
