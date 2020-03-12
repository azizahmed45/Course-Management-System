package com.mrgreenapps.coursemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrgreenapps.coursemanagementsystem.model.Course;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudentDashboardFragment extends Fragment {

    @BindView(R.id.add_course_button)
    Button addCourseButton;

    @BindView(R.id.course_list)
    RecyclerView courseListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_dashboard, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.add_course_button)
    public void addCourse(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.join_course, null, false);
        EditText invitationCodeForm = view.findViewById(R.id.invitation_code_form);
        Button joinButton = view.findViewById(R.id.join_course_button);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(invitationCodeForm.getText().toString().isEmpty()){
                    invitationCodeForm.setError("Required");
                    invitationCodeForm.requestFocus();
                    return;
                }

                DB.getCourseByInvitationQuery(invitationCodeForm.getText().toString())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.getDocuments().size() > 0){
                                    String courseId = queryDocumentSnapshots.getDocuments().get(0).getId();
                                    String studentId = FirebaseAuth.getInstance().getUid();

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
