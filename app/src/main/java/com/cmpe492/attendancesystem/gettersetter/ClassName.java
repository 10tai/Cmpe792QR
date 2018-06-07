package com.cmpe492.attendancesystem.gettersetter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

// getter and setter for class name, Object of this class is sent to firbase server to record the class period
public class ClassName {

    private String name;
    private boolean attendanceActive = false;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAttendanceActive() {
        return attendanceActive;
    }

    public void setAttendanceActive(boolean attendanceActive) {
        this.attendanceActive = attendanceActive;
    }
}
