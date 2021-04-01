package com.example.shopper;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {

    private RecyclerView myOrdersRecyclerView;
    public static MyOrderAdapter myOrderAdapter;
    private BottomSheetDialog loadindDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ////loading dialog

        loadindDialog = new BottomSheetDialog((getContext()));
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadindDialog.show();
        ////loading dialog
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        myOrdersRecyclerView = view.findViewById(R.id.myorders_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);
        if (DBqueries.myOrderitemModelList.size() == 0) {
            loadindDialog.dismiss();
        }

        myOrderAdapter = new MyOrderAdapter(DBqueries.myOrderitemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        DBqueries.loadOrders(getContext(), myOrderAdapter, loadindDialog);
        return view;
    }
}