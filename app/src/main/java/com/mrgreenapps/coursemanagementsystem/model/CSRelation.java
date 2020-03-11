package com.mrgreenapps.coursemanagementsystem.model;

public class CSRelation {
    private String courseId;
    private String studentId;

    public CSRelation() {
    }

    public CSRelation(String courseId, String studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
