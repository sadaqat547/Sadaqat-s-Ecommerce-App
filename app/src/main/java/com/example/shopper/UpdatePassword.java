package com.example.shopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.bitvale.switcher.SwitcherX;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class UpdatePassword extends Fragment {

    private EditText oldPassword,confirmNewPassword,newPassword;
    private Button updatePasswordBtn;
    private String email;
    private BottomSheetDialog loadindDialog;
    private CheckBox showpassword;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_update_password, container, false);
////loading dialog
        showpassword = view.findViewById(R.id.switchershowPasswords);
        loadindDialog = new BottomSheetDialog(getContext());
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog
        email = getArguments().getString("Email");


        oldPassword = view.findViewById(R.id.oldPassword);
        newPassword = view.findViewById(R.id.newPassword);
        confirmNewPassword = view.findViewById(R.id.confirmNewPassword);
        updatePasswordBtn = view.findViewById(R.id.uUpdateBtnPasswords);
        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
       
        oldPassword.addTextChangedListener(new TextWatcher() {
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
        newPassword.addTextChangedListener(new TextWatcher() {
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
        confirmNewPassword.addTextChangedListener(new TextWatcher() {
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

        return view;
    }
    private void  checkInputs(){
        if (!TextUtils.isEmpty(oldPassword.getText()) && oldPassword.length() >= 4){
            if (!TextUtils.isEmpty(newPassword.getText()) && newPassword.length() >= 4){
                if (!TextUtils.isEmpty(confirmNewPassword.getText()) && confirmNewPassword.length() >= 4){

                        updatePasswordBtn.setEnabled(true);
                    updatePasswordBtn.setTextColor(getResources().getColor(R.color.black));



                }else {
                    updatePasswordBtn.setEnabled(false);
                    updatePasswordBtn.setTextColor(getResources().getColor(R.color.lightblack));
                }

            }else {
                updatePasswordBtn.setEnabled(false);
                updatePasswordBtn.setTextColor(getResources().getColor(R.color.lightblack));

            }

        }else {
            updatePasswordBtn.setEnabled(false);
            updatePasswordBtn.setTextColor(getResources().getColor(R.color.lightblack));


        }
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadindDialog.show();

                checkEmailAndPasswords();

                loadindDialog.dismiss();
            }
        });
    }


    private void checkEmailAndPasswords(){
            if (newPassword.getText().toString().equals(confirmNewPassword.getText().toString())){

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                loadindDialog.show();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, oldPassword.getText().toString());

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                oldPassword.setText(null);
                                                newPassword.setText(null);
                                                confirmNewPassword.setText(null);
                                                getActivity().finish();
                                                Toast.makeText(getContext(), "Password Updated Successfully!", Toast.LENGTH_SHORT).show();
                                            }else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                            }
                                            loadindDialog.dismiss();
                                        }
                                    });
                                }else {
                                    ///errror
                                    loadindDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });





            }else{
                confirmNewPassword.setError("Password isn't matching!");
                updatePasswordBtn.setEnabled(false);
                updatePasswordBtn.setTextColor(getResources().getColor(R.color.lightblack));


            }








    }
}