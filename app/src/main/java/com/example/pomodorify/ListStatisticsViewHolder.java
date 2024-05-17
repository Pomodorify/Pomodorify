package com.example.pomodorify;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ListStatisticsViewHolder extends RecyclerView.ViewHolder{

    private final TextView itemData;
    public ListStatisticsViewHolder(View view, ListStatisticsAdapter listStatisticsAdapter) {
        super(view);

        itemData = (TextView) view.findViewById(R.id.statisticsText);

        view.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove from database
                listStatisticsAdapter.removeSelectedStatistic.removeSelectedStatistic(listStatisticsAdapter.statisticsData.get(getAdapterPosition()).id);

                //update adapter (UI)
                listStatisticsAdapter.statisticsData.remove(getAdapterPosition());
                listStatisticsAdapter.notifyItemRemoved(getAdapterPosition());

            }
        });
    }

    public TextView getTextView(){
        return itemData;
    }
}
