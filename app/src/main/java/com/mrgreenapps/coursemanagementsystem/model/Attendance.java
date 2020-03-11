package com.mrgreenapps.coursemanagementsystem.model;

public class Attendance {
    private String studentId;
    private String classId;
    private String courseId;
    private boolean present;

    public Attendance() {
    }

    public Attendance(String studentId, String classId, String courseId, boolean present) {
        this.studentId = studentId;
        this.classId = classId;
        this.courseId = courseId;
        this.present = present;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
