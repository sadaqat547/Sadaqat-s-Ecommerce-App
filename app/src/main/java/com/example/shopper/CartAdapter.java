package com.example.shopper;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter {
    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                return new CartItemViewHolder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productid = cartItemModelList.get(position).getProductid();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupens = cartItemModelList.get(position).getFreeCoupens();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                boolean inStock = cartItemModelList.get(position).isInstock();
                Long productQuantity = cartItemModelList.get(position).getProductQuantity();
                Long maxQuantity = cartItemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIds();
                long stockQty = cartItemModelList.get(position).getStockQuantity();
                boolean cod = cartItemModelList.get(position).isCOD();
                ((CartItemViewHolder) viewHolder).setItemsDetails(productid, resource, title, freeCoupens, productPrice, cuttedPrice, offersApplied, position, inStock, String.valueOf(productQuantity), maxQuantity, qtyError, qtyIds, stockQty,cod);

                break;
            case CartItemModel.TOTAL_AMOUNT:

                int totalItems = 0;
                int totalItemsPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;

                for (int x = 0; x < cartItemModelList.size(); x++) {

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInstock()) {
                        int quantity = Integer.parseInt(String.valueOf(totalItems + cartItemModelList.get(x).getProductQuantity()));

                            totalItems = (totalItems + quantity-1);

                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                            totalItemsPrice = totalItemsPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice()) * quantity;
                        } else {
                            totalItemsPrice = totalItemsPrice + Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()) * quantity;

                        }
                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getCuttedPrice())) {
                            savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getCuttedPrice()) - Integer.parseInt(cartItemModelList.get(x).getProductPrice()))* quantity;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()))* quantity;
                            }
                        }else{
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice())  - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()))* quantity;
                            }
                        }
                    }


                    }
                    if (totalItemsPrice > 1000) {
                        deliveryPrice = "200";
                        totalAmount = totalItemsPrice+(Integer.parseInt(deliveryPrice));

                    } else {
                        deliveryPrice = "free";
                        totalAmount = totalItemsPrice;
                    }
                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemsPrice(totalItemsPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(savedAmount);
                    ((CartTotalAmountViewholder) viewHolder).setTotalAmount(totalItems, totalItemsPrice, deliveryPrice, totalAmount, savedAmount);
                    break;
                    default:
                        return;

                }
                if (lastPosition < position) {
                    Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.slide_from_left_slow);
                    viewHolder.itemView.setAnimation(animation);
                    lastPosition = position;
                }


        }

        @Override
        public int getItemCount () {
            return cartItemModelList.size();
        }

        class CartItemViewHolder extends RecyclerView.ViewHolder {
            private ImageView productImage;
            private TextView productTitle;
            private ImageView freeCoupenIcons;
            private TextView freeCoupens;
            private TextView productPrice;
            private TextView cuttedPrice;
            private TextView offersApplied;
            private TextView coupensApplied;
            private TextView productQuantity;
            private LinearLayout deleteBtn;
            private LinearLayout coupenRedemtionlayout;
            private Button redemBtn;

            private TextView futterText;
            private Button applycoupenBtn;
            private Button removeCoupenBtn;
            private LinearLayout applyorremovecontainer;

            ////coupendialog
            private TextView coupenTitle;
            private TextView coupenExpiryDate;
            private TextView coupenBody;
            private RecyclerView coupensRecyclerview;
            private LinearLayout selectedCoupen;
            private TextView discountedPrice;
            private TextView originalPrice;
            private String productOriginalPrice;
            private TextView coupenRedemtionBody;
            private ImageView codIndicator;
            ////coupendialog


            public CartItemViewHolder(@NonNull View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.product_image);
                coupenRedemtionBody = itemView.findViewById(R.id.couponredemption_text);
                productTitle = itemView.findViewById(R.id.product_image_title);
                freeCoupens = itemView.findViewById(R.id.tv_free_coupon);
                freeCoupenIcons = itemView.findViewById(R.id.free_coupon_icon);
                productPrice = itemView.findViewById(R.id.product_price);
                cuttedPrice = itemView.findViewById(R.id.cutted_price);
                offersApplied = itemView.findViewById(R.id.offers_applied);
                coupensApplied = itemView.findViewById(R.id.coupons_applied);
                productQuantity = itemView.findViewById(R.id.product_quantity);
                deleteBtn = itemView.findViewById(R.id.remove_item_btn);
                coupenRedemtionlayout = itemView.findViewById(R.id.cupon_redemtion_layout);
                redemBtn = itemView.findViewById(R.id.couponredemption_btn);
                codIndicator = itemView.findViewById(R.id.cod_indicator);


            }

            private void setItemsDetails(final String productid, String resource, String title, Long freeCoupensNo, final String productPriceText, String cuttedPriceText, Long offersAppliedNo, final int position, boolean instock, final String Quantity, final Long maxQuantity, boolean qtyError, final List<String> qtyIds, final long stockQty,boolean COD) {
                Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.slider_background_light)).into(productImage);
                productTitle.setText(title);

                final Dialog checkCoupenPriceDialog = new Dialog(itemView.getContext());
                checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
                checkCoupenPriceDialog.setCancelable(false);
                checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                if (COD){
                    codIndicator.setVisibility(View.VISIBLE);
                    codIndicator.setAlpha((float) 1);

                }else{
//                    codIndicator.setVisibility(View.INVISIBLE);
                    codIndicator.setAlpha((float) 0.4);

                }
                if (instock) {

                    productPrice.setText("Rs." + productPriceText + "/-");
                    productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                    cuttedPrice.setText("Rs." + cuttedPriceText + "/-");
                    coupenRedemtionlayout.setVisibility(View.VISIBLE);
                    //coupen dialog

                    ImageView toggleRecyclerview = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
                    coupensRecyclerview = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
                    selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
                    coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
                    coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validaty);
                    coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);
                    originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
                    discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);

                    removeCoupenBtn = checkCoupenPriceDialog.findViewById(R.id.dialogcancel_btn);
                    applycoupenBtn = checkCoupenPriceDialog.findViewById(R.id.applynowbtn);
                    applyorremovecontainer = checkCoupenPriceDialog.findViewById(R.id.applyorremovecontainer);
                    futterText = checkCoupenPriceDialog.findViewById(R.id.futter_text);

                    futterText.setVisibility(View.INVISIBLE);
                    applyorremovecontainer.setVisibility(View.VISIBLE);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    coupensRecyclerview.setLayoutManager(layoutManager);

                    productOriginalPrice = productPriceText;
                    originalPrice.setText(productPrice.getText());
                    MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(position, DBqueries.rewardModelList, true, coupensRecyclerview, selectedCoupen, productOriginalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice,cartItemModelList);
                    coupensRecyclerview.setAdapter(myRewardsAdapter);
                    myRewardsAdapter.notifyDataSetChanged();
                    applycoupenBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                                for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                    if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                        rewardModel.setAlreadyUsed(true);
                                        coupenRedemtionlayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.rewards_background));
                                        coupenRedemtionBody.setText(rewardModel.getCoupenBody());
                                        redemBtn.setText("Change Coupen");
                                    }
                                }
                                coupensApplied.setVisibility(View.VISIBLE);
                                cartItemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2));
                                productPrice.setText(discountedPrice.getText());
                                String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2)));
                                coupensApplied.setText("Coupens Applied - Rs." + offerDiscountedAmt + "/-");
                                notifyItemChanged(cartItemModelList.size()-1);
                                checkCoupenPriceDialog.dismiss();
                            }
                        }
                    });
                    removeCoupenBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                    rewardModel.setAlreadyUsed(false);
                                }
                            }
                            coupenTitle.setText("Select  Coupen...");
                            coupenBody.setText("Tap the navigation icon on the top right corner to select your coupen. Thanks! ");
                            coupenExpiryDate.setText("Tap the icon on top right corner...");
                            coupensApplied.setVisibility(View.INVISIBLE);
                            coupenRedemtionlayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupenred));
                            coupenRedemtionBody.setText("Check price after coupen redemption");
                            redemBtn.setText("Redeem");
                            cartItemModelList.get(position).setSelectedCoupenId(null);
                            productPrice.setText("Rs."+productPriceText+"/-");
                            notifyItemChanged(cartItemModelList.size()-1);

                            checkCoupenPriceDialog.dismiss();
                        }
                    });
                    toggleRecyclerview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialogrecyclerview();
                        }
                    });
                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                        for (RewardModel rewardModel : DBqueries.rewardModelList) {
                            if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                coupenRedemtionlayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.rewards_background));
                                coupenRedemtionBody.setText(rewardModel.getCoupenBody());
                                redemBtn.setText("Change Coupen");
                                coupenBody.setText(rewardModel.getCoupenBody());
                                if (rewardModel.getType().equals("Discount")) {
                                    coupenTitle.setText(rewardModel.getType());

                                } else {
                                    coupenTitle.setText("Flat Rs." + rewardModel.getDiscamount() + " OFF");
                                }
                                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm aaa");
                                coupenExpiryDate.setText("Till " + simpleDateFormat.format(rewardModel.getTimestamp()));

                            }
                        }
                        discountedPrice.setText("Rs." + cartItemModelList.get(position).getDiscountedPrice() + "/-");
                        coupensApplied.setVisibility(View.VISIBLE);
                        productPrice.setText("Rs." + cartItemModelList.get(position).getDiscountedPrice() + "/-");
                        String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));
