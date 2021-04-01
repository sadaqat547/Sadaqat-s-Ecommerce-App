package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

public class SearchLinkActivity extends AppCompatActivity {
    private Button gotodashbpoard,paste,search;
    private EditText linkpasteedit;
    ClipboardManager clipboardManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_link);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Search product by Link");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gotodashbpoard = findViewById(R.id.gotoDashboARD);
        paste = findViewById(R.id.productlinkeditpastebtn);
        search = findViewById(R.id.activitysearchlinksearchbtn);
        linkpasteedit = findViewById(R.id.productlinkedit);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clipboardManager.hasPrimaryClip()){
                    Toast.makeText(SearchLinkActivity.this, "No text saved in your clipBoard!", Toast.LENGTH_SHORT).show();

                }else {
                    ClipData clipData = clipboardManager.getPrimaryClip();
                    ClipData.Item item = clipData.getItemAt(0);
                    linkpasteedit.setText(item.getText().toString());
                    Toast.makeText(SearchLinkActivity.this, "pasted from clipboard.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        gotodashbpoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

            search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(linkpasteedit.getText().toString())){
                    search.setEnabled(true);
                    Intent productDetailsIntent = new Intent(SearchLinkActivity.this, ProductDetailsActivity.class);
                            productDetailsIntent.putExtra("product_ID",linkpasteedit.getText().toString());
                            startActivity(productDetailsIntent);
                }else {
                    search.setEnabled(false);
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "share Demo");
                String sharemessage = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                intent.putExtra(Intent.EXTRA_TEXT, sharemessage);
                startActivity(Intent.createChooser(intent, "Share By"));
            } catch (Exception e) {
                Toast.makeText(SearchLinkActivity.this, "An Unexpected error occured!", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}