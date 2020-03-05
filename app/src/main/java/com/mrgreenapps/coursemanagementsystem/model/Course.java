package com.mrgreenapps.coursemanagementsystem.model;

import java.util.Date;

public class Course {

    private String name;

    private String code;

    private String createdBy;

    private String details;

    private Date startDate;

    private Date endDate;

    public Course() {
    }

    public Course(String name, String code, String createdBy, String details, Date startDate, Date endDate) {
        this.name = name;
        this.code = code;
        this.createdBy = createdBy;
        this.details = details;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
