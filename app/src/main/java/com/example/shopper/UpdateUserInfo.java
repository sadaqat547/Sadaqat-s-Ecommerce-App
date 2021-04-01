package com.example.shopper;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class UpdateUserInfo extends Fragment {


    ///updateuserinfo
    private CircleImageView profileImage;
    private TextView  updateprofiletext, removerprofiletext;
    private EditText nameedit, emailedit, password;
    private Button updatuserinfobtn, done;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    ///updateuserinfo
    private BottomSheetDialog loadindDialog;
    private BottomSheetDialog passwordDialog;
    private String name;
    private String email;
    private String profile;
    private Uri uri;
    private boolean updatephoto = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_user_info, container, false);
        ///updateuserinfo
        ////loading dialog

        loadindDialog = new BottomSheetDialog(getContext());
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog
        ////password dialog

        passwordDialog = new BottomSheetDialog(getContext());
        passwordDialog.setContentView(R.layout.password_confirmation_layout);
        passwordDialog.setCancelable(true);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////password dialog

        password = passwordDialog.findViewById(R.id.dialogPassword);
        done = passwordDialog.findViewById(R.id.dialogDonebtn);
        profileImage = view.findViewById(R.id.fragmentProfile);
        updateprofiletext = view.findViewById(R.id.updateProfileText);
        removerprofiletext = view.findViewById(R.id.removeProfileText);
        nameedit = view.findViewById(R.id.updatenameedit);
        emailedit = view.findViewById(R.id.updateemailedit);
        updatuserinfobtn = view.findViewById(R.id.updateuserinfoBtn);

        ///updateuserinfo    }

        assert getArguments() != null;
        name = getArguments().getString("Name");
        email = getArguments().getString("Email");
        profile = getArguments().getString("Profile");

        Glide.with(getContext()).load(profile).into(profileImage);
        nameedit.setText(name);
        emailedit.setText(email);


        updateprofiletext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntennt = new Intent(Intent.ACTION_PICK);
                        galleryIntennt.setType("image/*");
                        startActivityForResult(galleryIntennt, 1);
                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntennt = new Intent(Intent.ACTION_PICK);
                    galleryIntennt.setType("image/*");
                    startActivityForResult(galleryIntennt, 1);
                }
            }
        });


        removerprofiletext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = null;
                updatephoto = true;
                Glide.with(getContext()).load(R.mipmap.ic_close).into(profileImage);

            }
        });

        updatuserinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPasswords();
            }
        });

        emailedit.addTextChangedListener(new TextWatcher() {
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
        nameedit.addTextChangedListener(new TextWatcher() {
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

    private void checkInputs() {
        if (!TextUtils.isEmpty(emailedit.getText())) {
            if (!TextUtils.isEmpty(nameedit.getText())) {

                updatuserinfobtn.setEnabled(true);
                updatuserinfobtn.setTextColor(getResources().getColor(R.color.progreesbarred));


            } else {
                updatuserinfobtn.setEnabled(false);
                updatuserinfobtn.setTextColor(getResources().getColor(R.color.lightblack));


            }
        }
    }

    private void checkEmailAndPasswords() {

        if (emailedit.getText().toString().matches(emailPattern)) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (emailedit.getText().toString().toLowerCase().trim().equals(email.toLowerCase().trim())) {
                loadindDialog.show();
                updatePhoto(user);
            } else {
                passwordDialog.show();
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (TextUtils.isEmpty(password.getText())){
                            password.setError("Password can,t be empty!");
                        }else {
                            loadindDialog.show();


                            String userPassword = password.getText().toString();
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(email, userPassword);
                            passwordDialog.dismiss();

                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            user.updateEmail(emailedit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if (task.isSuccessful()) {
                                                                        updatePhoto(user);
                                                                    } else {
                                                                        loadindDialog.dismiss();
                                                                        ///errror
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }
                                                            });
                                                        }else{
                                                            loadindDialog.dismiss();
                                                            ///errror
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                        }
                                                        }
                                                });

                                            } else {
                                                loadindDialog.dismiss();
                                                ///errror
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        } else {
            emailedit.setError("Email isn't valid!");

            updatuserinfobtn.setEnabled(false);
            updatuserinfobtn.setTextColor(getResources().getColor(R.color.lightblack));


        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    updatephoto = true;
                    Glide.with(getContext()).load(uri).into(profileImage);

                } else {
                    Toast.makeText(getContext(), "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntennt = new Intent(Intent.ACTION_PICK);
                galleryIntennt.setType("image/*");
                startActivityForResult(galleryIntennt, 1);

            } else {
                Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void updatePhoto(final FirebaseUser user){
        //updateing photo

        if (updatephoto) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile/" + user.getUid() + ".jpg");

            if (uri != null) {

                Glide.with(getContext()).asBitmap().load(uri).circleCrop().into(new ImageViewTarget<Bitmap>(profileImage) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                uri = task.getResult();
                                                DBqueries.profile = task.getResult().toString();
                                                Glide.with(getContext()).load(DBqueries.profile).into(profileImage);
                                                Map<String, Object> updateData = new HashMap<>();
                                                updateData.put("email", emailedit.getText().toString());
                                                updateData.put("fullname", nameedit.getText().toString());
                                                updateData.put("profile", DBqueries.profile);

                                                updateField(user,updateData);
                                            } else {
                                                loadindDialog.dismiss();
                                                DBqueries.profile = "";
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                } else {
                                    loadindDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        return;

                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        profileImage.setImageResource(R.mipmap.ic_launcher);
                    }
                });
            } else { ////removing photo

                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            profileImage = null;
                            DBqueries.profile = "";
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("email", emailedit.getText().toString());
                            updateData.put("fullname", nameedit.getText().toString());
                            updateData.put("profile", "");

                            updateField(user,updateData);
                        }else {
                            loadindDialog.dismiss();
                            ///errror
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("fullname", nameedit.getText().toString());
            updateField(user,updateData);
        }
        //updateing photo
    }



    private void updateField(FirebaseUser user, final Map<String,Object> updateData){
        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid()).update(updateData).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (updateData.size() >2){
                                DBqueries.email = emailedit.getText().toString().trim();
                                DBqueries.fullname = nameedit.getText().toString().trim();

                            }else {
                                DBqueries.fullname = nameedit.getText().toString();
                            }
                            Toast.makeText(getContext(), "Updated successfully!", Toast.LENGTH_SHORT).show();
                            getActivity().finish();

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                        }
                        loadindDialog.dismiss();
                    }
                });
    }
}