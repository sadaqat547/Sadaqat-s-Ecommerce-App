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


public class MyRewardsFragment extends Fragment {

    private RecyclerView rewardsRecyclerView;
    private BottomSheetDialog loadindDialog;
    public  static MyRewardsAdapter myRewardsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);
        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ////loading dialog

        loadindDialog = new BottomSheetDialog(getContext());
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadindDialog.show();
        ////loading dialog
         myRewardsAdapter = new MyRewardsAdapter(DBqueries.rewardModelList,false);
        rewardsRecyclerView.setAdapter(myRewardsAdapter);
        rewardsRecyclerView.setLayoutManager(layoutManager);
       if (DBqueries.rewardModelList.size() ==0 ){
           DBqueries.loadRewards(getContext(),loadindDialog,true);
       }else{
           loadindDialog.dismiss();
       }

        myRewardsAdapter.notifyDataSetChanged();
        return view;

    }
}