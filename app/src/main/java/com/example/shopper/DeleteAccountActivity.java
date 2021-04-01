package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteAccountActivity extends AppCompatActivity {

    private Button changeemail,delete;
    private EditText emaillll;
    private BottomSheetDialog loadindDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Delete my account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        emaillll = findViewById(R.id.changeemailText);
        final String email = getIntent().getStringExtra("Email");
        changeemail = findViewById(R.id.changeEmailBtn);
        ////loading dialog

        loadindDialog = new BottomSheetDialog(DeleteAccountActivity.this);
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog
        delete = findViewById(R.id.deleteAccountBtn);
        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeEmailIntent = new Intent(DeleteAccountActivity.this,UpdateUserInfoActivity.class);
                startActivity(changeEmailIntent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emaillll.getText())){
                        loadindDialog.show();
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseFirestore.getInstance().collection("USERS").document(user.getUid()).delete();
                                            Toast.makeText(DeleteAccountActivity.this, "Account Deleted Successfully.", Toast.LENGTH_SHORT).show();
                                            Intent registerIntent = new Intent(DeleteAccountActivity.this,RegisterActivity.class);
                                            startActivity(registerIntent);
                                            finish();
                                        }else{
                                            String error = task.getException().getMessage();
                                            Toast.makeText(DeleteAccountActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                        loadindDialog.dismiss();
                                    }
                                });

                }else{
                    emaillll.setError("Email field should not be empty!");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_share) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"share Demo");
                String sharemessage = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID+"\n\n";
                intent.putExtra(Intent.EXTRA_TEXT,sharemessage);
                startActivity(Intent.createChooser(intent,"Share By"));
            }catch (Exception e){
                Toast.makeText(DeleteAccountActivity.this, "An Unexpected error occured!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }else if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}