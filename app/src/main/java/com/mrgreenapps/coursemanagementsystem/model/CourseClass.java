package com.mrgreenapps.coursemanagementsystem.model;

import java.util.Date;
import java.util.HashMap;

public class CourseClass {
    private String courseId;
    private Date date;
    private int present;
    private int absent;
    private HashMap<String, Boolean> attendance;

    public CourseClass() {
    }

    public CourseClass(String courseId, Date date) {
        this.courseId = courseId;
        this.date = date;
    }

    public CourseClass(String courseId, Date date, int present, int absent, HashMap<String, Boolean> attendance) {
        this.courseId = courseId;
        this.date = date;
        this.present = present;
        this.absent = absent;
        this.attendance = attendance;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public HashMap<String, Boolean> getAttendance() {
        return attendance;
    }

    public void setAttendance(HashMap<String, Boolean> attendance) {
        this.attendance = attendance;
    }
}
