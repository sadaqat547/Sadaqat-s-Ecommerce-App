package com.example.shopper;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.ViewHolder> {
    private List<RewardModel> rewardModelList;
    private Boolean useminiLayout = false;
    private RecyclerView coupensRecyclerview;
    private LinearLayout selectedCoupen;
    private String productOriginalPrice;
    private TextView selectedcoupenTitle;
    private TextView selectedcoupenExpiryDate;
    private TextView selectedcoupenBody;
    private TextView discountedPrice;
    private int cartitemposition ;
    private List<CartItemModel> cartItemModelList;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useminiLayout) {
        this.rewardModelList = rewardModelList;
        this.useminiLayout = useminiLayout;
    }

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useminiLayout, RecyclerView coupensRecyclerview, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useminiLayout = useminiLayout;
        this.coupensRecyclerview = coupensRecyclerview;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedcoupenTitle = coupenTitle;
        this.selectedcoupenExpiryDate = coupenExpiryDate;
        this.selectedcoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
        this.cartitemposition = cartitemposition;
    }
    public MyRewardsAdapter(int cartitemposition,List<RewardModel> rewardModelList, Boolean useminiLayout, RecyclerView coupensRecyclerview, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice,List<CartItemModel> cartItemModelList) {
        this.rewardModelList = rewardModelList;
        this.useminiLayout = useminiLayout;
        this.coupensRecyclerview = coupensRecyclerview;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedcoupenTitle = coupenTitle;
        this.selectedcoupenExpiryDate = coupenExpiryDate;
        this.selectedcoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
        this.cartItemModelList = cartItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (useminiLayout) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);

        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String coupenId = rewardModelList.get(position).getCoupenId();
        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getTimestamp();
        String body = rewardModelList.get(position).getCoupenBody();
        String lower_limit = rewardModelList.get(position).getLower_limit();
        String upper_limit = rewardModelList.get(position).getUpper_limit();
        String discamount = rewardModelList.get(position).getDiscamount();
        Boolean alreadyUsed = rewardModelList.get(position).getAlreadyUsed();
        viewHolder.setData(coupenId,type, validity, body, upper_limit, lower_limit, discamount,alreadyUsed);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView couponTitle;
        private TextView couponExpiryDate;
        private TextView couponBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            couponTitle = itemView.findViewById(R.id.coupen_title);
            couponExpiryDate = itemView.findViewById(R.id.coupen_validaty);
            couponBody = itemView.findViewById(R.id.coupen_body);
        }

        private void setData(final String coupenId, final String type, final Date validity, final String body, final String upper_limit, final String lower_limit, final String discamount, final boolean alreadyUsed) {
            if (type.equals("Discount")) {
                couponTitle.setText(type);

            } else {
                couponTitle.setText("Flat Rs." + discamount + " OFF");
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm aaa");
            if (alreadyUsed){
                couponExpiryDate.setText("Already Used!");
                couponExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.progreesbarred));
                couponBody.setTextColor(Color.parseColor("#C6C6C6"));
                couponTitle.setTextColor(Color.parseColor("#C6C6C6"));
            }else{
                couponBody.setTextColor(Color.parseColor("#ffffff"));
                couponTitle.setTextColor(Color.parseColor("#ffffff"));
                couponExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.validitycolor));
                couponExpiryDate.setText("Till " + simpleDateFormat.format(validity));

            }


            couponBody.setText(body);
            if (useminiLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!alreadyUsed){
                            selectedcoupenTitle.setText(type);
                            selectedcoupenExpiryDate.setText((simpleDateFormat.format(validity)));
                            selectedcoupenBody.setText(body);
                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lower_limit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upper_limit)) {
                                if (type.equals("Discount")) {
                                    Long discountamount = Long.valueOf(productOriginalPrice) * Long.valueOf(discamount) / 100;
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - discountamount) + "/-");
                                } else {
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discamount)) + "/-");

                                }
//                                if (cartitemposition != -1) {
                                    cartItemModelList.get(cartitemposition).setSelectedCoupenId(coupenId);
//                                }
                            } else {
//                                if (cartitemposition != -1) {
                                    cartItemModelList.get(cartitemposition).setSelectedCoupenId(null);
//                                }
                                Toast.makeText(itemView.getContext(), "Sorry! Product doesn't lies between coupen price!", Toast.LENGTH_LONG).show();
                                discountedPrice.setText("Invalid coupen!");
                            }

                            if (coupensRecyclerview.getVisibility() == View.GONE) {
                                coupensRecyclerview.setVisibility(View.VISIBLE);
                                selectedCoupen.setVisibility(View.GONE);
                            } else {
                                coupensRecyclerview.setVisibility(View.GONE);
                                selectedCoupen.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                });
            }
        }
    }
}
