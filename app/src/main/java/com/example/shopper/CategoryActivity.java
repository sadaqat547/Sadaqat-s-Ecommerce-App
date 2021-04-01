package com.example.shopper;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.shopper.DBqueries.lists;
import static com.example.shopper.DBqueries.loadFragmentData;
import static com.example.shopper.DBqueries.loadedCategoriesName;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;
    private List<HomePageModel> homePageModelfakeList = new ArrayList<>();

    private HomePageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ////homepage fake list
        List<SliderModel> sliderModelfakeList = new ArrayList<>();
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelfakeList.add(new SliderModel("null", "#dfdfdf"));



        List<HorizontalProductScrollModel> horizontalProductScrollModelfakeList = new ArrayList<>();
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("","","","",""));
        homePageModelfakeList.add(new HomePageModel(0,sliderModelfakeList));
        homePageModelfakeList.add(new HomePageModel(1,"","#F0F0F0",""));
        homePageModelfakeList.add(new HomePageModel(2,"","#F0F0F0",horizontalProductScrollModelfakeList,new ArrayList<WishlistModel>()));


        ////homepage fake list
        categoryRecyclerView = findViewById(R.id.category_recycler_view);








/////baner slider



        ////horizontalprodyct layout






        ////horizontalprodyct layout



        /////////////

        LinearLayoutManager testinglayoutmanager = new LinearLayoutManager(this);
        testinglayoutmanager.setOrientation(RecyclerView.VERTICAL);
        categoryRecyclerView.setLayoutManager(testinglayoutmanager);
        adapter = new HomePageAdapter(homePageModelfakeList);

        int listposition = 0;
        for (int x = 0;x< loadedCategoriesName.size();x++){
    if (loadedCategoriesName.get(x).equals(title.toUpperCase())){
        listposition = x;
    }
}
        if (listposition == 0){
    loadedCategoriesName.add(title.toUpperCase());
    lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecyclerView,this,loadedCategoriesName.size() -1,title);
}else{
            adapter = new HomePageAdapter(lists.get(listposition));

        }
        categoryRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();


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
            Intent searchIntent = new Intent(CategoryActivity.this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }else if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}