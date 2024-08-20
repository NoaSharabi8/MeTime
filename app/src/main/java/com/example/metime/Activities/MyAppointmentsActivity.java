package com.example.metime.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.metime.Fragments.AppointmentsListFragment;
import com.example.metime.Fragments.TreatmentListFragment;
import com.example.metime.Interfaces.CallBack_AppointmentClicked;
import com.example.metime.Interfaces.CallBack_TreatmentClicked;
import com.example.metime.Models.Appointment;
import com.example.metime.Models.Treatment;
import com.example.metime.R;
import com.example.metime.Utilities.DataBaseManager;
import com.example.metime.Utilities.TimeManager;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MyAppointmentsActivity extends AppCompatActivity {

    private Appointment chosenAppointment;
    private MaterialButton BTN_cancel_appointment;
    private MaterialButton BTN_update_appointment;
    private MaterialButton BTN_back;
    private AppointmentsListFragment appointmentsListFragment;
    CallBack_AppointmentClicked callBack_SendClick = new CallBack_AppointmentClicked() {
        public void userClicked(Appointment appointment) {
            setChosenAppointment(appointment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);
        DataBaseManager.loadAppointmentsDataFromDB(new DataBaseManager.CallBack<List>() {
            @Override
            public void res(List res) {
                findViews();
                initViews();
                initFragments();
                beginTransactions();
            }
        });

    }

    public void setChosenAppointment(Appointment chosenAppointment) {
        this.chosenAppointment = chosenAppointment;
    }

    private void initViews() {
        BTN_back.setOnClickListener(v-> returnToHomePage());
        BTN_cancel_appointment.setOnClickListener(v->cancelAppointment());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }

    private void cancelAppointment() {
        if(chosenAppointment!=null) {
            DataBaseManager.removeAppointmentFromDB(chosenAppointment);
            String date = chosenAppointment.getDate();
            String[] temp = date.split("/");
            DataBaseManager.getAvailableHours(temp[0], temp[1], temp[2], new DataBaseManager.CallBack<String>() {
                @Override
                public void res(String res) {
                    String newValue = TimeManager.returnHoursToAvailability(chosenAppointment.getTreatmentId(), chosenAppointment.getBeginningTime(), res);
                    DataBaseManager.returnAvailabilityHours(temp[0], temp[1], temp[2], newValue);
                    showOkayDialog();
                    chosenAppointment=null;
                }
            });

        }
    }

    private void returnToHomePage() {
        Intent i = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        onStop();
    }

    private void findViews() {
        BTN_cancel_appointment=findViewById(R.id.my_appointments_BTN_cancel);
        BTN_update_appointment=findViewById(R.id.my_appointments_BTN_update);
        BTN_back=findViewById(R.id.my_appointments_BTN_back);
    }

    private void initFragments() {
        appointmentsListFragment = new AppointmentsListFragment();
        appointmentsListFragment.setCallBack(callBack_SendClick);
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.my_appointments_LISTVIEW_appointments_list, appointmentsListFragment).commit();
    }

    private void showOkayDialog() {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title of the dialog
        builder.setTitle("Appointment Canceled");

        // Set the message to be displayed in the dialog
        builder.setMessage("Hope to see you again soon !");

        // Add an "Okay" button to the dialog
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataBaseManager.loadAppointmentsDataFromDB(new DataBaseManager.CallBack<List>() {
                    @Override
                    public void res(List res) {
                        initFragments();
                        beginTransactions();
                    }
                });
                dialog.dismiss();
            }
        });

        // Optional: Set the dialog to be cancelable
        builder.setCancelable(true);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
