package com.cmpe492.attendancesystem.student;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmpe492.attendancesystem.AppGlobals;
import com.cmpe492.attendancesystem.Helpers;
import com.cmpe492.attendancesystem.R;
import com.cmpe492.attendancesystem.firebase.FireBase;
import com.cmpe492.attendancesystem.gettersetter.StudentDetail;
import com.cmpe492.attendancesystem.teacher.Main;
import com.firebase.client.Firebase;

// student side main screen
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button teacher;
    private EditText name;
    private EditText rollNo;
    private FloatingActionButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        teacher = findViewById(R.id.teacher);
        name = findViewById(R.id.name);
        rollNo = findViewById(R.id.roll_no);
        loginButton = findViewById(R.id.button_login);
        //setting the click listeners for both button
        loginButton.setOnClickListener(this);
        teacher.setOnClickListener(this);
        // checking in shared pereferences if student is already login or not.
        // if user is login then set name and roll no and disable the edittext fields.
        if (AppGlobals.isLogin()) {
            name.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_NAME));
            rollNo.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ROLL_NO));
            name.setFocusable(false);
            rollNo.setFocusable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                if (!AppGlobals.isLogin()) {
                    if (name.getText().toString() == null || name.getText().toString().trim().isEmpty()) {
                        Helpers.showSnackBar(v, "please enter your name");
                        name.setError("Name is required!");
                        return;
                    }
                    if (rollNo.getText().toString() == null || rollNo.getText().toString().trim().isEmpty()) {
                        Helpers.showSnackBar(v, "please enter your Roll No");
                        rollNo.setError("Roll No is required");
                        return;
                    }
                    if (name.getText().toString().contains(".")) {
                        Helpers.showSnackBar(v, "Name should not contain '.'");
                        return;
                    }
                    if (rollNo.getText().toString().contains(".")) {
                        Helpers.showSnackBar(v, "Roll No should not contain '.'");
                        return;
                    }
                    //This code is used to login a user,Roll no is unique in it. if a user is
                    // already created in the database it will login else it will create a new user.
                    Firebase firebase = FireBase.getInstance(getApplicationContext());
                    StudentDetail studentDetail = new StudentDetail();
                    studentDetail.setName(name.getText().toString());
                    studentDetail.setRollNo(rollNo.getText().toString());
                    firebase.child(AppGlobals.STUDENT).child(rollNo.getText().toString())
                            .setValue(studentDetail);
                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_NAME, name.getText().toString());
                    AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_ROLL_NO, rollNo.getText().toString());
                    AppGlobals.saveLogin(true);
                }
                // if there was not error in entered details of student then student will be sent to
                // next screen to select the class of which a student want to mark the attendance.
                startActivity(new Intent(getApplicationContext(), MarkAttendance.class));
                break;

            case R.id.teacher:
                // if user will click on I am a teacher. he will be send to teacher static login screen.
                startActivity(new Intent(getApplicationContext(), Main.class));
                break;

        }
    }
}
