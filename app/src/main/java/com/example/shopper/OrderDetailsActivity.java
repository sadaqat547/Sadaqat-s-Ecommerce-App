package com.example.shopper;


import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {
    private int position;
    private TextView productTitle;
    private TextView productPrice;
    private TextView productQty;
    private ImageView productImage, orderedIndicator, packedIndicator, ShippedIndicator, DeliveredIndicator;
    private ProgressBar o_p_progressbar, p_s_progress, s_d_progress;
    private TextView orderedtitle, packedtitle, shippedtitle, deliveredtitle;
    private TextView ordereddate, packeddate, shippeddate, delivereddate;
    private TextView orderedbody, packedbody, shippedbody, deliveredbody;
    private TextView address, fullName, pincode;
    private TextView totalItems, deliveryPrice, totalAmount, totalItemPrice, savedamount;
    private Button changeaddressbtn;
    private BottomSheetDialog loadindDialog;
    private SimpleDateFormat simpleDateFormat;
    private Button ordercancelbtn;
    private BottomSheetDialog cancelDialog;
    private Button no, yes;
    private TextView surityText;

    private int rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ////loading dialog

        loadindDialog = new BottomSheetDialog((OrderDetailsActivity.this));
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog

        //cancel dialog

        cancelDialog = new BottomSheetDialog((OrderDetailsActivity.this));
        cancelDialog.setContentView(R.layout.order_cancel_dialog);
        cancelDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        cancelDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////cancel dialog


        productTitle = findViewById(R.id.product_title);
        productPrice = findViewById(R.id.product_price);
        productQty = findViewById(R.id.product_quantity);
        productImage = findViewById(R.id.product_image);
        orderedIndicator = findViewById(R.id.ordered_indicator);
        packedIndicator = findViewById(R.id.packed_indicator);
        ShippedIndicator = findViewById(R.id.shipping_indicator);
        DeliveredIndicator = findViewById(R.id.delivered_indicator);
        o_p_progressbar = findViewById(R.id.ordereddd_packedd_progress);
        p_s_progress = findViewById(R.id.packed_shippingg_progress);
        s_d_progress = findViewById(R.id.shipping_deliveredd_progress);
        orderedtitle = findViewById(R.id.orderd_title);
        packedtitle = findViewById(R.id.packed_title);
        shippedtitle = findViewById(R.id.shipped_title);
        deliveredtitle = findViewById(R.id.delivered_title);
        ordereddate = findViewById(R.id.ordered_date);
        packeddate = findViewById(R.id.packed_date);
        shippeddate = findViewById(R.id.shipped_date);
        delivereddate = findViewById(R.id.delivered_date);
        orderedbody = findViewById(R.id.ordered_body);
        packedbody = findViewById(R.id.packed_body);
        shippedbody = findViewById(R.id.shipped_body);
        deliveredbody = findViewById(R.id.delivered_body);
        fullName = findViewById(R.id.full_name);
        pincode = findViewById(R.id.pincodesss);
        address = findViewById(R.id.address);
        deliveryPrice = findViewById(R.id.delivery_price);
        totalAmount = findViewById(R.id.total_price);
        totalItemPrice = findViewById(R.id.total_items_price);
        totalItems = findViewById(R.id.totsl_items);
        savedamount = findViewById(R.id.saved_amount);
        changeaddressbtn = findViewById(R.id.change_or_add_address_btn);
        ordercancelbtn = findViewById(R.id.cancel_btn);
        no = findViewById(R.id.noBtn);
        yes = findViewById(R.id.yesBtn);
        surityText = findViewById(R.id.suritytext);

        changeaddressbtn.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Orders details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        position = getIntent().getIntExtra("Position", -1);
        final MyOrderitemModel model = DBqueries.myOrderitemModelList.get(position);


        productTitle.setText(model.getProductTitle());
        if (!model.getDiscountedPrice().equals("")) {
            productPrice.setText("Rs." + model.getDiscountedPrice() + "/-");
        } else {
            productPrice.setText("Rs." + model.getProductPrice() + "/-");

        }
        productQty.setText("Qty : " + String.valueOf(model.getProductQty()));
        Glide.with(this).load(model.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.slider_background_light)).into(productImage);

        simpleDateFormat = new SimpleDateFormat("  EEE,dd MMM yyyy hh:mm aa");
        switch (model.getOrderStatus()) {
            case "Ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                ordereddate.setText(String.valueOf(simpleDateFormat.format(model.getOrdeedrDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                packedbody.setText("Not Packed your Order yet. You should come back in few hours. ");
                packedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                packedtitle.setText("Not Packed");
                packedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                packeddate.setVisibility(View.GONE);
                p_s_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                p_s_progress.setVisibility(View.VISIBLE);
                p_s_progress.setProgress(100);
                o_p_progressbar.setVisibility(View.VISIBLE);
                o_p_progressbar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));


                o_p_progressbar.setProgress(100);
                s_d_progress.setProgress(100);

                ShippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                shippedbody.setText("Not Shipped your Order yet. You should come back in few days. ");
                shippedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                shippedtitle.setText("Not Shipped");
                shippedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                shippeddate.setVisibility(View.GONE);

                s_d_progress.setVisibility(View.VISIBLE);
                s_d_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));

                DeliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                deliveredbody.setText("Not Delivered your Order yet. You should come back in few days. ");
                deliveredbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                deliveredtitle.setText("Not Delivered");
                deliveredtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                delivereddate.setVisibility(View.GONE);


                break;
            case "Packed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                ordereddate.setText(String.valueOf(simpleDateFormat.format(model.getOrdeedrDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                packedbody.setText("Your order has been packed suceessfully!");
                packedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                packedtitle.setText("Packed");
                packedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                packeddate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                p_s_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));

                p_s_progress.setVisibility(View.VISIBLE);
                p_s_progress.setProgress(100);
                o_p_progressbar.setVisibility(View.VISIBLE);

                o_p_progressbar.setProgress(100);

                ShippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                shippedbody.setText("Not Shipped your Order yet. You should come back in few days. ");
                shippedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                shippedtitle.setText("Not Shipped");
                shippedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                shippeddate.setVisibility(View.GONE);
                s_d_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));

                s_d_progress.setVisibility(View.VISIBLE);
                s_d_progress.setProgress(100);


                DeliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                deliveredbody.setText("Not Delivered your Order yet. You should come back in few days. ");
                deliveredbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                deliveredtitle.setText("Not Delivered");
                deliveredtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                delivereddate.setVisibility(View.GONE);
                break;
            case "Shipped":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                ordereddate.setText(String.valueOf(simpleDateFormat.format(model.getOrdeedrDate())));


                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                packedbody.setText("Your order has been packed suceessfully!");
                packedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                packedtitle.setText("Packed");
                packedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                packeddate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                p_s_progress.setVisibility(View.VISIBLE);
                p_s_progress.setProgress(100);
                o_p_progressbar.setVisibility(View.VISIBLE);
                o_p_progressbar.setProgress(100);

                ShippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                shippedbody.setText("Your order has been shipped suceessfully!");
                shippedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                shippedtitle.setText("Shipped");
                shippedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                shippeddate.setText(String.valueOf(simpleDateFormat.format(model.getShippeddate())));
                s_d_progress.setVisibility(View.VISIBLE);
                s_d_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));

                DeliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                deliveredbody.setText("Not Delivered your Order yet. You should come back in few days. ");
                deliveredbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                deliveredtitle.setText("Not Delivered");
                deliveredtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                delivereddate.setVisibility(View.GONE);
                break;

            case "Out for Delivery":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                ordereddate.setText(String.valueOf(simpleDateFormat.format(model.getOrdeedrDate())));


                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                packedbody.setText("Your order has been packed suceessfully!");
                packedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                packedtitle.setText("Packed");
                packedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                packeddate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));


                o_p_progressbar.setVisibility(View.VISIBLE);
                o_p_progressbar.setProgress(100);

                ShippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                shippedbody.setText("Your order has been shipped suceessfully!");
                shippedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                shippedtitle.setText("Shipped");
                shippedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                shippeddate.setText(String.valueOf(simpleDateFormat.format(model.getShippeddate())));


                s_d_progress.setVisibility(View.VISIBLE);
                s_d_progress.setProgress(100);


                DeliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                deliveredbody.setText("Your Order is out for deliver successfully!");
                deliveredbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                deliveredtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                delivereddate.setText(String.valueOf(simpleDateFormat.format(model.getDeliveredDate())));

                deliveredtitle.setText("Out For Delivery");

                break;
            case "Delivered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                ordereddate.setText(String.valueOf(simpleDateFormat.format(model.getOrdeedrDate())));


                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                packedbody.setText("Your order has been packed suceessfully!");
                packedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                packedtitle.setText("Packed");
                packedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                packeddate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));


                o_p_progressbar.setVisibility(View.VISIBLE);
                o_p_progressbar.setProgress(100);

                ShippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                shippedbody.setText("Your order has been shipped suceessfully!");
                shippedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                shippedtitle.setText("Shipped");
                shippedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                shippeddate.setText(String.valueOf(simpleDateFormat.format(model.getShippeddate())));


                s_d_progress.setVisibility(View.VISIBLE);
                s_d_progress.setProgress(100);


                DeliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                deliveredbody.setText("Your order has been delivered suceessfully!");
                deliveredbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                deliveredtitle.setText("Delivered");
                deliveredtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                delivereddate.setText(String.valueOf(simpleDateFormat.format(model.getDeliveredDate())));

                break;
            case "Cancelled":

                if (model.getPackedDate().after(model.getOrdeedrDate())) {
                    if (model.getShippeddate().after(model.getPackedDate())) {
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                        ordereddate.setText(String.valueOf(simpleDateFormat.format(model.getOrdeedrDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                        packedbody.setText("Your order has been packed suceessfully!");
                        packedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                        packedtitle.setText("Packed");
                        packedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                        packeddate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        p_s_progress.setVisibility(View.VISIBLE);
                        p_s_progress.setProgress(100);
                        o_p_progressbar.setVisibility(View.VISIBLE);
                        o_p_progressbar.setProgress(100);

                        ShippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                        shippedbody.setText("Your order has been shipped suceessfully!");
                        shippedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                        shippedtitle.setText("Shipped");
                        shippedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                        shippeddate.setText(String.valueOf(simpleDateFormat.format(model.getShippeddate())));
                        s_d_progress.setVisibility(View.VISIBLE);
                        s_d_progress.setProgress(100);


                        DeliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                        deliveredbody.setText("Your order has been cancelled suceessfully!");
                        deliveredbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                        deliveredtitle.setText("Cancelled");
                        deliveredtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                        delivereddate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));

                    } else {
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                        ordereddate.setText(String.valueOf(simpleDateFormat.format(model.getOrdeedrDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                        packedbody.setText("Your order has been packed suceessfully!");
                        packedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                        packedtitle.setText("Packed");
                        packedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                        packeddate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        p_s_progress.setVisibility(View.VISIBLE);
                        p_s_progress.setProgress(100);
                        p_s_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                        o_p_progressbar.setVisibility(View.VISIBLE);
                        o_p_progressbar.setProgress(100);

                        ShippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                        shippedbody.setText("Your order has been cancelled suceessfully!");
                        shippedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                        shippedtitle.setText("Cancelled");
                        shippedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                        shippeddate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                        s_d_progress.setVisibility(View.VISIBLE);
                        s_d_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                        s_d_progress.setProgress(100);


                        DeliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                        deliveredbody.setText("Order Cancelled");
                        deliveredbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                        deliveredtitle.setText("Order Cancelled");
                        deliveredtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                        delivereddate.setVisibility(View.GONE);

                    }


                } else {
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbargreen)));
                    ordereddate.setText(String.valueOf(simpleDateFormat.format(model.getOrdeedrDate())));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                    packedbody.setText("Your order has been cancelled suceessfully!");
                    packedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                    packedtitle.setText("Cancelled");
                    packedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                    packeddate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));

                    p_s_progress.setVisibility(View.VISIBLE);
                    p_s_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                    p_s_progress.setProgress(100);
                    o_p_progressbar.setVisibility(View.VISIBLE);
                    o_p_progressbar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                    o_p_progressbar.setProgress(100);

                    ShippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                    shippedbody.setText("Order Cancelled");
                    shippedbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                    shippedtitle.setText("Order Cancelled");
                    shippedtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                    shippeddate.setVisibility(View.GONE);
                    s_d_progress.setVisibility(View.VISIBLE);
                    s_d_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                    s_d_progress.setProgress(100);


                    DeliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                    deliveredbody.setText("Order Cancelled");
                    deliveredbody.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                    deliveredtitle.setText("Order Cancelled");
                    deliveredtitle.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.halkablack)));
                    delivereddate.setVisibility(View.GONE);

                }
                break;
            default:
