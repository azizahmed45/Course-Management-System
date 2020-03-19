package com.mrgreenapps.coursemanagementsystem.comon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mrgreenapps.coursemanagementsystem.DB;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import butterknife.ButterKnife;

public class StartFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_fragment, container, false);
        ButterKnife.bind(this, view);


        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DB.getUser(FirebaseAuth.getInstance().getUid())
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                            if (userInfo.getType().equals(UserInfo.TYPE_TEACHER)) {
                                navigateToTeacherDashboard();
                            } else if (userInfo.getType().equals(UserInfo.TYPE_STUDENT)) {
                                navigateToStudentDashboard();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            NavHostFragment.findNavController(StartFragment.this)
                    .navigate(R.id.action_startFragment_to_loginFragment);
        }

        return view;
    }

    private void navigateToTeacherDashboard() {
        NavHostFragment.findNavController(StartFragment.this)
                .navigate(R.id.action_startFragment_to_teacherDashboardFragment2);
    }

    private void navigateToStudentDashboard() {
        NavHostFragment.findNavController(StartFragment.this)
                .navigate(R.id.action_startFragment_to_studentDashboardFragment);
    }

}
