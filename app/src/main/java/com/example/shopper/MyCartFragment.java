package com.example.shopper;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import static com.example.shopper.DBqueries.cartItemModelList;


public class MyCartFragment extends Fragment {
    private RecyclerView cartItemsrecyclerview;
    public static Button continueBtn;
    private TextView totalAmount;


    private BottomSheetDialog loadindDialog;
    public static CartAdapter cartAdapter;
    private Boolean inStock;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        ////loading dialog

        loadindDialog = new BottomSheetDialog(getContext());
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(true);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadindDialog.show();
        ////loading dialog


        totalAmount = view.findViewById(R.id.total_cart_amount);
        continueBtn = view.findViewById(R.id.cart_continue_btnn);

        totalAmount.setVisibility(View.VISIBLE);
        cartItemsrecyclerview = view.findViewById(R.id.cart_items_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsrecyclerview.setLayoutManager(layoutManager);


        cartAdapter = new CartAdapter(DBqueries.cartItemModelList, totalAmount, true);
        cartItemsrecyclerview.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent deliveryintent = new Intent(getContext(),DeliveryActivity.class);
//                startActivity(deliveryintent);
                DeliveryActivity.cartItemModelList = new ArrayList<>();
                DeliveryActivity.fromCart = true;

                for (int x = 0; x < DBqueries.cartItemModelList.size(); x++) {
                    CartItemModel cartItemModel = DBqueries.cartItemModelList.get(x);
                    if (cartItemModel.isInstock()) {
                        DeliveryActivity.cartItemModelList.add(cartItemModel);
                    }
                }
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                loadindDialog.show();
                if (DBqueries.addressesModelList.size() == 0) {
                    DBqueries.loadAddresses(getContext(), loadindDialog,true);

                } else {
                    loadindDialog.dismiss();
                    Intent deliveryIntent = new Intent(getContext(), DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
//                LinearLayout parent = (LinearLayout) continueBtn.getParent();
//                if (cartItemModelList.size() == 0){
//                    parent.setVisibility(View.GONE);
//                }else{
//                    parent.setVisibility(View.VISIBLE);
//
//                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DBqueries.clearData();

            cartAdapter.notifyDataSetChanged();
            if (DBqueries.rewardModelList.size() == 0) {
                loadindDialog.show();
                DBqueries.loadRewards(getContext(), loadindDialog, false);
            }
            if (DBqueries.cartItemModelList.size() == 0) {
                DBqueries.cartlist.clear();
                DBqueries.loadCartList(getContext(), loadindDialog, true, totalAmount);
            } else {
                if (cartItemModelList.get(cartItemModelList.size() - 1).getType() == CartItemModel.TOTAL_AMOUNT) {
                    LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                    parent.setVisibility(View.GONE);

                }else{
                    LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                    parent.setVisibility(View.VISIBLE);
                }
                loadindDialog.dismiss();
            }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (CartItemModel cartItemModel : cartItemModelList ){
            if (!TextUtils.isEmpty(cartItemModel.getSelectedCoupenId())){
                for (RewardModel rewardModel : DBqueries.rewardModelList) {
                    if (rewardModel.getCoupenId().equals(cartItemModel.getSelectedCoupenId())) {
                        rewardModel.setAlreadyUsed(false);
                    }
                }
                cartItemModel.setSelectedCoupenId(null);
                if (MyRewardsFragment.myRewardsAdapter != null  ) {
                    MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}////adaddressa tivity