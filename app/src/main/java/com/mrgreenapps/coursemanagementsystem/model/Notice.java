package com.mrgreenapps.coursemanagementsystem.model;

import java.util.Date;

public class Notice {
    String title;
    String details;
    Date date;
    String courseId;

    public Notice() {
    }

    public Notice(String title, String details, Date date, String courseId) {
        this.title = title;
        this.details = details;
        this.date = date;
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
