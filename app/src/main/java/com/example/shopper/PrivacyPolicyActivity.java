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
import android.widget.TextView;
import android.widget.Toast;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private TextView privacyGoogle,privacyFb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Privacy Policy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        privacyGoogle = findViewById(R.id.privacyGooglePlay);
        privacyFb = findViewById(R.id.privacyFb);
        privacyFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.facebook.com/profile.php?id=100040623601985");

            }
        });
        privacyGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"share Demo");
                    String sharemessage = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID+"\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT,sharemessage);
                    startActivity(Intent.createChooser(intent,"Share By"));
                }catch (Exception e){
                    Toast.makeText(PrivacyPolicyActivity.this, "An Unexpected error occured!", Toast.LENGTH_SHORT).show();
                }
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
            Intent searchIntent = new Intent(PrivacyPolicyActivity.this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }else if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}