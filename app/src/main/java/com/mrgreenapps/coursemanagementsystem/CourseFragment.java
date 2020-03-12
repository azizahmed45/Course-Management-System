package com.mrgreenapps.coursemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseFragment extends Fragment {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_fragment, container, false);
        ButterKnife.bind(this, view);

        String courseId = getArguments().getString("course_id");

        ClassListFragment classListFragment = new ClassListFragment(courseId);
        TutorialListFragment tutorialListFragment = new TutorialListFragment(courseId);
        ExamListFragment examListFragment = new ExamListFragment(courseId);
        ResultFragment resultFragment = new ResultFragment(courseId);

        openFragment(classListFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.class_id:
                        openFragment(classListFragment);
                        return true;
                    case R.id.assignment:
                        openFragment(tutorialListFragment);
                        return true;
                    case R.id.exam:
                        openFragment(examListFragment);
                        return true;
                    case R.id.result:
                        openFragment(resultFragment);
                        return true;
                }
                return false;
            }
        });


        return view;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

}
