package com.mrgreenapps.coursemanagementsystem.model;

public class UserInfo {

    public static final String TYPE_TEACHER = "Lecturer";
    public static final String TYPE_STUDENT = "Student";

    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";
    public static final String GENDER_OTHER = "other";

    private String uid;

    private String regId;

    private String name;

    private String type;

    private String gender;

    private String phoneNumber;

    private String profileImageUrl;

    public UserInfo() {
    }

    public UserInfo(String uid, String regId, String name, String type, String gender, String phoneNumber, String profileImageUrl) {
        this.uid = uid;
        this.regId = regId;
        this.name = name;
        this.type = type;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
    }

    public static String getTypeTeacher() {
        return TYPE_TEACHER;
    }

    public static String getTypeStudent() {
        return TYPE_STUDENT;
    }

    public static String getGenderMale() {
        return GENDER_MALE;
    }

    public static String getGenderFemale() {
        return GENDER_FEMALE;
    }

    public static String getGenderOther() {
        return GENDER_OTHER;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
