package com.mrgreenapps.coursemanagementsystem.model;

import java.util.HashMap;

public class Exam {

    public static final String TYPE_TUTORIAL = "tutorial";
    public static final String TYPE_EXAM = "exam";

    private String courseId;
    private float totalMark;
    private boolean published;
    private String name;
    private HashMap<String, Float> markList;

    public Exam() {
    }

    public Exam(String courseId, String name) {
        this.courseId = courseId;
        this.name = name;
    }

    public Exam(String courseId, float totalMark, String name) {
        this.courseId = courseId;
        this.totalMark = totalMark;
        this.name = name;
    }

    public Exam(String courseId, float totalMark, boolean published, String name, HashMap<String, Float> markList) {
        this.courseId = courseId;
        this.totalMark = totalMark;
        this.published = published;
        this.name = name;
        this.markList = markList;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public float getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(float totalMark) {
        this.totalMark = totalMark;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Float> getMarkList() {
        return markList;
    }

    public void setMarkList(HashMap<String, Float> markList) {
        this.markList = markList;
    }
}
