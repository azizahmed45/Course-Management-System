package com.mrgreenapps.coursemanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Internal;
import com.mrgreenapps.coursemanagementsystem.model.CSRelation;
import com.mrgreenapps.coursemanagementsystem.model.CourseClass;
import com.mrgreenapps.coursemanagementsystem.model.Exam;
import com.mrgreenapps.coursemanagementsystem.model.MarkingFactor;
import com.mrgreenapps.coursemanagementsystem.model.Result;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenerateResultFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView markingFactorListView;

    @BindView(R.id.generate_button)
    Button generateButton;

    @BindView(R.id.grade_list)
    RecyclerView gradeListView;

    @BindView(R.id.grade_option)
    Spinner gradeOptionSpinner;

    @BindView(R.id.add_grade_area)
    LinearLayout addGradeArea;

    @BindView(R.id.add_button)
    Button addGradeButton;

    @BindView(R.id.grade_letter_field)
    EditText gradeLetterField;

    @BindView(R.id.lower_bound_field)
    EditText lowerBoundField;


    List<MarkingFactor> markingFactorList;

    MarkingFactorAdapter markingFactorAdapter;

    GradeOptionListAdapter gradeOptionListAdapter;

    String courseId;

    int taskCounter = 0;
    HashMap<String, Float> mAttendanceMark = new HashMap<>();
    HashMap<String, Float> mTutorialMark = new HashMap<>();
    HashMap<String, HashMap<String, Float>> mExamMarkList = new HashMap<>();
    HashMap<String, String> studentNameMap = new HashMap<>();
    HashMap<String, String> studentRegIdMap = new HashMap<>();


    private static final String TAG = "GenerateResultFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generate_result_fragment, container, false);
        ButterKnife.bind(this, view);

        courseId = getArguments().getString("course_id");

        gradeOptionListAdapter = new GradeOptionListAdapter(getContext());
        gradeListView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        gradeListView.setAdapter(gradeOptionListAdapter);

        markingFactorList = new ArrayList<>();
        markingFactorAdapter = new MarkingFactorAdapter();
        markingFactorListView.setLayoutManager(new LinearLayoutManager(getContext()));
        markingFactorListView.setAdapter(markingFactorAdapter);

        MarkingFactor attendanceFactor = new MarkingFactor("Attendance", 0F);
        markingFactorList.add(attendanceFactor);

        MarkingFactor tutorialFactor = new MarkingFactor("Tutorial", 0F);
        markingFactorList.add(tutorialFactor);

        DB.getExamListQuery(courseId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                            Exam exam = dc.toObject(Exam.class);
                            if (exam != null && exam.getName() != null) {
                                MarkingFactor markingFactor = new MarkingFactor(exam.getName(), 0F, dc.getId());
                                markingFactorList.add(markingFactor);
                            }
                        }

                        markingFactorAdapter.setMarkingFactorList(markingFactorList);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });


        gradeOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    gradeOptionListAdapter.setDefaultGradeList();
                    addGradeArea.setVisibility(View.GONE);
                } else if (position == 1) {
                    gradeOptionListAdapter.setCustomGradeList();
                    addGradeArea.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gradeLetterField.getText().toString().isEmpty()){
                    gradeLetterField.setError("Required");
                    gradeLetterField.requestFocus();
                    return;
                }

                if(lowerBoundField.getText().toString().isEmpty()){
                    lowerBoundField.setError("Required");
                    lowerBoundField.requestFocus();
                    return;
                }

                String gradeLetter = gradeLetterField.getText().toString();
                String lowerBound = lowerBoundField.getText().toString();

                if(gradeOptionListAdapter.getGradeLetters().contains(gradeLetter)){
                    Toast.makeText(getContext(), "Grade letter already added", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(gradeOptionListAdapter.getGradeLowerBounds().contains(lowerBound)){
                    Toast.makeText(getContext(), "Lower bound needs to be different.", Toast.LENGTH_SHORT).show();
                    return;
                }

                gradeOptionListAdapter.addCustomGrade(gradeLetter, lowerBound);

                gradeLetterField.setText("");
                lowerBoundField.setText("");


            }
        });


        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float total = 0;
                for (MarkingFactor markingFactor : markingFactorAdapter.getMarkingFactorList()) {
                    total += markingFactor.getPercentage();
                }

                if (total != 100) {
                    Toast.makeText(getContext(), "Total percentage should be 100", Toast.LENGTH_SHORT).show();
                } else {
                    getMarks(markingFactorAdapter.getMarkingFactorList());
                }
            }
        });


        return view;
    }

    private void getMarks(List<MarkingFactor> markingFactors) {
        DB.getClassListQuery(courseId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CourseClass> courseClassList = new ArrayList<>();
                        float totalClass = 0;
                        for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                            CourseClass courseClass = dc.toObject(CourseClass.class);
                            courseClassList.add(courseClass);
                            totalClass++;
                        }

                        HashMap<String, Float> attendanceMark = CalcUtils.calculateAttendance(courseClassList);
                        HashMap<String, Float> attendanceFinal = CalcUtils.calculateMark(attendanceMark, totalClass, markingFactors.get(0).getPercentage());

                        mAttendanceMark = attendanceFinal;
                        mAttendanceMark.put("percent", markingFactors.get(0).getPercentage());
                        done();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });


        DB.getTutorialListQuery(courseId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Exam> tutorialList = new ArrayList<>();
                        int totalExam = 0;
                        float totalExamMarks = 0;
                        for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                            Exam tutorial = dc.toObject(Exam.class);
                            tutorialList.add(tutorial);
                            totalExamMarks += tutorial.getTotalMark();
                            totalExam++;
                        }

                        HashMap<String, Float> factorMarkList = CalcUtils.calculateExamFactor(tutorialList);
                        HashMap<String, Float> averageMarkList = CalcUtils.calculateExamAverage(factorMarkList, totalExam);
                        HashMap<String, Float> tutorialMark = CalcUtils.calculateFactoredMark(averageMarkList, totalExamMarks, markingFactors.get(1).getPercentage());

                        mTutorialMark = tutorialMark;
                        mTutorialMark.put("percent", markingFactors.get(1).getPercentage());

                        done();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });

        DB.getExamListQuery(courseId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                            List<Exam> examList = new ArrayList<>();
                            Exam exam = dc.toObject(Exam.class);
                            examList.add(exam);


                            for (MarkingFactor markFactor : markingFactors) {
                                if (markFactor.getExamId() != null && dc.getId().equals(markFactor.getExamId())) {
                                    HashMap<String, Float> factorMarkList = CalcUtils.calculateExamFactor(examList);
                                    HashMap<String, Float> examMark = CalcUtils.calculateFactoredMark(factorMarkList, exam.getTotalMark(), markFactor.getPercentage());

                                    examMark.put("percent", markFactor.getPercentage());

                                    mExamMarkList.put(markFactor.getExamId(), examMark);

                                }
                            }
                        }

                        done();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });


        DB.getCSRelationCourseQuery(courseId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CSRelation> csRelationList = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            csRelationList.add(documentSnapshot.toObject(CSRelation.class));
                        }

                        DB.getStudentList(csRelationList)
                                .addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                                    @Override
                                    public void onSuccess(List<QuerySnapshot> querySnapshots) {
                                        for (QuerySnapshot querySnapshot : querySnapshots) {
                                            if (querySnapshot.getDocuments().size() > 0) {
                                                UserInfo student = querySnapshot.getDocuments().get(0).toObject(UserInfo.class);

                                                if (student != null && student.getName() != null && student.getUid() != null)
                                                    studentNameMap.put(student.getUid(), student.getName());

                                                if (student != null && student.getRegId() != null && student.getUid() != null)
                                                    studentRegIdMap.put(student.getUid(), student.getRegId());


                                            }
                                        }

                                        done();
                                    }
                                });

                    }
                });
    }

    private void done() {
        taskCounter++;

        if (taskCounter >= 4) {

            Result result = new Result(
                    courseId,
                    studentNameMap,
                    studentRegIdMap,
                    mAttendanceMark,
                    mTutorialMark,
                    mExamMarkList
            );

            DB.addResult(courseId, result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Generated result saved.", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView())
                                    .navigateUp();
                        }
                    });
        }

    }
}
