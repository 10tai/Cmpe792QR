package com.cmpe492.attendancesystem.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpe492.attendancesystem.AppGlobals;
import com.cmpe492.attendancesystem.R;
import com.cmpe492.attendancesystem.firebase.FireBase;
import com.cmpe492.attendancesystem.gettersetter.AttendanceItem;
import com.cmpe492.attendancesystem.gettersetter.ClassName;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class shows the marked students details according to selected class.
 */
public class AttendanceDetails extends AppCompatActivity {

    private String currentClass;
    private ArrayList<String> namesArray;
    private ArrayList<AttendanceItem> classNames;
    private ListView listView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.attendance_details);
        // getting the selected class from intent
        currentClass = getIntent().getStringExtra("class_name");
        getSupportActionBar().setTitle(currentClass);
        listView = findViewById(R.id.students);
        displayClasses();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.qr_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.view_qr:
                intent = new Intent(this, QRActivity.class);
                intent.putExtra("currentClass", currentClass);
                startActivity(intent);
                return true;
            default:return false;
        }
    }

    private void displayClasses() {
        classNames = new ArrayList<>();
        adapter = new Adapter(getApplicationContext(), classNames);
        listView.setAdapter(adapter);
        Firebase firebase = FireBase.getInstance(getApplicationContext());
        firebase.child(AppGlobals.MARKED_ATTENDANCE).child(currentClass).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("TAG", " Main " + dataSnapshot);
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    AttendanceItem className = dataSnapshot1.getValue(AttendanceItem.class);
                    Log.i("TAG", " name " + className.getName());
                    classNames.add(className);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Adapter to show single detail of student who marks attendance in selected class in previous screen.
    private class Adapter extends ArrayAdapter<String> {

        private ArrayList<AttendanceItem> classNames;
        private ViewHolder viewHolder;

        public Adapter(@NonNull Context context, ArrayList<AttendanceItem> classNames) {
            super(context, R.layout.raw_select_class);
            this.classNames = classNames;
        }

        @Override
        public int getCount() {
            return classNames.size();
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.raw_attendance_detail, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.name = convertView.findViewById(R.id.name);
                viewHolder.rollNo = convertView.findViewById(R.id.roll_no);
                viewHolder.checkBox = convertView.findViewById(R.id.active);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final AttendanceItem attendanceItem = classNames.get(position);
            viewHolder.name.setText(attendanceItem.getName());
            viewHolder.rollNo.setText(attendanceItem.getRollNo());
            if (attendanceItem.isVerifiedByScanner()) {
                viewHolder.checkBox.setImageResource(android.R.drawable.checkbox_on_background);
            } else {
                viewHolder.checkBox.setImageResource(android.R.drawable.checkbox_off_background);
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView name;
        TextView rollNo;
        ImageView checkBox;

    }
}
