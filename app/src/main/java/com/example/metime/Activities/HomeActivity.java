package com.example.metime.Activities;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.metime.Models.User;
import com.example.metime.R;
import com.example.metime.Utilities.DataBaseManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomeActivity extends Activity {
    MaterialButton BTN_make_appointment;
    MaterialButton BTN_cancel_appointment;
    MaterialButton BTN_my_appointments;
    TextView TV_hello_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DataBaseManager.loadTreatmentsDataFromDB(new DataBaseManager.CallBack<List>() {
            @Override
            public void res(List res) {
                findViews();
                initViews();
            }
        });


    }

    private void initViews() {
        BTN_make_appointment.setOnClickListener(v->moveToServiceSelectionPage());
        BTN_my_appointments.setOnClickListener(v->moveToMyAppointmentsPage());
        setHelloMessage();

    }

    private void moveToMyAppointmentsPage() {
        Intent i = new Intent(this, MyAppointmentsActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        onPause();
    }

    private void setHelloMessage() {
        getUserName();
    }
    private void getUserName() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        usersRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String s=snapshot.child("name").getValue(String.class);
                TV_hello_message.append("Hello "+s+" (:");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void moveToServiceSelectionPage() {
        Intent i = new Intent(this, ServiceSelectionActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        onPause();
    }

    private void findViews() {
        BTN_make_appointment=findViewById(R.id.home_BTN_make_appointment);
        //BTN_cancel_appointment=findViewById(R.id.home_BTN_cancel_appointment);
        BTN_my_appointments=findViewById(R.id.home_BTN_my_appointments);
        TV_hello_message=findViewById(R.id.home_TV_hello_message);
    }
}
