package com.mrgreenapps.coursemanagementsystem;

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
import com.mrgreenapps.coursemanagementsystem.model.CourseClass;
import com.mrgreenapps.coursemanagementsystem.model.Tutorial;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TutorialListFragment extends Fragment {

    private String courseId;

    @BindView(R.id.add_button)
    Button addTutorialButton;

    @BindView(R.id.list)
    RecyclerView classListView;

    private TutorialListAdapter tutorialListAdapter;

    private List<DocumentSnapshot> tutorialSnapshotList;

    TutorialListFragment(String courseId){
        this.courseId = courseId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment_for_course, container, false);
        ButterKnife.bind(this, view);

        addTutorialButton.setText("Add Tutorial");

        addTutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTutorial();
            }
        });

        tutorialSnapshotList = new ArrayList<>();

        tutorialListAdapter = new TutorialListAdapter(tutorialSnapshotList);

        classListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        classListView.setAdapter(tutorialListAdapter);

        tutorialListAdapter.setOnItemClickListener(new TutorialListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("tutorial_id", tutorialSnapshotList.get(position).getId());
                bundle.putString("course_id", courseId);
                NavHostFragment.findNavController(TutorialListFragment.this)
                        .navigate(R.id.action_courseFragment_to_tutorialFragment, bundle);
            }
        });

        DB.getTutorialListQuery(courseId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null){
                            tutorialSnapshotList = queryDocumentSnapshots.getDocuments();
                            tutorialListAdapter.setTutorialSnapshotList(tutorialSnapshotList);
                        }

                    }
                });



        return view;
    }

    public void addTutorial(){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_exam, null, false);
        EditText nameForm = view.findViewById(R.id.name);
        Button addButton = view.findViewById(R.id.add_tutorial_button);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameForm.getText().toString().isEmpty()){
                    nameForm.setError("Required");
                    nameForm.requestFocus();
                    return;
                }

                DB.addTutorial(new Tutorial(courseId, nameForm.getText().toString()))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Bundle bundle = new Bundle();
                                bundle.putString("tutorial_id", documentReference.getId());
                                bundle.putString("course_id", courseId);
                                NavHostFragment.findNavController(TutorialListFragment.this)
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
