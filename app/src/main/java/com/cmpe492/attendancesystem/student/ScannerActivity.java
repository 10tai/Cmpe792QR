package com.cmpe492.attendancesystem.student;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cmpe492.attendancesystem.AppGlobals;
import com.cmpe492.attendancesystem.R;
import com.cmpe492.attendancesystem.firebase.FireBase;
import com.cmpe492.attendancesystem.gettersetter.AttendanceItem;
import com.firebase.client.Firebase;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

/**
 * Here it scans the QR code verifies the student if it scanned the right Qr-code
 * physically.
 * an open source library 'androidhive:barcode-reader' is used for scanning Qr-code
 */
public class ScannerActivity extends AppCompatActivity  implements BarcodeReader.BarcodeReaderListener  {


    private BarcodeReader barcodeReader;
    private String selectedClass = "";

    // initializing barcode when activity is created

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.scanner_activity);
        selectedClass = getIntent().getStringExtra("class_name");
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        barcodeReader.setListener(this);
    }

    // barcode is scanned here and after that by using attendance item. Setting the values after reading them from
    // local shared preferences.
    @Override
    public void onScanned(Barcode barcode) {
        // play beep sound
        barcodeReader.playBeep();
        barcodeReader.pauseScanning();
        Log.i("TAG", "sparse " + barcode.displayValue);
        if (barcode.displayValue.equals(selectedClass)) {
            AttendanceItem attendanceItem = new AttendanceItem();
            attendanceItem.setName(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_NAME));
            attendanceItem.setRollNo(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ROLL_NO));
            attendanceItem.setVerifiedByScanner(true);
            Firebase firebase = FireBase.getInstance(getApplicationContext());
            firebase.child(AppGlobals.MARKED_ATTENDANCE).child(selectedClass).push().setValue(attendanceItem);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Alert dialog which i am showing when the attendance has been successfully marked.
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(ScannerActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(ScannerActivity.this);
                    }
                    builder.setTitle("Attendance Marked!")
                            .setMessage("Your attendance has been marked")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    dialog.dismiss();
                                    ScannerActivity.this.finish();
                                }
                            })
                            .setIcon(android.R.drawable.checkbox_on_background)
                            .show();
                }
            });

        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        Log.i("TAG", "sparse " + sparseArray);

    }

    @Override
    public void onScanError(String s) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}
