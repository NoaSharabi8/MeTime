package com.example.metime.Utilities;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metime.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView dayOfMonth;
        private final CalendarAdapter.OnItemListener onItemListener;
        private static TextView prevDayChoice;
        public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener)
        {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
            TextView itemTextView = view.findViewById(R.id.cellDayText);
            GradientDrawable background = (GradientDrawable) itemTextView.getBackground();
            background.setColor(Color.parseColor("#FFD84C81"));
            if(prevDayChoice !=null) {
                GradientDrawable prevBackground = (GradientDrawable) prevDayChoice.getBackground();
                prevBackground.setColor(Color.parseColor("#F6CBDB"));
            }
            prevDayChoice =itemTextView;
        }
    }

