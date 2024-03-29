package com.mrgreenapps.coursemanagementsystem.comon.course;

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
import com.mrgreenapps.coursemanagementsystem.model.CourseClass;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;
import com.mrgreenapps.coursemanagementsystem.teacher.adapters.ClassListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassListFragment extends Fragment {

    @BindView(R.id.add_button)
    Button addClassButton;

    @BindView(R.id.list)
    RecyclerView classListView;

    private String courseId;

    private String userType;

    private ClassListAdapter classListAdapter;

    private List<DocumentSnapshot> classSnapshotList;

    public ClassListFragment(String courseId, String userType){
        this.courseId = courseId;
        this.userType = userType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.global_list_for_course_fragmnet, container, false);
        ButterKnife.bind(this, view);

        addClassButton.setText("Add Class");
        if(userType.equals(UserInfo.TYPE_STUDENT)) addClassButton.setVisibility(View.GONE);

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClass();
            }
        });

        classSnapshotList = new ArrayList<>();

        classListAdapter = new ClassListAdapter(classSnapshotList);

        classListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        classListView.setAdapter(classListAdapter);

        classListAdapter.setOnItemClickListener(new ClassListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("user_type", userType);
                bundle.putString("class_id", classSnapshotList.get(position).getId());
                bundle.putString("course_id", courseId);
                NavHostFragment.findNavController(ClassListFragment.this)
                        .navigate(R.id.action_courseFragment_to_attendanceFragment2, bundle);
            }
        });


        DB.getClassListQuery(courseId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null){
                            classSnapshotList = queryDocumentSnapshots.getDocuments();
                            classListAdapter.setClassSnapshotList(classSnapshotList);
                        }

                    }
                });


        return view;
    }

    public void addClass(){
        DB.addClass(new CourseClass(courseId, new Date()))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Bundle bundle = new Bundle();
                        bundle.putString("user_type", userType);
                        bundle.putString("class_id", documentReference.getId());
                        bundle.putString("course_id", courseId);
                        NavHostFragment.findNavController(ClassListFragment.this)
                                .navigate(R.id.action_courseFragment_to_attendanceFragment2, bundle);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
