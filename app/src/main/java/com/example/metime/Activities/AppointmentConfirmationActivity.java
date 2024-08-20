package com.example.metime.Activities;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.metime.Models.Treatment;
import com.example.metime.R;
import com.example.metime.Utilities.DataBaseManager;
import com.example.metime.Utilities.TimeManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AppointmentConfirmationActivity extends Activity {

    String date;
    String hour;
    String day;
    TextView TV_appointment_day;
    TextView TV_appointment_date;
    TextView TV_start_time;
    TextView TV_end_time;
    MaterialButton BTN_finish;
    String serviceId;
    TextView TV_appointment_treatment;
    TextView TV_appointment_price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_confirmation);
        findViews();
        initViews();
    }

    private void initViews() {
        Intent prev = getIntent();
        day=prev.getExtras().getString("DAY");
        hour= prev.getExtras().getString("HOUR");
        date=prev.getExtras().getString("DATE");
        serviceId=prev.getExtras().getString("SERVICE_ID");

        TV_appointment_date.setText("Date: "+date);
        TV_start_time.setText("Start: "+hour);
        char[] temp=day.toCharArray();
        String m=""+temp[0];
        m=m.toUpperCase();
        temp[0]=m.charAt(0);
        day=new String(temp);
        TV_appointment_day.setText("Day: "+day);
        String end= TimeManager.calculateEndTime(2, hour);
        TV_end_time.setText("End: "+end);
        Treatment t= DataBaseManager.findTreatmentById(serviceId);
        TV_appointment_treatment.setText(t.getName());
        TV_appointment_price.setText("Price: "+t.getPrice() +"₪");
        BTN_finish.setOnClickListener(v->moveToHomePage());

    }

    private void moveToHomePage() {
        Intent i = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        onPause();
    }

    private void findViews() {
       TV_appointment_day= findViewById(R.id.appointment_confirmation_TV_appointment_day);
        TV_appointment_date= findViewById(R.id.appointment_confirmation_TV_appointment_date);
        TV_start_time= findViewById(R.id.appointment_confirmation_TV_start_time);
        TV_end_time= findViewById(R.id.appointment_confirmation_TV_end_time);
        BTN_finish= findViewById(R.id.appointment_confirmation_BTN_finish);
        TV_appointment_treatment= findViewById(R.id.appointment_confirmation_TV_treatment_name);
        TV_appointment_price= findViewById(R.id.appointment_confirmation_TV_price);

    }
}
