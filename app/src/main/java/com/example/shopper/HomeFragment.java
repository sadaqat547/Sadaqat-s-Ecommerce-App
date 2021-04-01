package com.example.shopper;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chy.srlibrary.PTRLayoutView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reginald.swiperefresh.CustomSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

import static com.example.shopper.DBqueries.categoryModelList;
import static com.example.shopper.DBqueries.firebaseFirestore;
import static com.example.shopper.DBqueries.lists;
import static com.example.shopper.DBqueries.loadCategories;
import static com.example.shopper.DBqueries.loadFragmentData;
import static com.example.shopper.DBqueries.loadedCategoriesName;


public class HomeFragment extends Fragment {
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerview;
    private List<HomePageModel> homePageModelfakeList = new ArrayList<>();
    public HomePageAdapter adapter;
    private ImageView nointernet_connection;
    private Button closeapp;
    public ConnectivityManager connectivityManager;
    public NetworkInfo networkInfo;
    private TextView nonetwork;
    private TextView connectinternet;
    public static SwipeRefreshLayout refreshlayout;
    private List<CategoryModel> categoryModelfakeList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        closeapp = view.findViewById(R.id.close_app);
        nointernet_connection = view.findViewById(R.id.no_internet_connection);
        homePageRecyclerview = view.findViewById(R.id.home_page_recyclerview);
        nonetwork = view.findViewById(R.id.nonetwork);
        connectinternet = view.findViewById(R.id.connectinternet);
        refreshlayout = view.findViewById(R.id.refresh_layout);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        refreshlayout.setColorSchemeColors(getContext().getResources().getColor(R.color.black), getContext().getResources().getColor(R.color.progreesbarred), getContext().getResources().getColor(R.color.progreesbargreen));
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager testinglayoutmanager = new LinearLayoutManager(getContext());
        testinglayoutmanager.setOrientation(RecyclerView.VERTICAL);
        homePageRecyclerview.setLayoutManager(testinglayoutmanager);

///categoryfakelist
        categoryModelfakeList.add(new CategoryModel("null", ""));
        categoryModelfakeList.add(new CategoryModel("", ""));
        categoryModelfakeList.add(new CategoryModel("", ""));
        categoryModelfakeList.add(new CategoryModel("", ""));
        categoryModelfakeList.add(new CategoryModel("", ""));
        categoryModelfakeList.add(new CategoryModel("", ""));
        categoryModelfakeList.add(new CategoryModel("", ""));
        categoryModelfakeList.add(new CategoryModel("", ""));
        categoryModelfakeList.add(new CategoryModel("", ""));
        categoryModelfakeList.add(new CategoryModel("", ""));
///categoryfakelist


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



        List<HorizontalProductScrollModel> horizontalProductScrollModelfakeList = new ArrayList<>();
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelfakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));

        List<ImageProductScrollModel> imageProductScrollModelFakeList = new ArrayList<>();
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));
        imageProductScrollModelFakeList.add(new ImageProductScrollModel("", ""));

        homePageModelfakeList.add(new HomePageModel(0, sliderModelfakeList));
        homePageModelfakeList.add(new HomePageModel(1, "", "#dfdfdf",""));
        homePageModelfakeList.add(new HomePageModel(2, "", "#dfdfdf", horizontalProductScrollModelfakeList, new ArrayList<WishlistModel>()));
        ////homepage fake list
        categoryAdapter = new CategoryAdapter(categoryModelfakeList);
        adapter = new HomePageAdapter(homePageModelfakeList);

        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            nointernet_connection.setVisibility(View.GONE);
            nonetwork.setVisibility(View.GONE);
            connectinternet.setVisibility(View.GONE);
            if (categoryModelList.size() == 0) {
                loadCategories(categoryRecyclerView, getContext());
            } else {
                MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                categoryAdapter = new CategoryAdapter(DBqueries.categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }


            if (DBqueries.lists.size() == 0) {
                loadedCategoriesName.add("HOME");
                DBqueries.lists.add(new ArrayList<HomePageModel>());
                DBqueries.loadFragmentData(homePageRecyclerview, getContext(), 0, "HOME");


            } else {
                adapter = new HomePageAdapter(DBqueries.lists.get(0));
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerview.setAdapter(adapter);
        } else {
            closeapp.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.checked).into(nointernet_connection);
            nointernet_connection.setVisibility(View.VISIBLE);
            nonetwork.setVisibility(View.VISIBLE);
            connectinternet.setVisibility(View.VISIBLE);
            ///


            ///
            closeapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.exit(0);
                }
            });

        }





/////
     refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
         @Override
         public void onRefresh() {
             reloadpage();
         }
     });
        /////
       

        return view;


    }

    private   void reloadpage(){
//        categoryModelList.clear();
//        lists.clear();
//        loadedCategoriesName.clear();
        DBqueries.clearData();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            networkInfo = connectivityManager.getActiveNetworkInfo();
            nointernet_connection.setVisibility(View.GONE);
            nonetwork.setVisibility(View.GONE);
            connectinternet.setVisibility(View.GONE);
            categoryAdapter = new CategoryAdapter(categoryModelfakeList);
            adapter = new HomePageAdapter(homePageModelfakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homePageRecyclerview.setAdapter(adapter);
            DBqueries.loadCategories(categoryRecyclerView, getContext());

            DBqueries.loadedCategoriesName.add("HOME");
            DBqueries.lists.add(new ArrayList<HomePageModel>());
            DBqueries.loadFragmentData(homePageRecyclerview, getContext(), 0, "HOME");
        } else {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Toast.makeText(getContext(), "No Network Connection!", Toast.LENGTH_SHORT).show();
            closeapp.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(R.drawable.checked).into(nointernet_connection);
            nointernet_connection.setVisibility(View.VISIBLE);
            nonetwork.setVisibility(View.VISIBLE);
            connectinternet.setVisibility(View.VISIBLE);
            ///


            ///
            closeapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.exit(0);
                }
            });

        }
    }


}