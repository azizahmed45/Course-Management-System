package com.mrgreenapps.coursemanagementsystem;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.mrgreenapps.coursemanagementsystem.model.CSRelation;
import com.mrgreenapps.coursemanagementsystem.model.Course;
import com.mrgreenapps.coursemanagementsystem.model.CourseClass;
import com.mrgreenapps.coursemanagementsystem.model.Notice;
import com.mrgreenapps.coursemanagementsystem.model.Tutorial;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DB {
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_COURSES = "courses";
    public static final String COLLECTION_CLASS = "classes";
    public static final String COLLECTION_NOTICE = "notices";
    public static final String COLLECTION_TUTORIAL = "tutorial";
    public static final String COLLECTION_COURSE_STUDENT_RELATION = "course_student_relation";

    public static Task<Void> addUser(UserInfo userInfo) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .set(userInfo);
    }


    public static Task<DocumentReference> joinCourse(String courseId, String studentId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSE_STUDENT_RELATION)
                .add(new CSRelation(courseId, studentId));
    }

    public static Task<DocumentSnapshot> getUser(String uid) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(uid)
                .get();
    }

    public static Task<DocumentSnapshot> getCourse(String courseId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSES)
                .document(courseId)
                .get();
    }

    public static Task<DocumentReference> addCourse(Course course) {
        course.setCreatedBy(FirebaseAuth.getInstance().getUid());
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSES)
                .add(course);
    }

    public static Task<DocumentReference> addNotice(Notice notice) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NOTICE)
                .add(notice);
    }

    public static Task<DocumentReference> addClass(CourseClass courseClass) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_CLASS)
                .add(courseClass);
    }

    public static Task<DocumentReference> addTutorial(Tutorial tutorial) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_TUTORIAL)
                .add(tutorial);
    }

    public static Task<Void> addTutorialMarks(String tutorialId, HashMap<String, Double> marksList) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_TUTORIAL)
                .document(tutorialId)
                .update("markList", marksList);
    }

    public static Task<Void> publishTutorialMarks(String tutorialId, boolean published) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_TUTORIAL)
                .document(tutorialId)
                .update("published", published);
    }

    public static Task<Void> updateProfile(String userId, String name, String gender, String phone, String id) {
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        batch.update(
                FirebaseFirestore.getInstance().collection(COLLECTION_USERS).document(userId),
                "name",
                name

        );

        batch.update(
                FirebaseFirestore.getInstance().collection(COLLECTION_USERS).document(userId),
                "gender",
                gender
        );

        batch.update(
                FirebaseFirestore.getInstance().collection(COLLECTION_USERS).document(userId),
                "phoneNumber",
                phone
        );

        batch.update(
                FirebaseFirestore.getInstance().collection(COLLECTION_USERS).document(userId),
                "regId",
                id
        );

        return batch.commit();
    }

    public static Task<Void> addAttendance(String classId, HashMap<String, Boolean> attendanceList) {

        int presentCount = 0, absentCount = 0;
        for(Boolean present: attendanceList.values()){
            if(present) presentCount++;
            else absentCount++;
        }

        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        batch.update(
                FirebaseFirestore.getInstance().collection(COLLECTION_CLASS).document(classId),
                "attendance",
                attendanceList
        );

        batch.update(
                FirebaseFirestore.getInstance().collection(COLLECTION_CLASS).document(classId),
                "present",
                presentCount
        );

        batch.update(
                FirebaseFirestore.getInstance().collection(COLLECTION_CLASS).document(classId),
                "absent",
                absentCount
        );

        return batch.commit();

    }

    public static Task<DocumentSnapshot> getClass(String classId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_CLASS)
                .document(classId)
                .get();
    }

    public static Task<DocumentSnapshot> getTutorial(String tutorialId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_TUTORIAL)
                .document(tutorialId)
                .get();
    }

    public static Query getCourseListQuery() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSES)
                .whereEqualTo("createdBy", FirebaseAuth.getInstance().getUid());
    }

    public static Query getCourseByInvitationQuery(String invitationCOde) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSES)
                .whereEqualTo("inviteCode", invitationCOde);
    }

    public static Query getClassListQuery(String courseId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_CLASS)
                .orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("courseId", courseId);
    }

    public static Query getNoticeListQuery(String courseId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NOTICE)
                .whereEqualTo("courseId", courseId)
                .orderBy("date", Query.Direction.DESCENDING);
    }

    public static Query getTutorialListQuery(String courseId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_TUTORIAL)
                .orderBy("name", Query.Direction.ASCENDING)
                .whereEqualTo("courseId", courseId);
    }

    public static Query getCSRelationCourseQuery(String courseId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSE_STUDENT_RELATION)
                .whereEqualTo("courseId", courseId);
    }

    public static Query getCSRelationStudentQuery(String studentId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSE_STUDENT_RELATION)
                .whereEqualTo("studentId", studentId);
    }

    public static Query getCSRelationQueryStudentCourse(String studentId, String courseId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSE_STUDENT_RELATION)
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("courseId", courseId);
    }


    public static Task<List<QuerySnapshot>> getStudentList(String courseId, List<CSRelation> csRelationList) {

        List<Task<QuerySnapshot>> queryList = new ArrayList<>();

        for (CSRelation csRelation : csRelationList) {
            queryList.add(
                    FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                            .whereEqualTo("uid", csRelation.getStudentId())
                            .get()
            );
        }

        return Tasks.whenAllSuccess(queryList);
    }

}
