package com.cmpe492.attendancesystem.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpe492.attendancesystem.AppGlobals;
import com.cmpe492.attendancesystem.Helpers;
import com.cmpe492.attendancesystem.R;
import com.cmpe492.attendancesystem.firebase.FireBase;
import com.cmpe492.attendancesystem.gettersetter.AttendanceItem;
import com.cmpe492.attendancesystem.gettersetter.ClassName;
import com.cmpe492.attendancesystem.teacher.ClassMenu;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * As its name shows this class is to make sure that user selects a class and move to mark an attendance.
 * Moreover i am also checking weather a student has marked the Attendace for selected class or not.
 */
public class MarkAttendance extends AppCompatActivity {

    private AppCompatSpinner selectClass;
    private Button markAttendace;
    private ArrayList<String> namesArray;
    private ArrayList<ClassName> classNames;
    private Adapter adapter;
    private String selectedClass = "";
    private boolean checking = false;
    private boolean alreadyMarkedAttendace = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.mark_attendance_activity);
        selectClass = findViewById(R.id.select_class);
        markAttendace = findViewById(R.id.mark_attendance);
        selectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alreadyMarkedAttendace = false;
                ClassName className = classNames.get(position);
                selectedClass = className.getName();
                if (!checking) checkMyAttendance(selectedClass);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        markAttendace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedClass.equals("")) {
                    Helpers.showSnackBar(v, "please select class for attendance");
                    return;
                }
                if (alreadyMarkedAttendace) {
                    Helpers.showSnackBar(v, "Your attendance is already marked");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                intent.putExtra("class_name", selectedClass);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayClasses();
        if (!selectedClass.equals("") && !checking) {
            checkMyAttendance(selectedClass);
        }
    }

    private void displayClasses() {
        namesArray = new ArrayList<>();
        classNames = new ArrayList<>();
        adapter = new Adapter(classNames);
        selectClass.setAdapter(adapter);
        Firebase firebase = FireBase.getInstance(getApplicationContext());
        // getting all the classes which have attendance active.
        firebase.child(AppGlobals.CLASSES).orderByChild("attendanceActive").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    ClassName className = dataSnapshot1.getValue(ClassName.class);
                    namesArray.add(className.getName());
                    classNames.add(className);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // this method checks that if user has already marked the attendace or not. if did then donot allow
    // mark again.
    private void checkMyAttendance(String className) {
        Log.i("TAG", "checking my attendance ");
        checking = true;
        Firebase firebase = FireBase.getInstance(getApplicationContext());
        firebase.child(AppGlobals.MARKED_ATTENDANCE).child(className).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    AttendanceItem className = dataSnapshot1.getValue(AttendanceItem.class);
                    if (className.getRollNo().equals(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ROLL_NO))) {
                        alreadyMarkedAttendace = true;
                    }

                }
                checking = false;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                checking = false;
                alreadyMarkedAttendace = false;

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

    // Adapter for spinner from where a student will select the class.
    private class Adapter extends BaseAdapter {

        private ArrayList<ClassName> classNames;
        private ViewHolder viewHolder;

        public Adapter(ArrayList<ClassName> classNames) {
            this.classNames = classNames;
        }

        @Override
        public int getCount() {
            return classNames.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.raw_class, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.name = convertView.findViewById(R.id.class_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ClassName className = classNames.get(position);
            viewHolder.name.setText(className.getName());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView name;

    }
}
