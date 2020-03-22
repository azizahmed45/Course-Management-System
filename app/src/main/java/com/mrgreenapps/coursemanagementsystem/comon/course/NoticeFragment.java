package com.mrgreenapps.coursemanagementsystem.comon.course;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrgreenapps.coursemanagementsystem.DB;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.Notice;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeFragment extends Fragment {

    @BindView(R.id.add_button)
    Button addNoticeButton;

    @BindView(R.id.list)
    RecyclerView noticeListView;

    private String courseId;
    private String userType;

    public NoticeFragment(String courseId, String userType) {
        this.courseId = courseId;
        this.userType = userType;
    }


    private NoticeListAdapter noticeListAdapter;

    private List<DocumentSnapshot> noticeSnapshotList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.global_list_for_course_fragmnet, container, false);
        ButterKnife.bind(this, view);

        addNoticeButton.setText("Add Notice");


        if (userType.equals(UserInfo.TYPE_STUDENT)) addNoticeButton.setVisibility(View.GONE);


        noticeSnapshotList = new ArrayList<>();

        noticeListAdapter = new NoticeListAdapter(noticeSnapshotList);

        noticeListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        noticeListView.setAdapter(noticeListAdapter);


        DB.getNoticeListQuery(courseId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            noticeSnapshotList = queryDocumentSnapshots.getDocuments();
                            noticeListAdapter.setNoticeSnapshotList(noticeSnapshotList);
                        }

                    }
                });


        return view;
    }

    @OnClick(R.id.add_button)
    public void addNotice() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_notice, null, false);
        EditText titleField = view.findViewById(R.id.title_field);
        EditText detailsField = view.findViewById(R.id.details_field);

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
                if (titleField.getText().toString().isEmpty()) {
                    titleField.setError("Required");
                    titleField.requestFocus();
                } else if (detailsField.getText().toString().isEmpty()) {
                    detailsField.setError("Required");
                    detailsField.requestFocus();
                } else {
                    Notice notice = new Notice(
                            titleField.getText().toString(),
                            detailsField.getText().toString(),
                            new Date(),
                            courseId
                    );

                    DB.addNotice(notice)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getContext(), "Notice created successfully.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Notice creation Failed.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                }
            }
        });

        dialog.show();
    }
}
