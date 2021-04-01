package com.example.shopper;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {
    private List<MyOrderitemModel> myOrderitemModelList;
    private int lastPosition = -1;

    public MyOrderAdapter(List<MyOrderitemModel> myOrderitemModelList) {
        this.myOrderitemModelList = myOrderitemModelList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_orders_item_layout, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder viewholder, int position) {
        String resource = myOrderitemModelList.get(position).getProductImage();
        String title = myOrderitemModelList.get(position).getProductTitle();
        String productId = myOrderitemModelList.get(position).getProductId();
        String orderStatus = myOrderitemModelList.get(position).getOrderStatus();
        String productPrice = myOrderitemModelList.get(position).getProductPrice();
        Long productqty = myOrderitemModelList.get(position).getProductQty();
        Date date;
        switch (orderStatus) {
            case "Ordered":
                date = myOrderitemModelList.get(position).getOrdeedrDate();
                break;
            case "Packed":
                date = myOrderitemModelList.get(position).getPackedDate();
                break;
            case "Shipped":
                date = myOrderitemModelList.get(position).getShippeddate();
                break;
            case "Delivered":
                date = myOrderitemModelList.get(position).getDeliveredDate();
                break;
            case "Cancelled":
                date = myOrderitemModelList.get(position).getCancelledDate();
                break;
            default:
                date = myOrderitemModelList.get(position).getCancelledDate();

        }
        viewholder.setData(resource, title, orderStatus, date, productId, position,productPrice,productqty);


        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewholder.itemView.getContext(), R.anim.slide_from_left_slow);
            viewholder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return myOrderitemModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;
        private TextView productPrice;
        private TextView productQty;

        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            orderIndicator = itemView.findViewById(R.id.order_indicator);
            productTitle = itemView.findViewById(R.id.product_title);
            deliveryStatus = itemView.findViewById(R.id.order_delivered_date);
            productPrice = itemView.findViewById(R.id.product_pppppprice);
            productQty = itemView.findViewById(R.id.product_qqqqquantity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ordersdetailIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    itemView.getContext().startActivity(ordersdetailIntent);
                }
            });


        }
        Long totalitemsPriceValue;
        private void setData(String resource, String title, String orderStatus, Date date, String productId, final int position, String ppproductPrice,Long productqty) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.slider_background_light)).into(productImage);
            productTitle.setText(title);

            totalitemsPriceValue = Long.valueOf(myOrderitemModelList.get(position).getProductPrice())*myOrderitemModelList.get(position).getProductQty();
            productQty.setText("Qty: "+productqty);
            productPrice.setText("Rs."+totalitemsPriceValue+"/-");
            if (orderStatus.equals("Canceled")) {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.progreesbarred)));
            } else {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.progreesbargreen)));

            }

           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("  EEE,dd MMM yyyy hh:mm aa");



            deliveryStatus.setText(orderStatus + String.valueOf(simpleDateFormat.format(date)));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ordersdetailsIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    ordersdetailsIntent.putExtra("Position", position);
                    itemView.getContext().startActivity(ordersdetailsIntent);
                }
            });
//            // /rating layout
//            setRating(rating);
//            for (int x = 0; x < ratenowcontainer.getChildCount(); x++) {
//                final int startPosition = x;
//                ratenowcontainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setRating(startPosition);
//
//                        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Products").document(productId);
//
//                        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
//                            @Nullable
//                            @Override
//                            public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                                DocumentSnapshot documentSnapshot = transaction.get(documentReference);
//
//                                if (rating != 0) {
//                                    Long increase = documentSnapshot.getLong(startPosition + 1 + "_star") + 1;
//                                    Long decrease = documentSnapshot.getLong(rating + 1 + "_star") - 1;
//                                    transaction.update(documentReference, startPosition + 1 + "_star", increase);
//                                    transaction.update(documentReference, rating + 1 + "_star", decrease);
//                                } else {
//                                    Long increase = documentSnapshot.getLong(startPosition + 1 + "_star") + 1;
//                                    transaction.update(documentReference, startPosition + 1 + "_star", increase);
//
//                                }
//
//                                return null;
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<Object>() {
//                            @Override
//                            public void onSuccess(Object o) {
//
//
//                                final Map<String, Object> myrating = new HashMap<>();
//
//                                if (DBqueries.myratedIds.contains(productId)) {
//
//                                    myrating.put("rating_" + DBqueries.myratedIds.indexOf(productId), (long) startPosition + 1);
//                                } else {
//                                    myrating.put("list_size", (long) DBqueries.myratedIds.size() + 1);
//                                    myrating.put("product_ID_" + DBqueries.myratedIds.size(), productId);
//                                    myrating.put("rating_" + DBqueries.myratedIds.size(), (long) startPosition + 1);
//                                }
//
//
//                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
//                                        .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            DBqueries.myOrderitemModelList.get(position).setRating(startPosition + 1);
//                                            if (DBqueries.myratedIds.contains(productId)) {
//                                                DBqueries.myrating.set(DBqueries.myratedIds.indexOf(productId), Long.parseLong(String.valueOf(startPosition + 1)));
//                                            } else {
//                                                DBqueries.myratedIds.add(productId);
//                                                DBqueries.myrating.add(Long.parseLong(String.valueOf(startPosition + 1)));
//                                            }
//                                        } else {
//                                            String error = task.getException().getMessage();
//                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    }
//                                });
//
//
//                            }
//                        });
//                    }
//                });
//            }
//            // /rating layout
        }

//        private void setRating(int startPosition) {
//            for (int x = 0; x < ratenowcontainer.getChildCount(); x++) {
//                ImageView startbtn = (ImageView) ratenowcontainer.getChildAt(x);
//                startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#888888")));
//                if (x <= startPosition) {
//                    startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFB622")));
//
//                }
//            }
//        }

    }
}
