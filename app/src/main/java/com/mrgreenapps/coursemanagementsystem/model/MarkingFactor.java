package com.mrgreenapps.coursemanagementsystem.model;

public class MarkingFactor {
    private String name;
    private float percentage;
    private String examId;

    public MarkingFactor() {
    }

    public MarkingFactor(String name) {
        this.name = name;
    }

    public MarkingFactor(String name, float percentage) {
        this.name = name;
        this.percentage = percentage;
    }

    public MarkingFactor(String name, float percentage, String examId) {
        this.name = name;
        this.percentage = percentage;
        this.examId = examId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
