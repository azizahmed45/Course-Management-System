package com.mrgreenapps.coursemanagementsystem.model;

import java.util.HashMap;

public class Tutorial {
    private String courseId;
    private double totalMark;
    private String name;
    private HashMap<String, Double> markList;

    public Tutorial() {
    }

    public Tutorial(String courseId, String name) {
        this.courseId = courseId;
        this.name = name;
    }

    public Tutorial(String courseId, double totalMark, String name, HashMap<String, Double> markList) {
        this.courseId = courseId;
        this.totalMark = totalMark;
        this.name = name;
        this.markList = markList;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public double getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(double totalMark) {
        this.totalMark = totalMark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Double> getMarkList() {
        return markList;
    }

    public void setMarkList(HashMap<String, Double> markList) {
        this.markList = markList;
    }
}
