package com.mrgreenapps.coursemanagementsystem.model;

import java.util.HashMap;

public class Result {
    private String courseId;
    private HashMap<String, String> studentsName;
    private HashMap<String, String> studentsRegId;
    private HashMap<String, Float> attendanceMark;
    private HashMap<String, Float> tutorialMark;
    private HashMap<String, HashMap<String, Float>> examMarkList;
    private HashMap<String, String> gradeList;

    public Result() {
    }

    public Result(String courseId, HashMap<String, String> studentsName, HashMap<String, String> studentsRegId, HashMap<String, Float> attendanceMark, HashMap<String, Float> tutorialMark, HashMap<String, HashMap<String, Float>> examMarkList) {
        this.courseId = courseId;
        this.studentsName = studentsName;
        this.studentsRegId = studentsRegId;
        this.attendanceMark = attendanceMark;
        this.tutorialMark = tutorialMark;
        this.examMarkList = examMarkList;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public HashMap<String, String> getStudentsName() {
        return studentsName;
    }

    public void setStudentsName(HashMap<String, String> studentsName) {
        this.studentsName = studentsName;
    }

    public HashMap<String, String> getStudentsRegId() {
        return studentsRegId;
    }

    public void setStudentsRegId(HashMap<String, String> studentsRegId) {
        this.studentsRegId = studentsRegId;
    }

    public HashMap<String, Float> getAttendanceMark() {
        return attendanceMark;
    }

    public void setAttendanceMark(HashMap<String, Float> attendanceMark) {
        this.attendanceMark = attendanceMark;
    }

    public HashMap<String, Float> getTutorialMark() {
        return tutorialMark;
    }

    public void setTutorialMark(HashMap<String, Float> tutorialMark) {
        this.tutorialMark = tutorialMark;
    }

    public HashMap<String, HashMap<String, Float>> getExamMarkList() {
        return examMarkList;
    }

    public void setExamMarkList(HashMap<String, HashMap<String, Float>> examMarkList) {
        this.examMarkList = examMarkList;
    }

    public HashMap<String, String> getGradeList() {
        return gradeList;
    }

    public void setGradeList(HashMap<String, String> gradeList) {
        this.gradeList = gradeList;
    }
}