//                        DBqueries.cartItemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2));
                        coupensApplied.setText("Coupens Applied - Rs." + offerDiscountedAmt + "/-");
                    } else {
                        coupensApplied.setVisibility(View.INVISIBLE);
                        coupenRedemtionlayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupenred));
                        coupenRedemtionBody.setText("Check price after coupen redemption");
                        redemBtn.setText("Redeem");
                    }
                    //coupendialog

                    productQuantity.setText("Qty: " + Quantity);
                    if (!showDeleteBtn) {
                        if (qtyError) {
                            productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.progreesbarred));
                            productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.progreesbarred)));

                        } else {
                            productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                            productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.black)));
                        }
                    }
                    productQuantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final BottomSheetDialog quantityDialog = new BottomSheetDialog(itemView.getContext());
                            quantityDialog.setContentView(R.layout.quantity_dialog);
                            quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            quantityDialog.setCancelable(false);
                            quantityDialog.show();
                            final EditText quantityno = quantityDialog.findViewById(R.id.quantity_no);
                            Button cancelbtn = quantityDialog.findViewById(R.id.cancel_btn);
                            Button okbtn = quantityDialog.findViewById(R.id.ok_btn);
                            quantityno.setHint("Max " + String.valueOf(maxQuantity));

                            cancelbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    quantityDialog.dismiss();
                                }
                            });
                            okbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!TextUtils.isEmpty(quantityno.getText())) {
                                        if (Long.valueOf(quantityno.getText().toString()) <= maxQuantity && Long.valueOf(quantityno.getText().toString()) != 0) {
                                            if (itemView.getContext() instanceof MainActivity) {
                                                cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityno.getText().toString()));
                                            } else {
                                                if (DeliveryActivity.fromCart) {
                                                    cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityno.getText().toString()));
                                                } else {
                                                    DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityno.getText().toString()));

                                                }
                                            }
                                            productQuantity.setText("Qty: " + quantityno.getText());
                                            notifyItemChanged(cartItemModelList.size()-1);
                                            if (!showDeleteBtn) {
                                                DeliveryActivity.loadindDialog.show();
                                                DeliveryActivity.cartItemModelList.get(position).setQtyError(false);
                                                final int initialQty = Integer.parseInt(Quantity);
                                                final int finalQty = Integer.parseInt(quantityno.getText().toString());
                                                final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                                if (finalQty > initialQty) {
                                                    for (int x = 0; x < cartItemModelList.size() - 1; x++) {

                                                        for (int y = 0; y < finalQty - initialQty; y++) {
                                                            final String quantitydocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                            Map<String, Object> timestamp = new HashMap<>();
                                                            timestamp.put("time", FieldValue.serverTimestamp());
                                                            final int finalY = y;
                                                            firebaseFirestore.collection("PRODUCTS").document(productid).collection("QUANTITY").document(quantitydocumentName)
                                                                    .set(timestamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.add(quantitydocumentName);
                                                                    if (finalY + 1 == finalQty - initialQty) {
                                                                        firebaseFirestore.collection("PRODUCTS").document(productid).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING)
                                                                                .limit(stockQty).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    List<String> serverQuantities = new ArrayList<>();

                                                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                        serverQuantities.add(queryDocumentSnapshot.getId());
                                                                                    }
                                                                                    long AvailableQty = 0;
                                                                                    for (String qtyId : qtyIds) {
                                                                                        if (!serverQuantities.add(qtyId)) {

                                                                                            DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                            DeliveryActivity.cartItemModelList.get(position).setMaxQuantity(AvailableQty);
                                                                                            Toast.makeText(itemView.getContext(), "All products may not be available at required quantity!", Toast.LENGTH_SHORT).show();

                                                                                        } else {
                                                                                            AvailableQty++;

                                                                                        }

                                                                                    }
                                                                                    DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                } else {
                                                                                    ///errror
                                                                                    String error = task.getException().getMessage();
                                                                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                }
                                                                                DeliveryActivity.loadindDialog.dismiss();
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                } else if (initialQty > finalQty) {
                                                    for (int x = 0; x < initialQty - finalQty; x++) {
                                                        final String qtyID = qtyIds.get(qtyIds.size() - 1 - x);
                                                        final int finalX = x;
                                                        firebaseFirestore.collection("PRODUCTS").document(productid).collection("QUANTITY").document(qtyID).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        qtyIds.remove(qtyID);
                                                                        DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                        if (finalX+1 == initialQty - finalQty){
                                                                            DeliveryActivity.loadindDialog.dismiss();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }


                                        } else {
                                            Toast.makeText(itemView.getContext(), "Sorry Max quantity is " + maxQuantity.toString(), Toast.LENGTH_SHORT).show();

                                        }

                                    }


                                    quantityDialog.dismiss();

                                }
                            });

                        }
                    });
                    if (offersAppliedNo > 0) {
                        String offerDiscountedAmt = String.valueOf(Long.valueOf(cuttedPriceText) - Long.valueOf(productPriceText));
                        offersApplied.setVisibility(View.VISIBLE);
                        offersApplied.setText("Offer Applied - Rs." + offerDiscountedAmt + "/-");
                    } else {
                        offersApplied.setText("");
                        offersApplied.setVisibility(View.INVISIBLE);
                    }

                    if (freeCoupensNo > 0) {
                        freeCoupenIcons.setVisibility(View.VISIBLE);
                        freeCoupens.setVisibility(View.VISIBLE);
                        if (freeCoupensNo == 1) {
                            freeCoupens.setText("Free " + freeCoupensNo + " Coupen");

                        } else {
                            freeCoupens.setText("Free " + freeCoupensNo + " Coupens");
                        }

                    } else {
                        freeCoupenIcons.setVisibility(View.INVISIBLE);
                        freeCoupens.setVisibility(View.INVISIBLE);
                    }


                } else {
                    freeCoupens.setVisibility(View.INVISIBLE);
                    freeCoupenIcons.setVisibility(View.INVISIBLE);
                    productQuantity.setText("Qty: 0 ");
                    coupensApplied.setVisibility(View.GONE);
                    offersApplied.setVisibility(View.GONE);
                    productQuantity.setCompoundDrawables(null, null, null, null);

                    productQuantity.setTextColor(Color.parseColor("#70000000"));
                    productQuantity.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#70000000")));
                    coupenRedemtionlayout.setVisibility(View.GONE);
                    productPrice.setText("Out of Stock!");
                    cuttedPrice.setText("");
                    productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.progreesbarred));

                }


                if (showDeleteBtn) {
                    deleteBtn.setVisibility(View.VISIBLE);

                } else {
                    deleteBtn.setVisibility(View.GONE);
                }

                redemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (RewardModel rewardModel : DBqueries.rewardModelList) {
                            if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                        checkCoupenPriceDialog.show();

                    }
                });
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                            for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                    rewardModel.setAlreadyUsed(false);
                                }
                            }
                        }
                            if (!ProductDetailsActivity.running_cart_query) {
                            ProductDetailsActivity.running_cart_query = true;

                            DBqueries.removefromCart(position, itemView.getContext(), cartTotalAmount);
                        }
                    }
                });

            }

            private void showDialogrecyclerview() {
                if (coupensRecyclerview.getVisibility() == View.GONE) {
                    coupensRecyclerview.setVisibility(View.VISIBLE);
                    selectedCoupen.setVisibility(View.GONE);
                } else {
                    coupensRecyclerview.setVisibility(View.GONE);
                    selectedCoupen.setVisibility(View.VISIBLE);

                }
            }
        }

        class CartTotalAmountViewholder extends RecyclerView.ViewHolder {
            private TextView totalItems;
            private TextView totalItemsPrice;
            private TextView deliveryPrice;
            private TextView totalAmount;
            private TextView savedAmount;


            public CartTotalAmountViewholder(@NonNull View itemView) {
                super(itemView);
                totalItems = itemView.findViewById(R.id.totsl_items);
                totalItemsPrice = itemView.findViewById(R.id.total_items_price);
                deliveryPrice = itemView.findViewById(R.id.delivery_price);
                totalAmount = itemView.findViewById(R.id.total_price);
                savedAmount = itemView.findViewById(R.id.saved_amount);
            }


            private void setTotalAmount(int totalItemstext, int totalItempricetext, String deliveryPricetext, int totalAmountText, int savedAmounttext) {
                totalItems.setText("Price ("+totalItemstext+")");
                totalItemsPrice.setText("Rs." + totalItempricetext + "/-");
                if (deliveryPricetext.equals("Free")) {
                    deliveryPrice.setText(deliveryPricetext);
                } else {
                    deliveryPrice.setText("Rs."+deliveryPricetext+"/-");
                }
                totalAmount.setText("Rs." + totalAmountText + "/-");
                cartTotalAmount.setText("Rs." + totalAmountText + "/-");
                cartTotalAmount.setVisibility(View.VISIBLE);
                savedAmount.setText("You saved Rs." + savedAmounttext + "/- on this order.");
                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                if (totalItempricetext == 0) {
                    if (DeliveryActivity.fromCart) {
                        cartItemModelList.remove(cartItemModelList.size() - 1);
                        DeliveryActivity.cartItemModelList.remove(DeliveryActivity.cartItemModelList.size() - 1);
                    }
                    if (showDeleteBtn) {
                        cartItemModelList.remove(cartItemModelList.size() - 1);

                    }
                    parent.setVisibility(View.GONE);

                } else {
                    parent.setVisibility(View.VISIBLE);
                }
            }

        }

    }
