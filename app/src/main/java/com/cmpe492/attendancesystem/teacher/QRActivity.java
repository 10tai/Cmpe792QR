package com.cmpe492.attendancesystem.teacher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpe492.attendancesystem.AppGlobals;
import com.cmpe492.attendancesystem.Helpers;
import com.cmpe492.attendancesystem.R;
import com.cmpe492.attendancesystem.firebase.FireBase;
import com.cmpe492.attendancesystem.gettersetter.ClassName;
import com.firebase.client.Firebase;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import es.dmoral.prefs.Prefs;

/**
 * This class is used to add the class name. I check weather class is already created ot not.
 * i make sure that there is no duplicate by putting time and date with the class name in database.`
 */

public class QRActivity extends AppCompatActivity {

    private ImageView qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.qr_activity_layout);
        qr_code = findViewById(R.id.qr_code);
        String currentClass = getIntent().getStringExtra("currentClass");
        getSupportActionBar().setTitle(currentClass);
        Bitmap myBitmap = QRCode.from(currentClass).bitmap();
        qr_code.setImageBitmap(myBitmap);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:return false;
        }
    }

}
