package com.example.shopper;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import static com.example.shopper.ViewAllActivity.wishlistModelList;


public class MyWishlistFragment extends Fragment {

    private RecyclerView wishlistrecyclerview;
    private BottomSheetDialog loadindDialog;
    public static WishlistAdapter wishlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);
        wishlistrecyclerview = view.findViewById(R.id.mywishlist_recyclerview);
////loading dialog

        loadindDialog = new BottomSheetDialog(getContext());
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadindDialog.show();
        ////loading dialog





        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        wishlistrecyclerview.setLayoutManager(linearLayoutManager);

        if (DBqueries.wishlistModelList.size() ==0) {
            DBqueries.clearData();
            DBqueries.loadWishlist(getContext(), loadindDialog, true);
        }else{
            loadindDialog.dismiss();
        }

         wishlistAdapter = new WishlistAdapter(DBqueries.wishlistModelList, true);
        wishlistrecyclerview.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}