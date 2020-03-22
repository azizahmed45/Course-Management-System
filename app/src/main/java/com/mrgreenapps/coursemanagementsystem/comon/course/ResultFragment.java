package com.mrgreenapps.coursemanagementsystem.comon.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrgreenapps.coursemanagementsystem.DB;
import com.mrgreenapps.coursemanagementsystem.GenerateResultFragment;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.CSRelation;
import com.mrgreenapps.coursemanagementsystem.model.Course;
import com.mrgreenapps.coursemanagementsystem.model.Exam;
import com.mrgreenapps.coursemanagementsystem.model.Result;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class ResultFragment extends Fragment {


    private String courseId;
    private String userType;

    public ResultFragment(String courseId, String userType){
        this.courseId = courseId;
        this.userType = userType;
    }

    @BindView(R.id.not_generated_area)
    LinearLayout notGeneratedArea;

    @BindView(R.id.data_table)
    DataTable dataTable;

    @BindView(R.id.generate_button)
    Button generateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_fragment, container, false);
        ButterKnife.bind(this, view);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("course_id", courseId);

                Navigation.findNavController(view)
                        .navigate(R.id.action_courseFragment_to_generateResultFragment2, bundle);

            }
        });

        DB.getCourse(courseId)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Course course = documentSnapshot.toObject(Course.class);


                        if(course != null && course.isResultGenerated()){
                            notGeneratedArea.setVisibility(View.GONE);
                            showResultData();
                        } else {
                            notGeneratedArea.setVisibility(View.VISIBLE);
                        }
                    }
                });

//        Bundle bundle = new Bundle();
//        bundle.putString("course_id", courseId);
//        GenerateResultFragment generateResultFragment = new GenerateResultFragment();
//        generateResultFragment.setArguments(bundle);



        return view;
    }

    private void showResultData(){
        DB.getResult(courseId)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Result result = documentSnapshot.toObject(Result.class);

                        if(result !=  null){


                            DataTableHeader.Builder dataTableHeaderBuilder = new DataTableHeader.Builder();

                            dataTableHeaderBuilder.item("Name", 1);
                            dataTableHeaderBuilder.item("Id", 1);
                            dataTableHeaderBuilder.item("Attendance", 1);
                            dataTableHeaderBuilder.item("Tutorial", 1);

                            DB.getExamListQuery(courseId)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<String> examIdList = new ArrayList<>();
                                            for(DocumentSnapshot dc: queryDocumentSnapshots.getDocuments()){
                                                Exam exam = dc.toObject(Exam.class);
                                                if(exam != null){
                                                    dataTableHeaderBuilder.item(exam.getName(), 1);
                                                    examIdList.add(dc.getId());
                                                }

                                            }

                                            ArrayList<DataTableRow> rows = new ArrayList<>();

                                            DB.getResult(courseId)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            Result result = documentSnapshot.toObject(Result.class);
                                                            if(result != null){

                                                                DB.getCSRelationCourseQuery(courseId)
                                                                        .get()
                                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                                for(DocumentSnapshot dc: queryDocumentSnapshots.getDocuments()){
                                                                                    CSRelation csRelation = dc.toObject(CSRelation.class);
                                                                                    if(csRelation != null){
                                                                                        DataTableRow.Builder rowBuilder = new DataTableRow.Builder();

                                                                                                rowBuilder
                                                                                                .value(csRelation.getStudentId())
                                                                                                .value(
                                                                                                        result.getAttendanceMark().get(csRelation.getStudentId()) != null ?
                                                                                                                result.getAttendanceMark().get(csRelation.getStudentId()).toString() :
                                                                                                                "0"
                                                                                                )
                                                                                                .value(
                                                                                                        result.getTutorialMark().get(csRelation.getStudentId()) != null ?
                                                                                                                result.getTutorialMark().get(csRelation.getStudentId()).toString() :
                                                                                                                "0"
                                                                                                );

                                                                                                for(String examId: examIdList){
                                                                                                    rowBuilder.value(
                                                                                                            result.getExamMarkList().get(examId).get(csRelation.getStudentId()) != null ?
                                                                                                                    result.getExamMarkList().get(examId).get(csRelation.getStudentId()).toString() :
                                                                                                                    "0"
                                                                                                    );
                                                                                                }

                                                                                                rows.add(rowBuilder.build());



                                                                                    }
                                                                                }




                                                                                dataTable.setHeader(dataTableHeaderBuilder.build());
                                                                                dataTable.setRows(rows);

                                                                                dataTable.inflate(getContext());

                                                                            }
                                                                        });






                                                            }
                                                        }
                                                    });









                                        }
                                    });



                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
