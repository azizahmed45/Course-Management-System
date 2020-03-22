package com.mrgreenapps.coursemanagementsystem.comon;

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
import com.mrgreenapps.coursemanagementsystem.GenerateResultFragment;
import com.mrgreenapps.coursemanagementsystem.comon.course.ClassListFragment;
import com.mrgreenapps.coursemanagementsystem.comon.course.ExamListFragment;
import com.mrgreenapps.coursemanagementsystem.comon.course.NoticeFragment;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.comon.course.ResultFragment;
import com.mrgreenapps.coursemanagementsystem.comon.course.TutorialListFragment;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseFragment extends Fragment {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private String courseId;
    private String userType;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_fragment, container, false);
        ButterKnife.bind(this, view);
        courseId = getArguments().getString("course_id");
        userType = getArguments().getString("user_type");

        if (userType.equals(UserInfo.TYPE_TEACHER)) {
            bottomNavigationView.inflateMenu(R.menu.teacher_course_menu);
        } else if (userType.equals(UserInfo.TYPE_STUDENT)) {
            bottomNavigationView.inflateMenu(R.menu.student_course_menu);
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setItemFragment(item.getItemId());
                return true;
            }
        });

        return view;
    }

    private void setItemFragment(int id) {
        switch (id) {
            case R.id.class_id:
                openFragment(new ClassListFragment(courseId, userType));
                break;
            case R.id.tutorial:
                openFragment(new TutorialListFragment(courseId, userType));
                break;
            case R.id.exam:
                openFragment(new ExamListFragment(courseId, userType));
                break;
            case R.id.result:
                openFragment(new ResultFragment(courseId, userType));
//                openFragment(new GenerateResultFragment(courseId, userType));
                break;
            case R.id.notice:
                openFragment(new NoticeFragment(courseId, userType));
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
