package com.example.metime.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.metime.Fragments.TreatmentListFragment;
import com.example.metime.Interfaces.CallBack_TreatmentClicked;
import com.example.metime.Models.Treatment;
import com.example.metime.R;
import com.example.metime.Utilities.DataBaseManager;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ServiceSelectionActivity extends AppCompatActivity {
    private Treatment chosenTreatment;
    private MaterialButton BTN_next;
    private TreatmentListFragment treatmentListFragment;
    private ArrayAdapter<Treatment> adapter;
    private ListView LV_serviceList;
    CallBack_TreatmentClicked callBack_SendClick = new CallBack_TreatmentClicked() {
        public void userClicked(Treatment treatment) {
            setChosenTreatment(treatment);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);
        DataBaseManager.loadTreatmentsDataFromDB(new DataBaseManager.CallBack<List>() {
            @Override
            public void res(List res) {
                findViews();
                initViews();
                initFragments();
                beginTransactions();
            }
        });

    }

    private void initViews() {
        BTN_next.setOnClickListener(v->moveToAppointmentSchedulePage());
    }

    private void initFragments() {
        treatmentListFragment = new TreatmentListFragment();
        treatmentListFragment.setCallBack(callBack_SendClick);
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.service_selection_LISTVIEW_service_list, treatmentListFragment).commit();
    }

    private void moveToAppointmentSchedulePage() {
        if(chosenTreatment !=null) {
            Intent i = new Intent(this, AppointmentSchduleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("NAME", chosenTreatment.getName());
            bundle.putString("TIME", ""+ chosenTreatment.getDuration());
            bundle.putString("SERVICE_ID", chosenTreatment.getId());

            i.putExtras(bundle);
            startActivity(i);
            onPause();
        }
    }

    private void findViews() {
        BTN_next=findViewById(R.id.service_selection_BTN_next);
    }

//    private void initViews() {
//        adapter = new ArrayAdapter<Treatment>(this, R.layout.list_item_layout, R.id.list_item_name,
//                DataBaseManager.getTreatmentsList().getList()) {
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView nameTextView = view.findViewById(R.id.list_item_name);
//                //TextView scoreTextView = view.findViewById(R.id.list_item_score);
//                Treatment treatment = getItem(position);
//                nameTextView.setText(treatment.getName());
//                //scoreTextView.setText("\t\t " + service.getScore());
//                return view;
//            }
//        };
//        LV_serviceList.setAdapter(adapter);
//        LV_serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Handle item click
//                Treatment treatment = adapter.getItem(position);
//                if (treatment != null && callBack_SendClick != null) {
//                    callBack_SendClick.userClicked(treatment);
//                }
//            }
//        });
//    }

    public void setChosenTreatment(Treatment chosenTreatment) {
        this.chosenTreatment = chosenTreatment;
    }
}
