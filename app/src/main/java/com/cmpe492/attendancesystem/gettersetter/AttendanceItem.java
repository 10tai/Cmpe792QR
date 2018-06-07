package com.cmpe492.attendancesystem.gettersetter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

//getter and setters for Attendance Item. Attendance is recorded on firebase console using this.
public class AttendanceItem {

    private String studentRollNo;
    private String studentName;
    private boolean verifiedByScanner = false;

    public boolean isVerifiedByScanner() {
        return verifiedByScanner;
    }

    public void setVerifiedByScanner(boolean verifiedByScanner) {
        this.verifiedByScanner = verifiedByScanner;
    }

    public String getRollNo() {
        return studentRollNo;
    }

    public void setRollNo(String studentRollNo) {
        this.studentRollNo = studentRollNo;
    }

    public String getName() {
        return studentName;
    }

    public void setName(String studentName) {
        this.studentName = studentName;
    }
}
