package com.example.shopper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.bottomsheet.BottomSheetDialog;


public class AllProductsFragment extends Fragment {

    private RecyclerView allProductsGridView;
    public static GridAdapter allProductsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_products, container, false);
        allProductsGridView = view.findViewById(R.id.allproductsGridView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        allProductsGridView.setLayoutManager(layoutManager);

        allProductsAdapter = new GridAdapter(DBqueries.griditemviewList);
        allProductsGridView.setAdapter(allProductsAdapter);
        DBqueries.loadgrid(getContext(), allProductsAdapter);
        return view;
    }
}