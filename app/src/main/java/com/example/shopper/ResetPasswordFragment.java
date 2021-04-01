package com.example.shopper;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordFragment extends Fragment {

    private EditText registerdemail;
    private Button resetpassword;

    private ImageView emailIcon;
    private TextView successtext, errortext;
    private ProgressBar progressBar;
    private FrameLayout parentframeLayout;
    private FirebaseAuth firebaseAuth;
    private TextView goback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reset_password, container, false);

        registerdemail = view.findViewById(R.id.forgot_password_email);
        resetpassword = view.findViewById(R.id.reset_btn);
        parentframeLayout = getActivity().findViewById(R.id.register_framelayout);
        emailIcon = view.findViewById(R.id.forgotpasswordemailicon);
        successtext = view.findViewById(R.id.successfulltext);
        progressBar = view.findViewById(R.id.progressBar);
        errortext = view.findViewById(R.id.errortext);
        firebaseAuth = FirebaseAuth.getInstance();
        goback = view.findViewById(R.id.goback);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerdemail.addTextChangedListener(new TextWatcher() {
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
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {



                resetpassword.setEnabled(false);
                resetpassword.setTextColor(getResources().getColor(R.color.lightblack));
                firebaseAuth.sendPasswordResetEmail(registerdemail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    emailIcon.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.VISIBLE);
                                    successtext.setVisibility(View.VISIBLE);
                                    errortext.setVisibility(View.INVISIBLE);

                                    resetpassword.setEnabled(false);
                                    resetpassword.setTextColor(getResources().getColor(R.color.lightblack));


                                }else{
                                    emailIcon.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.VISIBLE);
                                    errortext.setVisibility(View.VISIBLE);
                                    successtext.setVisibility(View.INVISIBLE);



                                    resetpassword.setEnabled(true);
                                    resetpassword.setTextColor(getResources().getColor(R.color.black));

                                }


                            }
                        });
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });
    }
    private void checkInputs(){
        if (TextUtils.isEmpty(registerdemail.getText())){
            resetpassword.setEnabled(false);
            resetpassword.setTextColor(getResources().getColor(R.color.lightblack));

        }else{
            resetpassword.setEnabled(true);
            resetpassword.setTextColor(getResources().getColor(R.color.black));


        }
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentframeLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
