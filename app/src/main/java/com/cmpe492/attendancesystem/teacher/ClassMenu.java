package com.cmpe492.attendancesystem.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpe492.attendancesystem.AppGlobals;
import com.cmpe492.attendancesystem.R;
import com.cmpe492.attendancesystem.firebase.FireBase;
import com.cmpe492.attendancesystem.gettersetter.ClassName;
import com.cmpe492.attendancesystem.student.MarkAttendance;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/** this is the class teacher will see his all classes created by him before with time.
 *
 */
public class ClassMenu  extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private FloatingActionButton addClass;
    private ArrayList<String> namesArray;
    private ArrayList<ClassName> classNames;
    private Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Main.getInstance() != null) {
            Main.getInstance().finish();
        }
        setContentView(R.layout.class_activity);
        listView = findViewById(R.id.classes_list);
        addClass = findViewById(R.id.add_class);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayClasses();
            }
        });
        addClass.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayClasses();
    }


    // Getting all the classes from firebase
    private void displayClasses() {
        Firebase firebase = FireBase.getInstance(getApplicationContext());
        firebase.child(AppGlobals.CLASSES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                namesArray = new ArrayList<>();
                classNames = new ArrayList<>();
                adapter = new ClassMenu.Adapter(getApplicationContext(), classNames);
                listView.setAdapter(adapter);
                // looping the classes from server
                swipeRefreshLayout.setRefreshing(false);
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    ClassName className = dataSnapshot1.getValue(ClassName.class);
                    Log.i("TAG", " name " + className.getName());
                    namesArray.add(className.getName());
                    classNames.add(className);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        firebase.child(AppGlobals.CLASSES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                swipeRefreshLayout.setRefreshing(false);

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
        switch (v.getId()){
            case R.id.add_class:
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
                break;
        }
    }


    private class Adapter extends ArrayAdapter<String> {

        private ArrayList<ClassName> classNames;
        private ViewHolder viewHolder;

        public Adapter(@NonNull Context context, ArrayList<ClassName> classNames) {
            super(context, R.layout.raw_select_class);
            this.classNames = classNames;
        }

        @Override
        public int getCount() {
            return classNames.size();
        }

        @Override
        public View getView(final int position, android.view.View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.raw_select_class, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.name = convertView.findViewById(R.id.class_name);
                viewHolder.checkBox = convertView.findViewById(R.id.active);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final ClassName className = classNames.get(position);
            viewHolder.name.setText(className.getName());
            if (className.isAttendanceActive()) {
                viewHolder.checkBox.setChecked(true);
            } else {
                viewHolder.checkBox.setChecked(false);
            }
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && !className.isAttendanceActive()) {
                        className.setAttendanceActive(true);
                        Firebase firebase = FireBase.getInstance(getApplicationContext());
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("attendanceActive", true);
                        firebase.child(AppGlobals.CLASSES).child(className.getName()).updateChildren(result);

                    } else if (className.isAttendanceActive()) {
                        className.setAttendanceActive(false);
                        Firebase firebase = FireBase.getInstance(getApplicationContext());
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("attendanceActive", false);
                        firebase.child(AppGlobals.CLASSES).child(className.getName()).updateChildren(result);

                    }
                    notifyDataSetChanged();
                }
            });
            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ClassName className = classNames.get(position);
                    Intent intent = new Intent(getApplicationContext(), AttendanceDetails.class);
                    intent.putExtra("class_name", className.getName());
                    startActivity(intent);

                }
            });
            return convertView;
        }
    }

    private class ViewHolder {
        TextView name;
        CheckBox checkBox;

    }
}
