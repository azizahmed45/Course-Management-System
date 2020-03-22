package com.mrgreenapps.coursemanagementsystem.teacher.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrgreenapps.coursemanagementsystem.DB;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.CSRelation;
import com.mrgreenapps.coursemanagementsystem.model.Exam;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;
import com.mrgreenapps.coursemanagementsystem.teacher.adapters.MarksListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExamFragment extends Fragment {

    private String courseId;
    private String examId;
    private String userType;
    private String examType;

    @BindView(R.id.marks_list)
    RecyclerView marksListView;

    @BindView(R.id.submit_button)
    Button submitButton;

    MarksListAdapter marksListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_fragment, container, false);
        ButterKnife.bind(this, view);

        examType = getArguments().getString("exam_type");
        courseId = getArguments().getString("course_id");
        examId = getArguments().getString("exam_id");
        userType = getArguments().getString("user_type");

        if(userType.equals(UserInfo.TYPE_STUDENT)) submitButton.setVisibility(View.GONE);

        marksListAdapter = new MarksListAdapter(userType);
        marksListView.setAdapter(marksListAdapter);

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

                        DB.getExam(examType, examId)
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Exam exam = documentSnapshot.toObject(Exam.class);


                                        DB.getStudentList(csRelationList)
                                                .addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                                                    @Override
                                                    public void onSuccess(List<QuerySnapshot> querySnapshots) {
                                                        for (QuerySnapshot querySnapshot : querySnapshots) {
                                                            if (querySnapshot.getDocuments().size() > 0)
                                                                studentList.add(
                                                                        querySnapshot.getDocuments().get(0).toObject(UserInfo.class)
                                                                );
                                                        }


                                                        if (exam != null)
                                                            marksListAdapter.setTotalMark(exam.getTotalMark());

                                                        marksListAdapter.setList(
                                                                studentList,
                                                                exam != null ? exam.getMarkList() : new HashMap<>()

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
                HashMap<String, Float> marksListMap = marksListAdapter.getMarksList();

                DB.addExamMarks(examType, examId, marksListMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                NavHostFragment.findNavController(ExamFragment.this).navigateUp();

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
