package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class HelpCenterActivity extends AppCompatActivity {
private CircleImageView whatsapp;
private ImageView facebook,instagram,gmail,youtube;
private Button contactUs,gmailOk,whatsappOk;
    private BottomSheetDialog contactUsDialog;
    private BottomSheetDialog gmailDialog;
    private BottomSheetDialog watsapDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Help Center");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        ////password dialog

        contactUsDialog = new BottomSheetDialog(HelpCenterActivity.this);
        contactUsDialog.setContentView(R.layout.contact_us_dialog);
        contactUsDialog.setCancelable(true);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        contactUsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////password dialog

        ////password dialog

        gmailDialog = new BottomSheetDialog(HelpCenterActivity.this);
        gmailDialog.setContentView(R.layout.gmail_dialog);
        gmailDialog.setCancelable(true);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        gmailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////password dialog

        ////password dialog

        watsapDialog = new BottomSheetDialog(HelpCenterActivity.this);
        watsapDialog.setContentView(R.layout.whatsappdialog);
        watsapDialog.setCancelable(true);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        watsapDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////password dialog


        whatsapp = contactUsDialog.findViewById(R.id.dialogWatsap);
        facebook = contactUsDialog.findViewById(R.id.dialogF);
        instagram =contactUsDialog.findViewById(R.id.dialogInsta);
        gmail = contactUsDialog.findViewById(R.id.dialogGmail);
        youtube = contactUsDialog.findViewById(R.id.dialogYt);
        gmailOk = gmailDialog.findViewById(R.id.gmailok);
        whatsappOk = watsapDialog.findViewById(R.id.whatsappok);
        contactUs = findViewById(R.id.contactus);


        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsDialog.show();
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsDialog.dismiss();
                gotoUrl("https://www.facebook.com/profile.php?id=100040623601985");
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsDialog.dismiss();
                gotoUrl("https://www.instagram.com/sadaqa_t/?hl=en");
            }
        });
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsDialog.dismiss();
                gmailDialog.show();
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsDialog.dismiss();
                gotoUrl("https://www.youtube.com/");
            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsDialog.dismiss();
                watsapDialog.show();
            }
        });

        whatsappOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsDialog.dismiss();
                watsapDialog.dismiss();
                gmailDialog.dismiss();
            }
        });
        gmailOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsDialog.dismiss();
                watsapDialog.dismiss();
                gmailDialog.dismiss();
            }
        });
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_search) {
            Intent searchIntent = new Intent(HelpCenterActivity.this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }else if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}