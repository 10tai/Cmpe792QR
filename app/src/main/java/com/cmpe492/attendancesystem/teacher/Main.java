package com.cmpe492.attendancesystem.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.cmpe492.attendancesystem.AppGlobals;
import com.cmpe492.attendancesystem.BuildConfig;
import com.cmpe492.attendancesystem.Helpers;
import com.cmpe492.attendancesystem.R;

// teacher side main screen
public class Main extends AppCompatActivity implements View.OnClickListener {

    private EditText userName;
    private EditText password;
    private FloatingActionButton floatingActionButton;
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main);
        instance = this;
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        if (BuildConfig.DEBUG) {
            //userName.setText(AppGlobals.KEY_USERNAME);
            //password.setText(AppGlobals.KEY_PASSWORD);
        }
        floatingActionButton = findViewById(R.id.button_login);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                // checking if the right values were put to login for teacher and checking them
                // with values in @AppGlobals.
                if (userName.getText().toString().equals(AppGlobals.KEY_USERNAME) &&
                        password.getText().toString().equals(AppGlobals.KEY_PASSWORD)) {
                    startActivity(new Intent(getApplicationContext(), ClassMenu.class));
                } else if (userName.getText().toString() == null || userName.getText().toString().isEmpty()) {
                    Helpers.showSnackBar(v, "please enter user name");
                } else if (password.getText().toString() == null || password.getText().toString().isEmpty()) {
                    Helpers.showSnackBar(v, "please enter password");
                } else {
                    Helpers.showSnackBar(v, "please enter correct user name Or password");
                }
                    break;
        }
    }
}
