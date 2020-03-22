package com.mrgreenapps.coursemanagementsystem;

import com.mrgreenapps.coursemanagementsystem.model.CourseClass;
import com.mrgreenapps.coursemanagementsystem.model.Exam;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalcUtils {
    public static String generateRandomString(int length) {
        String randomString = "";

        final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890".toCharArray();
        final SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            randomString = randomString + chars[random.nextInt(chars.length)];
        }

        return randomString;
    }

    public static HashMap<String, Float> calculateAttendance(List<CourseClass> courseClassList) {
        HashMap<String, Float> allAttendance = new HashMap<>();
        for (CourseClass courseClass : courseClassList) {
            if (courseClass != null) {

                HashMap<String, Boolean> attendanceMap = courseClass.getAttendance();
                int count = 0;

                if (attendanceMap != null) {
                    for (String studentId : attendanceMap.keySet()) {

                        if (attendanceMap.get(studentId)) count = 1;
                        else count = 0;

                        allAttendance.put(
                                studentId,
                                allAttendance.get(studentId) != null ? allAttendance.get(studentId) + count : count
                        );
                    }

                }
            }
        }

        return allAttendance;
    }

    public static HashMap<String, Float> calculateExamFactor(List<Exam> examList) {
        HashMap<String, Float> factorMarkList = new HashMap<>();
        for (Exam exam : examList) {
            if (exam != null) {
                HashMap<String, Float> marksMap = exam.getMarkList();

                if (marksMap != null) {
                    double factorMark = 0;
                    for (String studentId : marksMap.keySet()) {

                        double mark = marksMap.get(studentId) != null ? marksMap.get(studentId) : 0;
                        factorMark = mark / exam.getTotalMark();

                        factorMarkList.put(
                                studentId,
                                factorMarkList.get(studentId) != null ? factorMarkList.get(studentId) + (float) factorMark : (float) factorMark

                        );
                    }
                }

            }
        }

        return factorMarkList;
    }

    public static HashMap<String, Float> calculateExamAverage(HashMap<String, Float> markList, int numberOfExam) {
        HashMap<String, Float> averageExamMarks = new HashMap<>();
        for (String studentId : markList.keySet()) {
            float average = markList.get(studentId) / numberOfExam;
            averageExamMarks.put(studentId, average);
        }

        return averageExamMarks;
    }

    public static HashMap<String, Float> calculateFactoredMark(HashMap<String, Float> marksList, float totalMarks, float factor) {
        HashMap<String, Float> finalMarksList = new HashMap<>();
        for (String studentId : marksList.keySet()) {
            float finalMark = marksList.get(studentId) * factor;
            finalMarksList.put(studentId, finalMark);
        }

        return finalMarksList;
    }


    public static HashMap<String, Float> calculateMark(HashMap<String, Float> marksList, float totalMarks, float factor) {
        HashMap<String, Float> finalMarksList = new HashMap<>();
        for (String studentId : marksList.keySet()) {
            float finalMark = (marksList.get(studentId) / totalMarks) * factor;
            finalMarksList.put(studentId, finalMark);
        }

        return finalMarksList;
    }
}
