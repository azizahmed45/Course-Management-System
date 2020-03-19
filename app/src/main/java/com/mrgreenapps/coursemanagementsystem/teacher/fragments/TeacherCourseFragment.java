package com.mrgreenapps.coursemanagementsystem.teacher.fragments;

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
import com.mrgreenapps.coursemanagementsystem.NoticeFragment;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.teacher.fragments.ClassListFragment;
import com.mrgreenapps.coursemanagementsystem.teacher.fragments.ExamListFragment;
import com.mrgreenapps.coursemanagementsystem.teacher.fragments.ResultFragment;
import com.mrgreenapps.coursemanagementsystem.teacher.fragments.TutorialListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherCourseFragment extends Fragment {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    
    String courseId;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_fragment, container, false);
        ButterKnife.bind(this, view);
        courseId = getArguments().getString("course_id");


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setItemFragment(item.getItemId());
                return true;
            }
        });

        return view;
    }

    private void setItemFragment(int id){
        switch (id) {
            case R.id.class_id:
                openFragment(new ClassListFragment(courseId));
                break;
            case R.id.tutorial:
                openFragment(new TutorialListFragment(courseId));
                break;
            case R.id.exam:
                openFragment(new ExamListFragment(courseId));
                break;
            case R.id.result:
                openFragment(new ResultFragment(courseId));
                break;
            case R.id.notice:
                NoticeFragment noticeFragment = new NoticeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("course_id", courseId);
                noticeFragment.setArguments(bundle);
                openFragment(noticeFragment);
                break;
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public void onResume() {
        setItemFragment(bottomNavigationView.getSelectedItemId());
        super.onResume();

    }
}
