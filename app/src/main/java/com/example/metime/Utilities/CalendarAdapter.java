package com.example.metime.Utilities;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metime.Activities.AppointmentSchduleActivity;
import com.example.metime.R;

import java.util.ArrayList;

    public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
    {
        private final ArrayList<String> daysOfMonth;
        private final OnItemListener onItemListener;


        public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener)
        {
            this.daysOfMonth = daysOfMonth;
            this.onItemListener = onItemListener;
        }

        @NonNull
        @Override
        public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.calendar_cell, parent, false);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
            return new CalendarViewHolder(view, onItemListener);
        }

        @Override
        public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
        {
            holder.dayOfMonth.setText(daysOfMonth.get(position));
            String day = daysOfMonth.get(position);

            // Check if the day is non-active (empty string) and change the background
            if (day.equals("")) {
                // Non-active day, set the background to a different color (e.g., light grey)
                //holder.itemView.setBackgroundColor(Color.parseColor("#E0E0E0")); // Grey background for non-active days
                holder.dayOfMonth.setBackgroundResource(R.drawable.non_exist_circle_shape);
            } else {
                int month= AppointmentSchduleActivity.getMonth();
                int year = AppointmentSchduleActivity.getYear();
                DataBaseManager.getAvailableHours(String.valueOf(day),String.valueOf(month),String.valueOf(year), new DataBaseManager.CallBack<String>() {
                    @Override
                    public void res(String res) {
                        if(res!=null  && !res.equals("000000000000000000000000") ) {
                            holder.dayOfMonth.setBackgroundResource(R.drawable.circle_shape);
                        } else {
                            holder.dayOfMonth.setBackgroundResource(R.drawable.non_active_circle_shape);
                            holder.itemView.setClickable(false);

                        }

                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            return daysOfMonth.size();
        }

        public interface  OnItemListener
        {
            void onItemClick(int position, String dayText);
        }
    }


