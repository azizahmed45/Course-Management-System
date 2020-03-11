package com.mrgreenapps.coursemanagementsystem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.mrgreenapps.coursemanagementsystem.model.Attendance;
import com.mrgreenapps.coursemanagementsystem.model.CSRelation;
import com.mrgreenapps.coursemanagementsystem.model.Course;
import com.mrgreenapps.coursemanagementsystem.model.CourseClass;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class DB {
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_COURSES = "courses";
    public static final String COLLECTION_CLASS = "classes";
    public static final String COLLECTION_ATTENDANCE = "attendances";
    public static final String COLLECTION_COURSE_STUDENT_RELATION = "course_student_relation";

    public static Task<DocumentReference> addUser(UserInfo userInfo){
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .add(userInfo);
    }

    public static Task<DocumentReference> addCourse(Course course){
        course.setCreatedBy(FirebaseAuth.getInstance().getUid());
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSES)
                .add(course);
    }

    public static Task<DocumentReference> addClass(CourseClass courseClass){
        return FirebaseFirestore.getInstance().collection(COLLECTION_CLASS)
                .add(courseClass);
    }

    public static Task<Void> addAttendance(List<Attendance> attendanceList){
        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        for(Attendance attendance: attendanceList){
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection(COLLECTION_ATTENDANCE)
                    .document();
            batch.set(documentReference, attendance);
        }

        return batch.commit();
    }

    public static Query getCourseListQuery(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSES)
                .whereEqualTo("createdBy", FirebaseAuth.getInstance().getUid());
    }

    public static Query getClassListQuery(String courseId){
        return FirebaseFirestore.getInstance().collection(COLLECTION_CLASS)
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("courseId", courseId);
    }

    public static Query getCSRelationQuery(String courseId){
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSE_STUDENT_RELATION)
                .whereEqualTo("courseId", courseId);
    }

    public static Task<List<QuerySnapshot>> getStudentList(String courseId, List<CSRelation> csRelationList){

        List<Task<QuerySnapshot>> queryList = new ArrayList<>();

        for(CSRelation csRelation: csRelationList){
            queryList.add(
                    FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                    .whereEqualTo("uid", csRelation.getStudentId())
                    .get()
            );
        }

        return Tasks.whenAllSuccess(queryList);
    }

}