//                date = model.getCancelledDate();
        }

//        // /rating layout
//        rating = model.getRating();
//        setRating(rating);
//        for (int x = 0; x < rateNowcontainer.getChildCount(); x++) {
//            final int startPosition = x;
//            rateNowcontainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    setRating(startPosition);
//
//                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Products").document(model.getProductId());
//
//                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
//                        @Nullable
//                        @Override
//                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
//
//                            if (rating != 0) {
//                                Long increase = documentSnapshot.getLong(startPosition+1 + "_star") + 1;
//                                Long decrease = documentSnapshot.getLong(rating + 1+"_star") - 1;
//                                transaction.update(documentReference, startPosition+1 + "_star", increase);
//                                transaction.update(documentReference, rating +1+ "_star", decrease);
//                            } else {
//                                Long increase = documentSnapshot.getLong(startPosition+1 + "_star") + 1;
//                                transaction.update(documentReference, startPosition + 1+"_star", increase);
//
//                            }
//
//                            return null;
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
//                        @Override
//                        public void onSuccess(Object o) {
//                            final Map<String, Object> myrating = new HashMap<>();
//
//                            if (DBqueries.myratedIds.contains(model.getProductId())) {
//
//                                myrating.put("rating_" + DBqueries.myratedIds.indexOf(model.getProductId()), (long) startPosition + 1);
//                            } else {
//                                myrating.put("list_size", (long) DBqueries.myratedIds.size() + 1);
//                                myrating.put("product_ID_" + DBqueries.myratedIds.size(), model.getProductId());
//                                myrating.put("rating_" + DBqueries.myratedIds.size(), (long) startPosition + 1);
//                            }
//
//
//                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
//                                    .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        DBqueries.myOrderitemModelList.get(position).setRating(startPosition + 1);
//                                        if (DBqueries.myratedIds.contains(model.getProductId())) {
//                                            DBqueries.myrating.set(DBqueries.myratedIds.indexOf(model.getProductId()), Long.parseLong(String.valueOf(startPosition + 1)));
//                                        } else {
//                                            DBqueries.myratedIds.add(model.getProductId());
//                                            DBqueries.myrating.add(Long.parseLong(String.valueOf(startPosition + 1)));
//                                        }
//                                    } else {
//                                        String error = task.getException().getMessage();
//                                        Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }
//                            });
//                        }
//                    });
//                }
//            });
//        }
//        // /rating layout

        final String orderid = model.getOrderId();
        if (model.getCancellationRequested()) {
            ordercancelbtn.setVisibility(View.VISIBLE);
            ordercancelbtn.setEnabled(false);
            ordercancelbtn.setText("Cancellation In Process");
            ordercancelbtn.setTextColor(getResources().getColor(R.color.progreesbarred));
            ordercancelbtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
        } else {
            if (model.getOrderStatus().equals("Ordered") || model.getOrderStatus().equals("Packed")) {
                ordercancelbtn.setVisibility(View.VISIBLE);

                ordercancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelDialog.show();
                        cancelDialog.findViewById(R.id.noBtn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDialog.dismiss();
                            }
                        });
                        cancelDialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDialog.dismiss();
                                loadindDialog.show();

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("ORDER ID", orderid);
                                    map.put("Product ID", model.getProductId());
                                    map.put("Order Cancelled", true);

                                    FirebaseFirestore.getInstance().collection("CANCELLED ORDERS").document().set(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        FirebaseFirestore.getInstance().collection("ORDERS").document(orderid).collection("OrderItems").document(model.getProductId()).update("Cancellation requested",true)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            model.setCancellationRequested(true);
                                                                            ordercancelbtn.setVisibility(View.VISIBLE);
                                                                            ordercancelbtn.setEnabled(false);
                                                                            ordercancelbtn.setText("Cancellation In Process");
                                                                            ordercancelbtn.setTextColor(getResources().getColor(R.color.progreesbarred));
                                                                            ordercancelbtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.progreesbarred)));
                                                                        }else{
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                                                                        }
                                                                        loadindDialog.dismiss();
                                                                    }
                                                                });
                                                    }else{
                                                        loadindDialog.dismiss();
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });

                            }
                        });
                        cancelDialog.show();
                    }
                });

            }
        }
        fullName.setText(model.getFullname());
        pincode.setText(model.getPincode());
        address.setText(model.getAddress());


        totalItems.setText("Price(" + model.getProductQty() + " items)");
        Long totalitemsPriceValue;
        if (model.getDiscountedPrice().equals("")) {
            totalitemsPriceValue = Long.valueOf(model.getProductPrice()) * model.getProductQty();
            totalItemPrice.setText("Rs." + totalitemsPriceValue + "/-");

        } else {
            totalitemsPriceValue = Long.valueOf(model.getDiscountedPrice()) * model.getProductQty();
            totalItemPrice.setText("Rs." + totalitemsPriceValue + "/-");
        }
        if (model.getDeliveryPrice().equals("Free")) {
            deliveryPrice.setText(model.getDeliveryPrice());
            totalAmount.setText(totalItemPrice.getText());

        } else {
            deliveryPrice.setText("Rs." + model.getDeliveryPrice() + "/-");

            totalAmount.setText("Rs." + (totalitemsPriceValue) + "/-");

        }
        if (!model.getCuttedPrice().equals("")) {
            if (!model.getDiscountedPrice().equals("")) {
                savedamount.setText("You saved Rs." + model.getProductQty() * (Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getDiscountedPrice())) + "/- on this order.");
            } else {
                savedamount.setText("You saved Rs." + model.getProductQty() * (Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getProductPrice())) + "/- on this order.");

            }
        } else {
            if (!model.getDiscountedPrice().equals("")) {
                savedamount.setText("You saved Rs." + model.getProductQty() * (Long.valueOf(model.getProductPrice()) - Long.valueOf(model.getDiscountedPrice())) + "/- on this order.");
            } else {
                savedamount.setText("You saved Rs.0/- on this order.");

            }
        }

    }

    //    private void setRating(int startPosition) {
//        for (int x = 0; x < rateNowcontainer.getChildCount(); x++) {
//            ImageView startbtn = (ImageView) rateNowcontainer.getChildAt(x);
//            startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#888888")));
//            if (x <= startPosition) {
//                startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFB622")));
//
//            }
//        }
//    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}