package com.example.shopper;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.PrivateKey;

import static com.example.shopper.DBqueries.addressesModelList;
import static com.example.shopper.HomeFragment.refreshlayout;
import static com.example.shopper.RegisterActivity.onResetPasswordFragment;


public class SignInFragment extends Fragment {


    private EditText email, password;
    private ProgressBar progressBar;
    private Button signinbtn;
    private TextView donthaveanaccount;
    private TextView forgotpaswd;
    private TextView closeBtn;
    private FrameLayout parentframelayout;
    public static boolean disableclodebtn = false;
    private Dialog loadindDialog;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        donthaveanaccount = view.findViewById(R.id.tv_signup_text);
        parentframelayout = getActivity().findViewById(R.id.register_framelayout);
        email = view.findViewById(R.id.login_email);
        forgotpaswd = view.findViewById(R.id.forgot_password);
        password = view.findViewById(R.id.login_Password);
        closeBtn = view.findViewById(R.id.sign_in_close_btn);
        ////loading dialog

        loadindDialog = new Dialog(getContext());
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog
        progressBar = view.findViewById(R.id.signin_progressBar);
        signinbtn = view.findViewById(R.id.login_button);
        firebaseAuth = FirebaseAuth.getInstance();
        if (disableclodebtn) {
            closeBtn.setVisibility(View.GONE);
        } else {
            closeBtn.setVisibility(View.VISIBLE);

        }
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donthaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment());
            }
        });
        forgotpaswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ResetPasswordFragment());
                onResetPasswordFragment = true;
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainIntent();
                getActivity().finish();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentframelayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(password.getText())) {
                signinbtn.setEnabled(true);
                signinbtn.setTextColor(getResources().getColor(R.color.black));

            } else {
                signinbtn.setEnabled(false);
                signinbtn.setTextColor(getResources().getColor(R.color.lightblack));
            }

        } else {
            signinbtn.setEnabled(false);
            signinbtn.setTextColor(getResources().getColor(R.color.lightblack));
        }
    }

    private void checkEmailAndPassword() {
            if (email.getText().toString().matches(emailPattern)) {
                if (password.length() >= 4) {
                    progressBar.setVisibility(View.VISIBLE);
                    signinbtn.setEnabled(false);
                    signinbtn.setTextColor(getResources().getColor(R.color.lightblack));
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mainIntent();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        signinbtn.setEnabled(true);
                                        signinbtn.setTextColor(getResources().getColor(R.color.black));
                                    }
                                }
                            });
                } else {
                    password.setError("Incorrect Password!");
                    progressBar.setVisibility(View.INVISIBLE);
                    signinbtn.setEnabled(true);
                    signinbtn.setTextColor(getResources().getColor(R.color.black));
                }
            } else {
                email.setError("Incorrect Email!");
                progressBar.setVisibility(View.INVISIBLE);
                signinbtn.setEnabled(true);
                signinbtn.setTextColor(getResources().getColor(R.color.black));
            }

    }

    private void mainIntent() {
        if (disableclodebtn) {
            disableclodebtn = false;

        } else {
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
            getActivity().finish();

        }
        getActivity().finish();
    }
}