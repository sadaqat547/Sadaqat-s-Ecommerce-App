package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.perfmark.Link;

public class ViewAllActivity extends AppCompatActivity {

private RecyclerView recyclerView;

public  static  List<WishlistModel> wishlistModelList;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
       //





            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            WishlistAdapter adapter = new WishlistAdapter(wishlistModelList, false);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
//        }else{}
        //if (layout_code == 1) {
           // recyclerView.setVisibility(View.INVISIBLE);
         ////   gridview.setVisibility(View.VISIBLE);
         //   List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
          //  horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.downloa, "test", "SD 410 ds", "Rs sd/-"));
         //   horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.im, "Oppo A37", "SD 410 processor", "Rs 14999/-"));
         //   horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.im, "Oppo A37", "SD 410 processor", "Rs 14999/-"));
         //   horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.im, "Oppo A37", "SD 410 processor", "Rs 14999/-"));
         //   horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.image, "Oppo A37", "SD 410 processor", "Rs 14999/-"));
         //   horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.images, "Oppo A37", "SD 410 processor", "Rs 14999/-"));
         //   horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.logo, "Oppo A37", "SD 410 processor", "Rs 14999/-"));
         //   horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.logocircle, "Oppo A37", "SD 410 processor", "Rs 14999/-"));
         //   horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.ic_close, "Oppo A37", "SD 410 processor", "Rs 14999/-"));


         //   GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
          //  gridview.setAdapter(gridProductLayoutAdapter);
         //   gridProductLayoutAdapter.notifyDataSetChanged();
      //  }





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
                Toast.makeText(ViewAllActivity.this, "An Unexpected error occured!", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}