package com.mrgreenapps.coursemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    @BindView(R.id.email_field)
    EditText emailField;

    @BindView(R.id.password_field)
    EditText passwordFiled;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            navigateToTeacherDashboard();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.login_button)
    public void login() {
        String email = emailField.getText().toString();
        String password = passwordFiled.getText().toString();

        if (email.isEmpty()) {
            emailField.setError("required");
            emailField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordFiled.setError("required");
            passwordFiled.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult.getUser() != null) {
                            Toast.makeText(getContext(), "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            navigateToTeacherDashboard();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Login failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.sign_up_button)
    public void signUp(){
        NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_signUpFragment2);
    }

    private void navigateToTeacherDashboard() {
        NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_teacherDashboardFragment2);
    }
}
