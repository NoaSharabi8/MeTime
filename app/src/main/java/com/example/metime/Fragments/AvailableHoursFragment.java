package com.example.metime.Fragments;

import static com.example.metime.Activities.AppointmentSchduleActivity.gettId;
import static com.example.metime.Utilities.DataBaseManager.getCurrentAvailableHours;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.metime.Interfaces.CallBack_HourClicked;
import com.example.metime.R;
import com.example.metime.Utilities.DataBaseManager;
import com.example.metime.Utilities.TimeManager;


public class AvailableHoursFragment extends Fragment {
    private ListView listView;
    LinearLayout list_item_LL;

    private ArrayAdapter<String> adapter;
    private CallBack_HourClicked callBack_SendClick;
    private static int selectedPosition = -1;
    public static String hours2=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        adapter = new ArrayAdapter<String>(getContext(), R.layout.availability_list_layout, R.id.list_item_hour,TimeManager.findOptionalHoursForTreatment(getCurrentAvailableHours(), gettId())) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView nameTextView = view.findViewById(R.id.list_item_hour);
                String t = getItem(position);
                nameTextView.setText(t);;

                if (position == selectedPosition) {
                    //if(!hours2.equals("000000000000000000000000")) {
                        view.setBackgroundColor(Color.parseColor("#F6CBDB"));  // Set selected color
                  //  }

                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.background));  // Reset to original color
                }
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                selectedPosition = position;

                String hour = adapter.getItem(position);
                if (hour != null && callBack_SendClick != null) {
                    callBack_SendClick.userClicked(hour);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void showAvailableHours(int day, int month, int year)  {

        DataBaseManager.getAvailableHours(String.valueOf(day),String.valueOf(month),String.valueOf(year), new DataBaseManager.CallBack<String>() {
            @Override
            public void res(String res) {
             //   if(res!=null && !res.equals("000000000000000000000000")) {
                    //hours2=res;
                    initViews();
                    adapter.notifyDataSetChanged();
                    initSelectedPosition();
              //  }

            }
        });
    }


    public static void initSelectedPosition() {
        selectedPosition=-1;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    public void setCallBack(CallBack_HourClicked callBack_SendClick) {
        this.callBack_SendClick = callBack_SendClick;
    }
    private void findViews(View view) {
        listView = view.findViewById(R.id.LISTVIEW_list);
        list_item_LL = view.findViewById(R.id.hours_LL);
    }

}