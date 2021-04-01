package com.example.shopper;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.shopper.SignInFragment.disableclodebtn;


public class SignUpFragment extends Fragment {
    private TextView alreadyhaveanaccount;
    private FrameLayout parentframelayout;
    private EditText email, name, password, confirmpassword,otpEdit;
    private Button signupbtn,verifyauth,resend;
    private TextView closebtn,phoneText,timer;
    private BottomSheetDialog otpDialog;

    String verificationCodeBySystem;
    private ProgressBar progressBar;
    private CheckBox checkBox;
    private ImageView timerimage;

    Boolean aa = false;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public static boolean disableclodebtn = false;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        alreadyhaveanaccount = view.findViewById(R.id.tv_logintext);
        checkBox = view.findViewById(R.id.checkBox);
        parentframelayout = getActivity().findViewById(R.id.register_framelayout);
        email = view.findViewById(R.id.signup_email);
        closebtn = view.findViewById(R.id.sign_up_close_btn);
        name = view.findViewById(R.id.signup_fullname);
        password = view.findViewById(R.id.signup_password);
        confirmpassword = view.findViewById(R.id.signup_confirmpassword);
        signupbtn = view.findViewById(R.id.signup_btn);
        progressBar = view.findViewById(R.id.sign_up_progressBar);
        timer = view.findViewById(R.id.textView34);
        timerimage = view.findViewById(R.id.imageView18);
        resend = view.findViewById(R.id.resendbtn);



        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();









        ////otp dialog

        otpDialog = new BottomSheetDialog(getContext());
        otpDialog.setContentView(R.layout.phone_auth_dialog);
        otpDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        otpDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        verifyauth = otpDialog.findViewById(R.id.verify);
        otpEdit = otpDialog.findViewById(R.id.enterOtp);
        phoneText = otpDialog.findViewById(R.id.verificationNo);
        ////otp dialog







        if (disableclodebtn) {
            closebtn.setVisibility(View.GONE);
        } else {
            closebtn.setVisibility(View.VISIBLE);

        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyhaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainIntent();
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
        name.addTextChangedListener(new TextWatcher() {
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
        confirmpassword.addTextChangedListener(new TextWatcher() {
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

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPasswords();

            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);

        fragmentTransaction.replace(parentframelayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(name.getText())) {
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 4) {
                    if (!TextUtils.isEmpty(confirmpassword.getText())) {
                            signupbtn.setEnabled(true);
                            signupbtn.setTextColor(getResources().getColor(R.color.black));

                    } else {
                        signupbtn.setEnabled(false);
                        signupbtn.setTextColor(getResources().getColor(R.color.lightblack));
                    }


                } else {
                    signupbtn.setEnabled(false);
                    signupbtn.setTextColor(getResources().getColor(R.color.lightblack));
                }

            } else {
                signupbtn.setEnabled(false);
                signupbtn.setTextColor(getResources().getColor(R.color.lightblack));

            }

        } else {
            signupbtn.setEnabled(false);
            signupbtn.setTextColor(getResources().getColor(R.color.lightblack));


        }
    }


    private void checkEmailAndPasswords() {
        if (email.getText().toString().matches(emailPattern)) {
            if (password.getText().toString().equals(confirmpassword.getText().toString())) {
                progressBar.setVisibility(View.VISIBLE);
                signupbtn.setEnabled(false);
                signupbtn.setTextColor(getResources().getColor(R.color.lightblack));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    firebaseAuth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Check your Inbox and verify your Email", Toast.LENGTH_SHORT).show();

                                                       signup();


                                                    } else {

                                                    }
                                                }
                                            });

                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signupbtn.setEnabled(true);
                                    signupbtn.setTextColor(getResources().getColor(R.color.black));


                                }
                            }
                        });


            } else {
                confirmpassword.setError("Password isn't matching!");
                progressBar.setVisibility(View.INVISIBLE);
                signupbtn.setEnabled(false);
                signupbtn.setTextColor(getResources().getColor(R.color.lightblack));


            }

        } else {
            email.setError("Email isn't valid!");
            progressBar.setVisibility(View.INVISIBLE);
            signupbtn.setEnabled(false);
            signupbtn.setTextColor(getResources().getColor(R.color.black));


        }


    }






    private void signup(){
        Map<Object, String> userdata = new HashMap<>();
        userdata.put("fullname", name.getText().toString());
        userdata.put("email", email.getText().toString());
        userdata.put("profile", "");

        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).set(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CollectionReference userdataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                    Map<Object, Long> wishlistMap = new HashMap<>();
                    wishlistMap.put("list_size", (long) 0);

                    Map<Object, Long> ratingsMap = new HashMap<>();
                    ratingsMap.put("list_size", (long) 0);

                    Map<Object, Long> cartMap = new HashMap<>();
                    cartMap.put("list_size", (long) 0);

                    Map<Object, Long> myAddressesMap = new HashMap<>();
                    myAddressesMap.put("list_size", (long) 0);
                    Map<Object, Long> myNotificationsMap = new HashMap<>();
                    myNotificationsMap.put("list_size", (long) 0);


                    final List<String> documentnames = new ArrayList<>();
                    documentnames.add("MY_WISHLIST");
                    documentnames.add("MY_RATINGS");
                    documentnames.add("MY_CART");
                    documentnames.add("MY_ADDRESSES");
                    documentnames.add("MY_NOTIFICATIONS");

                    List<Map<Object, Long>> documentFields = new ArrayList<>();
                    documentFields.add(wishlistMap);
                    documentFields.add(ratingsMap);
                    documentFields.add(cartMap);
                    documentFields.add(myAddressesMap);
                    documentFields.add(myNotificationsMap);


                    for (int x = 0; x < documentnames.size(); x++) {

                        final int finalX = x;
                        userdataReference.document(documentnames.get(x)).set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (finalX == documentnames.size() - 1) {
                                        mainIntent();
                                    }

                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    signupbtn.setEnabled(true);
                    signupbtn.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
    }


    private void mainIntent() {
        if (disableclodebtn) {
            disableclodebtn = false;

        } else {

            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
        }
        getActivity().finish();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        long duration = TimeUnit.MINUTES.toMillis(1);
//        new CountDownTimer(duration, 1000) {
//            @Override
//            public void onTick(long l) {
//                String sDuration = String.format(Locale.ENGLISH,"%02d : %02d",TimeUnit.MILLISECONDS.toMinutes(l)
//                        ,TimeUnit.MILLISECONDS.toSeconds(l)
//                        ,TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
//                timer.setText(sDuration);
//            }
//
//            @Override
//            public void onFinish() {
//                timer.setVisibility(View.GONE);
//                timerimage.setVisibility(View.GONE);
//                resend.setVisibility(View.VISIBLE);
//
//            }
//        }.start();
//    }
}