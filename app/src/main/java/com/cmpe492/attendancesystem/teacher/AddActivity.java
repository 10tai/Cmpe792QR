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
 * it makes sure that there is no duplicate by putting time and date with the class name in database.`
 */

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name;
    private TextView timeDate;
    private FloatingActionButton floatingActionButton;
    private String date;
    private CheckBox disableAfter2min;
    private CheckBox pin;
    //private boolean pinStatus;
    //private String savedQRcode;
    private ImageView qr_code;
    private EditText error_qr;
    private boolean bGeneratedQr = false;
    private ClassName className = null;
    private File qrcode_file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.add_class_layout);
        name = findViewById(R.id.class_name);
        timeDate = findViewById(R.id.time);
        pin = findViewById(R.id.pin);
        qr_code = findViewById(R.id.qr_code);
        error_qr = findViewById(R.id.error_qr);

//        pinStatus = Prefs.with(this).readBoolean("Pin_status");
//        savedQRcode = Prefs.with(this).read("SavedQRCode", null);
//        if (savedQRcode != null && pinStatus == true) {
//            pin.setChecked(true);
//
//        } else {
//            pin.setVisibility(View.INVISIBLE);
//        }
        disableAfter2min = findViewById(R.id.disable_check);
        floatingActionButton = findViewById(R.id.button_add);
        floatingActionButton.setOnClickListener(this);
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
        date = df.format(Calendar.getInstance().getTime());
        timeDate.setText(date);
        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm(1)) {
                    className = new ClassName();
                    className.setName(name.getText().toString() + " " + date);
                    className.setAttendanceActive(true);
                    className.setTime(date);
                    String text = className.getName(); // Whatever you need to encode in the QR code
                    qrcode_file = QRCode.from(text).withSize(200, 200).file();
                    Bitmap myBitmap = BitmapFactory.decodeFile(qrcode_file.getAbsolutePath());
                    qr_code.setImageBitmap(myBitmap);
                    bGeneratedQr = true;
                }
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                if (validateForm(2)) {
                    Firebase firebase = FireBase.getInstance(getApplicationContext());
                    firebase.child(AppGlobals.CLASSES).child(className.getName()).setValue(className);
                    Helpers.showSnackBar(v, "Class Added");
                    if (disableAfter2min.isChecked()) {
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Firebase firebase = FireBase.getInstance(getApplicationContext());
                                HashMap<String, Object> result = new HashMap<>();
                                result.put("attendanceActive", false);
                                firebase.child(AppGlobals.CLASSES).child(className.getName()).updateChildren(result);
                            }
                        }, (1000*60)*2);
                    }
                    // if teacher has marked that close attendance after 2 minutes then call this to
                    // inactivate the attendance.
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AddActivity.this.finish();
                        }
                    }, 2000);
                }
                break;
        }
    }

    private boolean validateForm(int op) {
        boolean isValid = true;
        if (name.getText().toString() == null || name.getText().toString().isEmpty()) {
            isValid = false;
            name.setError("please add class period");
        } else {
            name.setError(null);
        }
        op--;
        if (op <= 0) {
            return isValid;
        }
        if (bGeneratedQr == false) {
            isValid = false;
            error_qr.setVisibility(View.VISIBLE);
            error_qr.setError("please add QR code");
        } else {
            error_qr.setError(null);
        }
        return  isValid;
    }
}
