package com.mrgreenapps.coursemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrgreenapps.coursemanagementsystem.model.CSRelation;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceFragment extends Fragment {

    private String courseId;
    private String classId;

    @BindView(R.id.attendance_list)
    RecyclerView attendanceListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance_fragment, container, false);
        ButterKnife.bind(this, view);
        courseId = getArguments().getString("course_id");
        classId = getArguments().getString("class_id");


        DB.getCSRelationQuery(courseId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CSRelation> csRelationList = new ArrayList<>();
                        List<UserInfo> studentList = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            csRelationList.add(documentSnapshot.toObject(CSRelation.class));
                        }
                        Toast.makeText(getContext(), "" + queryDocumentSnapshots.size(), Toast.LENGTH_SHORT).show();

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

                                        attendanceListView.setAdapter(new AttendanceListAdapter(studentList));
                                    }
                                });


                    }
                });


        return view;
    }
}
