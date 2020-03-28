package com.mrgreenapps.coursemanagementsystem.comon.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrgreenapps.coursemanagementsystem.DB;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.TableAdapter;
import com.mrgreenapps.coursemanagementsystem.model.Course;
import com.mrgreenapps.coursemanagementsystem.model.Exam;
import com.mrgreenapps.coursemanagementsystem.model.Result;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultFragment extends Fragment {


    private String courseId;
    private String userType;

    public ResultFragment(String courseId, String userType) {
        this.courseId = courseId;
        this.userType = userType;
    }

    @BindView(R.id.not_generated_area)
    LinearLayout notGeneratedArea;

    @BindView(R.id.not_published_area)
    LinearLayout notPublishedArea;

    @BindView(R.id.result_area)
    LinearLayout resultArea;

    @BindView(R.id.tableView)
    TableView tableView;

    @BindView(R.id.generate_button)
    Button generateButton;

    TableAdapter tableAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_fragment, container, false);
        ButterKnife.bind(this, view);

        tableAdapter = new TableAdapter();
        tableView.setAdapter(tableAdapter);
        tableView.setTableViewListener(new TableListener());

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

                        if(course != null){


                            if(userType.equals(UserInfo.TYPE_TEACHER)){
                                if(course.isResultGenerated()){
                                    resultArea.setVisibility(View.VISIBLE);
                                    notGeneratedArea.setVisibility(View.GONE);
                                    notPublishedArea.setVisibility(View.GONE);
                                    showResultData();
                                } else {
                                    resultArea.setVisibility(View.GONE);
                                    notGeneratedArea.setVisibility(View.VISIBLE);
                                    notPublishedArea.setVisibility(View.GONE);
                                }
                            } else if(userType.equals(UserInfo.TYPE_STUDENT)){
                                if(course.isResultPublished()){
                                    resultArea.setVisibility(View.VISIBLE);
                                    notGeneratedArea.setVisibility(View.GONE);
                                    notPublishedArea.setVisibility(View.GONE);
                                    showResultData();
                                } else {
                                    resultArea.setVisibility(View.GONE);
                                    notGeneratedArea.setVisibility(View.GONE);
                                    notPublishedArea.setVisibility(View.VISIBLE);
                                }
                            }

                        }

                    }
                });


        return view;
    }

    private void showResultData() {
        List<String> columnHeaderList = new ArrayList<>();
        List<String> rowHeaderList = new ArrayList<>();
        List<String> examIdList = new ArrayList<>();
        List<List<String>> cellList = new ArrayList<>();


        columnHeaderList.add("Name");
        columnHeaderList.add("Reg. Id");
        columnHeaderList.add("Attendance");
        columnHeaderList.add("Tutorial");

        DB.getExamListQuery(courseId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                            Exam exam = dc.toObject(Exam.class);

                            if (exam != null) {
                                columnHeaderList.add(exam.getName());
                                examIdList.add(dc.getId());
                            }
                        }

                        DB.getResult(courseId)
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Result result = documentSnapshot.toObject(Result.class);

                                        DecimalFormat formatter = new DecimalFormat("#.##");

                                        if (result != null) {

                                            int counter = 0;
                                                for (String studentIid : result.getStudentsName().keySet()) {
                                                    counter++;
                                                    List<String> tempList = new ArrayList<>();

                                                    tempList.add(
                                                            result.getStudentsName().get(studentIid) == null ?
                                                                    "No name" : result.getStudentsName().get(studentIid)
                                                    );

                                                    tempList.add(
                                                            result.getStudentsRegId().get(studentIid) == null ?
                                                                    "No Id" : result.getStudentsRegId().get(studentIid)
                                                    );

                                                    tempList.add(
                                                            result.getAttendanceMark().get(studentIid) == null ?
                                                                    "0.00" : String.valueOf(formatter.format(result.getAttendanceMark().get(studentIid)))
                                                    );


                                                    tempList.add(
                                                            result.getTutorialMark().get(studentIid) == null ?
                                                                    "0.00" : String.valueOf(formatter.format(result.getTutorialMark().get(studentIid)))
                                                    );

                                                    for(String examId: examIdList){
                                                        tempList.add(
                                                                result.getExamMarkList().get(examId) == null ?
                                                                        "0.00" : String.valueOf(formatter.format(result.getTutorialMark().get(studentIid)))
                                                        );
                                                    }

                                                    rowHeaderList.add(String.valueOf(counter));
                                                    cellList.add(tempList);
                                                }




                                            tableAdapter.setAllItems(columnHeaderList, rowHeaderList, cellList);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void showResultData(){
//        DB.getResult(courseId)
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Result result = documentSnapshot.toObject(Result.class);
//
//                        if(result !=  null){
//
//
//                            DataTableHeader.Builder dataTableHeaderBuilder = new DataTableHeader.Builder();
//
//                            dataTableHeaderBuilder.item("Name", 1);
//                            dataTableHeaderBuilder.item("Id", 1);
//                            dataTableHeaderBuilder.item("Attendance", 1);
//                            dataTableHeaderBuilder.item("Tutorial", 1);
//
//                            DB.getExamListQuery(courseId)
//                                    .get()
//                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                            List<String> examIdList = new ArrayList<>();
//                                            for(DocumentSnapshot dc: queryDocumentSnapshots.getDocuments()){
//                                                Exam exam = dc.toObject(Exam.class);
//                                                if(exam != null){
//                                                    dataTableHeaderBuilder.item(exam.getName(), 1);
//                                                    examIdList.add(dc.getId());
//                                                }
//
//                                            }
//
//                                            ArrayList<DataTableRow> rows = new ArrayList<>();
//
//                                            DB.getResult(courseId)
//                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                                        @Override
//                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                                            Result result = documentSnapshot.toObject(Result.class);
//                                                            if(result != null){
//
//                                                                DB.getCSRelationCourseQuery(courseId)
//                                                                        .get()
//                                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                                            @Override
//                                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                                                                                for(DocumentSnapshot dc: queryDocumentSnapshots.getDocuments()){
//                                                                                    CSRelation csRelation = dc.toObject(CSRelation.class);
//                                                                                    if(csRelation != null){
//                                                                                        DataTableRow.Builder rowBuilder = new DataTableRow.Builder();
//
//                                                                                                rowBuilder
//                                                                                                .value(csRelation.getStudentId())
//                                                                                                .value(
//                                                                                                        result.getAttendanceMark().get(csRelation.getStudentId()) != null ?
//                                                                                                                result.getAttendanceMark().get(csRelation.getStudentId()).toString() :
//                                                                                                                "0"
//                                                                                                )
//                                                                                                .value(
//                                                                                                        result.getTutorialMark().get(csRelation.getStudentId()) != null ?
//                                                                                                                result.getTutorialMark().get(csRelation.getStudentId()).toString() :
//                                                                                                                "0"
//                                                                                                );
//
//                                                                                                for(String examId: examIdList){
//                                                                                                    rowBuilder.value(
//                                                                                                            result.getExamMarkList().get(examId).get(csRelation.getStudentId()) != null ?
//                                                                                                                    result.getExamMarkList().get(examId).get(csRelation.getStudentId()).toString() :
//                                                                                                                    "0"
//                                                                                                    );
//                                                                                                }
//
//                                                                                                rows.add(rowBuilder.build());
//
//
//
//                                                                                    }
//                                                                                }
//
//
//
//
//                                                                                dataTable.setHeader(dataTableHeaderBuilder.build());
//                                                                                dataTable.setRows(rows);
//
//                                                                                dataTable.inflate(getContext());
//
//                                                                            }
//                                                                        });
//
//
//
//
//
//
//                                                            }
//                                                        }
//                                                    });
//
//
//
//
//
//
//
//
//
//                                        }
//                                    });
//
//
//
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    class TableListener implements ITableViewListener{

        @Override
        public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

        }

        @Override
        public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

        }

        @Override
        public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {

        }

        @Override
        public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {

        }

        @Override
        public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

        }

        @Override
        public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

        }
    }

}
