package com.example.metime.Fragments;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.metime.Interfaces.CallBack_TreatmentClicked;
import com.example.metime.Models.Treatment;
import com.example.metime.R;
import com.example.metime.Utilities.DataBaseManager;
import com.example.metime.Utilities.TimeManager;


public class TreatmentListFragment extends Fragment {
    private ListView listView;
    LinearLayout list_item_LL;

    private ArrayAdapter<Treatment> adapter;
    private CallBack_TreatmentClicked callBack_SendClick;
    private int selectedPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);

        initViews();
        return view;
    }

    private void initViews() {
        adapter = new ArrayAdapter<Treatment>(getContext(), R.layout.treatment_list_item_layout, R.id.list_item_name, DataBaseManager.getTreatmentsList().getList()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView nameTextView = view.findViewById(R.id.list_item_name);
                TextView scoreTextView = view.findViewById(R.id.list_item_details);
                Treatment t = getItem(position);
                nameTextView.setText(t.getName());
                String d= TimeManager.convertDurationToHours(t.getDuration());
                scoreTextView.setText("Duration: "+d+"\nPrice: "+t.getPrice()+" ₪");

                if (position == selectedPosition) {
                    view.setBackgroundColor(Color.parseColor("#F6CBDB"));  // Set selected color

                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.background));  // Reset to original color
                }
                //scoreTextView.setText("\t\t " + t.getScore());
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                selectedPosition = position;

                Treatment item = adapter.getItem(position);
                if (item != null && callBack_SendClick != null) {
                    callBack_SendClick.userClicked(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void sendClicked() {
        int position = listView.getCheckedItemPosition();
        if (position != ListView.INVALID_POSITION) {
            Treatment record = DataBaseManager.getTreatmentsList().getList().get(position);
            if(callBack_SendClick != null) {
                callBack_SendClick.userClicked(record);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    public void setCallBack(CallBack_TreatmentClicked callBack_SendClick) {
        this.callBack_SendClick = callBack_SendClick;
    }
    private void findViews(View view) {
        listView = view.findViewById(R.id.LISTVIEW_list);
        list_item_LL = view.findViewById(R.id.list_item_LL);
    }
}