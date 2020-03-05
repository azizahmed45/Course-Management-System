package com.mrgreenapps.coursemanagementsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.mrgreenapps.coursemanagementsystem.model.Course;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherDashboardFragment extends Fragment {

    @BindView(R.id.add_course_button)
    Button addCourseButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_dashboard, container, false);
        ButterKnife.bind(this, view);

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCourseCreateDialog();
            }
        });

        return view;
    }

    private void showCourseCreateDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_course, null, false);
        EditText courseNameField = view.findViewById(R.id.course_name_field);
        EditText courseCodeField = view.findViewById(R.id.course_code_field);
        EditText courseDetailsField = view.findViewById(R.id.course_details_field);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button createButton = view.findViewById(R.id.create_button);


        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (courseNameField.getText().toString().isEmpty()) {
                    courseNameField.setError("Required");
                    courseNameField.requestFocus();
                } else if (courseCodeField.getText().toString().isEmpty()) {
                    courseCodeField.setError("Required");
                    courseCodeField.requestFocus();
                } else {
                    Course course = new Course();
                    course.setName(courseNameField.getText().toString());
                    course.setCode(courseCodeField.getText().toString());
                    course.setDetails(courseDetailsField.getText().toString());

                    DB.addCourse(course)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getContext(), "Course created successfully.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Course creation Failed.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                }
            }
        });
    }
}
