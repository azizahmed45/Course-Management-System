package com.mrgreenapps.coursemanagementsystem;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrgreenapps.coursemanagementsystem.model.Course;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

public class DB {
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_COURSES = "courses";

    public static Task<DocumentReference> addUser(UserInfo userInfo){
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .add(userInfo);
    }

    public static Task<DocumentReference> addCourse(Course course){
        course.setCreatedBy(FirebaseAuth.getInstance().getUid());
        return FirebaseFirestore.getInstance().collection(COLLECTION_COURSES)
                .add(course);
    }
}
