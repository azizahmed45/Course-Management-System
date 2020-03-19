package com.mrgreenapps.coursemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileEditFragment extends Fragment {

    @BindView(R.id.name)
    EditText nameField;

    @BindView(R.id.phone)
    EditText phoneNumberField;

    @BindView(R.id.id)
    EditText idField;

    @BindView(R.id.gender)
    Spinner genderSpinner;

    @BindView(R.id.update_button)
    Button updateButton;

    UserInfo userInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_edit_fragment, container, false);
        ButterKnife.bind(this, view);

        DB.getUser(FirebaseAuth.getInstance().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userInfo = documentSnapshot.toObject(UserInfo.class);

                        if(userInfo != null){
                            if(userInfo.getName() != null) nameField.setText(userInfo.getName());
                            if(userInfo.getRegId() != null) idField.setText(userInfo.getRegId());
                            if(userInfo.getPhoneNumber() != null) phoneNumberField.setText(userInfo.getPhoneNumber());
                            if(userInfo.getType() != null && userInfo.getType().equals(UserInfo.TYPE_TEACHER)) idField.setVisibility(View.GONE);
                        }
                    }
                });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.updateProfile(
                        FirebaseAuth.getInstance().getUid(),
                        nameField.getText().toString(),
                        genderSpinner.getSelectedItem().toString(),
                        phoneNumberField.getText().toString(),
                        idField.getText().toString()
                )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Profile updated.", Toast.LENGTH_SHORT).show();
                                if(userInfo != null && userInfo.getType().equals(UserInfo.TYPE_STUDENT)){
                                    NavHostFragment.findNavController(ProfileEditFragment.this)
                                            .navigate(R.id.action_profileEditFragment_to_studentDashboardFragment);

                                } else if(userInfo != null && userInfo.getType().equals(UserInfo.TYPE_TEACHER)){
                                    NavHostFragment.findNavController(ProfileEditFragment.this)
                                            .navigate(R.id.action_profileEditFragment_to_teacherDashboardFragment2);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Profile Update failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }
}
