package com.mrgreenapps.coursemanagementsystem.student.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrgreenapps.coursemanagementsystem.DB;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.CSRelation;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;
import com.mrgreenapps.coursemanagementsystem.comon.CourseListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudentDashboardFragment extends Fragment {

    @BindView(R.id.add_course_button)
    Button addCourseButton;

    @BindView(R.id.course_list)
    RecyclerView courseListView;

    @BindView(R.id.logout_button)
    ImageButton logoutButton;

    private CourseListAdapter courseListAdapter;

    private List<DocumentSnapshot> courseSnapshotList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_dashboard, container, false);
        ButterKnife.bind(this, view);

        courseSnapshotList = new ArrayList<>();

        courseListAdapter = new CourseListAdapter(courseSnapshotList, UserInfo.TYPE_STUDENT);

        courseListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        courseListView.setAdapter(courseListAdapter);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                NavHostFragment.findNavController(StudentDashboardFragment.this)
                        .navigate(R.id.action_studentDashboardFragment_to_loginFragment);
            }
        });

        DB.getCSRelationStudentQuery(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                CSRelation csRelation = documentSnapshot.toObject(CSRelation.class);

                                courseSnapshotList = new ArrayList<>();

                                DB.getCourse(csRelation.getCourseId())
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                courseSnapshotList.add(documentSnapshot);
                                                courseListAdapter.setCourseSnapshotList(courseSnapshotList);
                                            }
                                        });
                            }
                        }
                    }
                });

        courseListAdapter.setOnItemClickListener(new CourseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("course_id", courseSnapshotList.get(position).getId());
                bundle.putString("user_type", UserInfo.TYPE_STUDENT);
                NavHostFragment.findNavController(StudentDashboardFragment.this)
                        .navigate(R.id.action_studentDashboardFragment_to_courseFragment, bundle);
            }
        });

        return view;
    }

    @OnClick(R.id.add_course_button)
    public void addCourse() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.join_course, null, false);
        EditText invitationCodeForm = view.findViewById(R.id.invitation_code_form);
        Button joinButton = view.findViewById(R.id.join_course_button);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invitationCodeForm.getText().toString().isEmpty()) {
                    invitationCodeForm.setError("Required");
                    invitationCodeForm.requestFocus();
                    return;
                }

                DB.getCourseByInvitationQuery(invitationCodeForm.getText().toString())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                                    String courseId = queryDocumentSnapshots.getDocuments().get(0).getId();
                                    String studentId = FirebaseAuth.getInstance().getUid();

                                    DB.getCSRelationQueryStudentCourse(studentId, courseId)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (queryDocumentSnapshots.getDocuments().size() <= 0) {
                                                        DB.joinCourse(courseId, studentId)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        Toast.makeText(getContext(), "Successfully joined to the course.", Toast.LENGTH_SHORT).show();
                                                                        alertDialog.cancel();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getContext(), "Course join failed.", Toast.LENGTH_SHORT).show();
                                                                        alertDialog.cancel();
                                                                    }
                                                                });
                                                    } else {
                                                        alertDialog.cancel();
                                                        Toast.makeText(getContext(), "You are already assigned to the course.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Something went wrong. ", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                } else {
                                    Toast.makeText(getContext(), "Invalid invitation code.", Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                        });

            }
        });

        alertDialog.show();
    }
}
