package com.example.metime.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.metime.R;
import com.example.metime.Utilities.DataBaseManager;
import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends Activity {


    private MaterialButton BTN_signup;
    private EditText ET_name;
    private EditText ET_email;
    private EditText ET_password;
    private EditText ET_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
        initViews();

    }

    private void initViews() {
        BTN_signup.setOnClickListener(v->addClientToDB());
    }

    private void addClientToDB() {
        String name = ET_name.getText().toString();
        String email = ET_email.getText().toString();
        String password = ET_password.getText().toString();
        String cPassword = ET_confirm_password.getText().toString();
//        if(DataBaseManager.addClient(name,email,password, cPassword)){
//            moveToHomePage();
//        }

    }

    private void moveToHomePage() {
        Intent i = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        onPause();
    }

    private void findViews() {
        BTN_signup=findViewById(R.id.signup_BTN_signup);
        ET_name=findViewById(R.id.signup_ET_name);
        ET_email=findViewById(R.id.signup_ET_email);
        ET_password=findViewById(R.id.signup_ET_password);
        ET_confirm_password=findViewById(R.id.signup_ET_confirm_password);
    }
}
