package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bitvale.switcher.SwitcherX;

public class ConfirmActivity extends AppCompatActivity {

    private CheckBox termsandcondition;
    private TextView termsbtn;
    private CheckBox codcheck;
    private CheckBox paytmcheck;
    private CheckBox payoneercheck;
    private CheckBox bankcheck;
    private CheckBox jazzcashcheck;
    private CheckBox easypaisacheck;
    private VideoView videoView;

    private ProgressBar progressBar;

    private SwitcherX coupensswitch;

    private Button confirm;
    private Button Decline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Confirm Your Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        termsandcondition = findViewById(R.id.termsandcondition);
        termsbtn = findViewById(R.id.termsbtn);
        codcheck = findViewById(R.id.codcheck);
        paytmcheck = findViewById(R.id.paytmcheck);
        progressBar = findViewById(R.id.progressBarrrrr);
        payoneercheck = findViewById(R.id.payoneercheck);
        bankcheck = findViewById(R.id.banktransfercheck);
        easypaisacheck = findViewById(R.id.easypaisacheck);
        jazzcashcheck = findViewById(R.id.jazzcashcheck);
        coupensswitch = findViewById(R.id.switchercoupen);
        confirm = findViewById(R.id.confirmbtn);
        Decline = findViewById(R.id.decline_btn);


        if (codcheck.isChecked()){
            paytmcheck.setChecked(false);
            payoneercheck.setChecked(false);
            bankcheck.setChecked(false);
            easypaisacheck.setChecked(false);
            jazzcashcheck.setChecked(false);
        }
        if (paytmcheck.isChecked()){
            codcheck.setChecked(false);
            payoneercheck.setChecked(false);
            bankcheck.setChecked(false);
            easypaisacheck.setChecked(false);
            jazzcashcheck.setChecked(false);
        }
        if (payoneercheck.isChecked()){
            paytmcheck.setChecked(false);
            codcheck.setChecked(false);
            bankcheck.setChecked(false);
            easypaisacheck.setChecked(false);
            jazzcashcheck.setChecked(false);
        }
        if (bankcheck.isChecked()){
            paytmcheck.setChecked(false);
            payoneercheck.setChecked(false);
            codcheck.setChecked(false);
            easypaisacheck.setChecked(false);
            jazzcashcheck.setChecked(false);
        }
        if (easypaisacheck.isChecked()){
            paytmcheck.setChecked(false);
            payoneercheck.setChecked(false);
            bankcheck.setChecked(false);
            codcheck.setChecked(false);
            jazzcashcheck.setChecked(false);
        }
        if (jazzcashcheck.isChecked()){
            paytmcheck.setChecked(false);
            payoneercheck.setChecked(false);
            bankcheck.setChecked(false);
            easypaisacheck.setChecked(false);
            codcheck.setChecked(false);
        }
        final String orderId = getIntent().getStringExtra("orderId");
       termsbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(ConfirmActivity.this, "Pending Work", Toast.LENGTH_SHORT).show();
           }
       });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.resetMainActivity = true;
                progressBar.setVisibility(View.VISIBLE);
                if (termsandcondition.isChecked()){
                    Toast.makeText(ConfirmActivity.this, "Thanks!", Toast.LENGTH_SHORT).show();
                    Intent confirmIntent = new Intent(ConfirmActivity.this,OrderconfirmedActivity.class);
                    confirmIntent.putExtra("orderId",orderId);
                    startActivity(confirmIntent);

                    finish();
                }else{
                    termsandcondition.setError("Terms and Conditions are not checked!");
                    Toast.makeText(ConfirmActivity.this, "Terms and Conditions are not checked!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        Decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(ConfirmActivity.this, "Your Order Has Been Placed.Will not Declined more.", Toast.LENGTH_LONG).show();

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }

}