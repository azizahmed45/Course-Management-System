package com.mrgreenapps.coursemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrgreenapps.coursemanagementsystem.model.CSRelation;
import com.mrgreenapps.coursemanagementsystem.model.CourseClass;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceFragment extends Fragment {

    private String courseId;
    private String classId;

    @BindView(R.id.attendance_list)
    RecyclerView attendanceListView;

    @BindView(R.id.submit_button)
    Button submitButton;

    AttendanceListAdapter attendanceListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance_fragment, container, false);
        ButterKnife.bind(this, view);
        courseId = getArguments().getString("course_id");
        classId = getArguments().getString("class_id");

        attendanceListAdapter = new AttendanceListAdapter();
        attendanceListView.setAdapter(attendanceListAdapter);

        DB.getCSRelationCourseQuery(courseId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CSRelation> csRelationList = new ArrayList<>();
                        List<UserInfo> studentList = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            csRelationList.add(documentSnapshot.toObject(CSRelation.class));
                        }
//                        Toast.makeText(getContext(), "" + queryDocumentSnapshots.size(), Toast.LENGTH_SHORT).show();

                        DB.getClass(classId)
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        CourseClass courseClass = documentSnapshot.toObject(CourseClass.class);


                                        DB.getStudentList(courseId, csRelationList)
                                                .addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                                                    @Override
                                                    public void onSuccess(List<QuerySnapshot> querySnapshots) {
                                                        for (QuerySnapshot querySnapshot : querySnapshots) {
                                                            if (querySnapshot.getDocuments().size() > 0)
                                                                studentList.add(
                                                                        querySnapshot.getDocuments().get(0).toObject(UserInfo.class)
                                                                );
                                                        }



                                                        attendanceListAdapter.setList(
                                                                studentList,
                                                                courseClass != null ? courseClass.getAttendance() : new HashMap<>()

                                                        );


                                                    }
                                                });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Something went wrong." + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Boolean> attendanceListMap = attendanceListAdapter.getAttendanceList();

                DB.addAttendance(classId, attendanceListMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Successfully updated.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Something went wrong" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });


        return view;
    }
}
