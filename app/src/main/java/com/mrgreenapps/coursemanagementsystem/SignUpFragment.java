package com.mrgreenapps.coursemanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";

    @BindView(R.id.email_field)
    EditText emailField;

    @BindView(R.id.password_field)
    EditText passwordFiled;

    @BindView(R.id.password_verify_field)
    EditText verifyPasswordFiled;

    @BindView(R.id.account_type_spinner)
    Spinner accountTypeSpinner;

    @BindView(R.id.sign_up_button)
    Button signUpButton;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            navigateToProfileEdit();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.sign_up_button})
    public void signUp() {
        String email = emailField.getText().toString();
        String password = passwordFiled.getText().toString();
        String verifyPassword = verifyPasswordFiled.getText().toString();

        if (email.isEmpty()) {
            emailField.setError("required");
            emailField.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("not a valid email");
            emailField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordFiled.setError("required");
            passwordFiled.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordFiled.setError("password length needs at least 6 characters");
            passwordFiled.requestFocus();
            return;
        }

        if (!password.equals(verifyPassword)) {
            verifyPasswordFiled.setError("password not matched");
            verifyPasswordFiled.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult.getUser() != null) {
                            Toast.makeText(getContext(), "Signed Up Successfully.", Toast.LENGTH_SHORT).show();
                            UserInfo userInfo = new UserInfo();
                            userInfo.setType(accountTypeSpinner.getSelectedItem().toString());
                            userInfo.setUid(authResult.getUser().getUid());

                            DB.addUser(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void data) {
                                    if(userInfo.getType().equals(UserInfo.TYPE_TEACHER))
                                        navigateToProfileEdit();
                                    else if(userInfo.getType().equals(UserInfo.TYPE_STUDENT))
                                        navigateToProfileEdit();
                                }
                            });

                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Sign Up failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToProfileEdit() {
        NavHostFragment.findNavController(SignUpFragment.this)
                .navigate(R.id.action_signUpFragment2_to_profileEditFragment);
    }
}
