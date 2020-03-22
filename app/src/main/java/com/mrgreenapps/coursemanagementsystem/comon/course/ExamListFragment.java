package com.mrgreenapps.coursemanagementsystem.comon.course;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrgreenapps.coursemanagementsystem.DB;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.Exam;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;
import com.mrgreenapps.coursemanagementsystem.teacher.adapters.ExamListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExamListFragment extends Fragment {

    private String courseId;
    private String userType;

    @BindView(R.id.add_button)
    Button addExamButton;

    @BindView(R.id.list)
    RecyclerView examListView;

    private ExamListAdapter examListAdapter;

    private List<DocumentSnapshot> examSnapshotList;

    public ExamListFragment(String courseId, String userType) {
        this.courseId = courseId;
        this.userType = userType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.global_list_for_course_fragmnet, container, false);
        ButterKnife.bind(this, view);

        addExamButton.setText("Add Exam");

        if (userType.equals(UserInfo.TYPE_STUDENT)) addExamButton.setVisibility(View.GONE);

        addExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExam();
            }
        });

        examSnapshotList = new ArrayList<>();

        examListAdapter = new ExamListAdapter(examSnapshotList, userType, Exam.TYPE_EXAM);

        examListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        examListView.setAdapter(examListAdapter);

        examListAdapter.setOnItemClickListener(new ExamListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("exam_type", Exam.TYPE_EXAM);
                bundle.putString("exam_id", examSnapshotList.get(position).getId());
                bundle.putString("course_id", courseId);
                bundle.putString("user_type", userType);
                NavHostFragment.findNavController(ExamListFragment.this)
                        .navigate(R.id.action_courseFragment_to_tutorialFragment, bundle);
            }
        });


        DB.getExamListQuery(courseId)
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            boolean addedORRemoved = false;
                            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {

                                if (documentChange.getType().equals(DocumentChange.Type.ADDED)
                                        || documentChange.getType().equals(DocumentChange.Type.REMOVED)
                                ) {
                                    addedORRemoved = true;
                                    break;
                                }
                            }

                            if (addedORRemoved) {
                                if (userType.equals(UserInfo.TYPE_TEACHER)) {
                                    examSnapshotList = queryDocumentSnapshots.getDocuments();
                                    examListAdapter.setExamSnapshotList(examSnapshotList);
                                } else if (userType.equals(UserInfo.TYPE_STUDENT)) {
                                    List<DocumentSnapshot> publishedDocumentSnapshotList = new ArrayList<>();

                                    for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                        Exam tutorial = ds.toObject(Exam.class);

                                        if (tutorial != null && tutorial.isPublished())
                                            publishedDocumentSnapshotList.add(ds);
                                    }

                                    examSnapshotList = publishedDocumentSnapshotList;
                                    examListAdapter.setExamSnapshotList(examSnapshotList);
                                }


                            }
                        }

                    }
                });


        return view;
    }

    public void addExam() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_exam, null, false);
        TextView tutorialOrExam = view.findViewById(R.id.tutorial_or_exam);
        tutorialOrExam.setText("Create Exam");

        EditText nameForm = view.findViewById(R.id.name);
        EditText totalMarksForm = view.findViewById(R.id.total_marks);
        Button addButton = view.findViewById(R.id.add_button);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameForm.getText().toString().isEmpty()) {
                    nameForm.setError("Required");
                    nameForm.requestFocus();
                    return;
                }
                if (totalMarksForm.getText().toString().isEmpty()) {
                    totalMarksForm.setError("Required");
                    totalMarksForm.requestFocus();
                    return;
                }

                DB.addExam(new Exam(
                        courseId,
                        Float.parseFloat(totalMarksForm.getText().toString()),
                        nameForm.getText().toString()
                ))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Bundle bundle = new Bundle();
                                bundle.putString("exam_type", Exam.TYPE_EXAM);
                                bundle.putString("exam_id", documentReference.getId());
                                bundle.putString("course_id", courseId);
                                bundle.putString("user_type", userType);
                                NavHostFragment.findNavController(ExamListFragment.this)
                                        .navigate(R.id.action_courseFragment_to_tutorialFragment, bundle);
                                dialog.cancel();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed.", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

            }
        });

        dialog.show();

    }
}
