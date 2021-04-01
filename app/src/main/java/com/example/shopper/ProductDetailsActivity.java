package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.shopper.RegisterActivity.setSignupFragment;

public class ProductDetailsActivity extends AppCompatActivity {
    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;
    public static Activity productDetailsActivity;

    public static boolean fromSearch = false;
    private String productOriginalPrice;
    private ViewPager productImagesViewpager;
    private TabLayout viewPagerIndicator;
    private Button buynowbtn;
    private LinearLayout addtocart;
    private TextView averageRatingMiniView;

    AlertDialog.Builder builder1;
    private TextView badgeCount;

    private static int currentFragment = -1;

    private LinearLayout coupenlinearlayout;
    ///////product dezcription

    private ConstraintLayout productdetailsonlycontainer;
    private ConstraintLayout productdetailstabscontainer;
    private TabLayout productDetailsTablayout;
    private ViewPager productDetailsViewPager;
    ////product dezcription

    public static Boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static Boolean ALREADY_ADDED_TO_CART = false;
    public static FloatingActionButton addtowishlistbtn;
    public static FloatingActionButton idcopier;
    private ImageView productdetailscart;
    private LinearLayout addtoCartBtn;
    private Button coupenRedeemBtn;
    private TextView producttitle,viewAllthreeComments;
    private TextView totalratingsminiview;
    private TextView productprice;
    private TextView cuttedprice;
    private ImageView codIndicator;
    private TextView tv_codindicator;
    private TextView reward_title;
    private TextView reward_body;
    private TextView productsDescriptionOnlyBody;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();

    private BottomSheetDialog signindialog;
    private BottomSheetDialog loadindDialog;
    private DocumentSnapshot documentSnapshot;

    ////coupendialog
    private TextView coupenTitle;
    private TextView coupenExpiryDate;
    private TextView coupenBody;
    private RecyclerView coupensRecyclerview;
    private LinearLayout selectedCoupen;
    private TextView discountedPrice;
    private TextView originalPrice;
    ////coupendialog

    ///rating
    public static int initialrating;
    public static LinearLayout ratenowlayout;
    private TextView totalRatings;
    private TextView averageRating;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsNoContainer;
    private LinearLayout ratings_progressbar_container;
    ///rating
    private boolean inStock = false;


    private FirebaseFirestore firebaseFirestore;
    private String productDescription;
    private String productotherDetails;
    private FirebaseUser currentUser;
    public static String productid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coupenlinearlayout = findViewById(R.id.cupon_redemtion_layout);
        addtocart = findViewById(R.id.add_to_cart_btn);
        viewAllthreeComments = findViewById(R.id.viewAllthreeComments);
        codIndicator = findViewById(R.id.cod_indicator_imageview);
        tv_codindicator = findViewById(R.id.cod_indicator_textview);
        reward_title = findViewById(R.id.reward_title);
        reward_body = findViewById(R.id.reward_body);
        productsDescriptionOnlyBody = findViewById(R.id.product_details_only_body);
        ratings_progressbar_container = findViewById(R.id.ratings_progressbar_container);

        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);
        productImagesViewpager = findViewById(R.id.products_image_viewpager);
        totalRatings = findViewById(R.id.total_ratiings);
        productprice = findViewById(R.id.product_price);
        cuttedprice = findViewById(R.id.cutted_price);
        totalratingsminiview = findViewById(R.id.totla_ratings_minilist);
        producttitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.rating);
        addtoCartBtn = findViewById(R.id.add_to_cart_btn);
        buynowbtn = findViewById(R.id.buy_now_btn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        coupenRedeemBtn = findViewById(R.id.couponredemption_btn);
        averageRating = findViewById(R.id.avarege_rating);
        viewPagerIndicator = findViewById(R.id.viewPager_indicator);
        addtowishlistbtn = findViewById(R.id.add_to_wishlist_btn);
        idcopier = findViewById(R.id.copier);
        productDetailsViewPager = findViewById(R.id.products_details_viewpager);
        productDetailsTablayout = findViewById(R.id.Product_details_tabLayout);
        productdetailsonlycontainer = findViewById(R.id.product_detils_container);
        productdetailstabscontainer = findViewById(R.id.product_details_tab_container);
        initialrating = -1;

        ////loading dialog

        loadindDialog = new BottomSheetDialog(ProductDetailsActivity.this);
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadindDialog.show();
        ////loading dialog
//        getComments(productid,viewAllthreeComments);


        //coupen dialog
        final Dialog checkCoupenPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView toggleRecyclerview = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
        coupensRecyclerview = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validaty);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);
        originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
        discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        coupensRecyclerview.setLayoutManager(layoutManager);


        toggleRecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetailsActivity.this, "Make sure you applying coupen in your cart", Toast.LENGTH_SHORT).show();
            }
        });
        //coupendialog


        final List<String> productImages = new ArrayList<>();
        productid = getIntent().getStringExtra("product_ID");
        firebaseFirestore.collection("PRODUCTS").document(productid).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            documentSnapshot = task.getResult();
                            firebaseFirestore.collection("PRODUCTS").document(productid).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (long x = 1; x < (long) documentSnapshot.get("no_of_products_images") + 1; x++) {
                                            productImages.add(documentSnapshot.get("product_image_" + x).toString());
                                        }
                                        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                                        productImagesViewpager.setAdapter(productImagesAdapter);


                                        producttitle.setText(documentSnapshot.get("product_title").toString());
                                        averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                                        totalratingsminiview.setText("(" + (long) documentSnapshot.get("total_rating") + ")ratings");
                                        productprice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                                        /////for coupen dialog
                                        productOriginalPrice = documentSnapshot.get("product_price").toString();
                                        originalPrice.setText(productprice.getText());
                                        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(DBqueries.rewardModelList, true, coupensRecyclerview, selectedCoupen, productOriginalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice);
                                        coupensRecyclerview.setAdapter(myRewardsAdapter);
                                        myRewardsAdapter.notifyDataSetChanged();
                                        /////for coupen dialog

                                        cuttedprice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");

                                        if ((boolean) documentSnapshot.get("COD")) {
                                            codIndicator.setVisibility(View.VISIBLE);
                                            tv_codindicator.setVisibility(View.VISIBLE);
                                        } else {
                                            codIndicator.setVisibility(View.INVISIBLE);
                                            tv_codindicator.setVisibility(View.INVISIBLE);

                                        }
                                        reward_title.setText((long) documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupens_title").toString());
                                        reward_body.setText(documentSnapshot.get("free_coupens_body").toString());


                                        if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                            productdetailstabscontainer.setVisibility(View.VISIBLE);
                                            productdetailsonlycontainer.setVisibility(View.GONE);
                                            productDescription = documentSnapshot.get("product_description").toString();
                                            ProductSpecificationFragment.productSpecificationModelList = new ArrayList<>();
                                            productotherDetails = documentSnapshot.get("product_other_details").toString();
                                            for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                                ProductSpecificationFragment.productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                                                for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                                    ProductSpecificationFragment.productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));

                                                }
                                            }
                                        } else {
                                            productdetailstabscontainer.setVisibility(View.GONE);
                                            productdetailsonlycontainer.setVisibility(View.VISIBLE);
                                            productsDescriptionOnlyBody.setText(documentSnapshot.get("product_description").toString());

                                        }

                                        totalRatings.setText((long) documentSnapshot.get("total_rating") + " ratings");

                                        for (int x = 0; x < 5; x++) {
                                            TextView ratings = (TextView) ratingsNoContainer.getChildAt(x);
                                            ratings.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                                            ProgressBar progressBar = (ProgressBar) ratings_progressbar_container.getChildAt(x);
                                            int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_rating")));
                                            progressBar.setMax(maxProgress);
                                            progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                                        }
                                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_rating")));
                                        averageRating.setText(documentSnapshot.get("average_rating").toString());

                                        productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTablayout.getTabCount(), productDescription, productotherDetails, productSpecificationModelList));

                                        if (currentUser != null) {
                                            if (DBqueries.myrating.size() == 0) {
                                                DBqueries.loadratingList(ProductDetailsActivity.this);
                                            }
                                            if (DBqueries.cartlist.size() == 0) {
                                                DBqueries.loadCartList(ProductDetailsActivity.this, loadindDialog, false, new TextView(ProductDetailsActivity.this));
                                            }

                                            if (DBqueries.wishlist.size() == 0) {
                                                DBqueries.loadWishlist(ProductDetailsActivity.this, loadindDialog, false);
                                            } else {
                                                loadindDialog.dismiss();
                                            }
                                            if (DBqueries.rewardModelList.size() == 0) {
                                                DBqueries.loadRewards(ProductDetailsActivity.this, loadindDialog, false);
                                            }
                                            if (DBqueries.cartlist.size() != 0 && DBqueries.wishlist.size() != 0 && DBqueries.rewardModelList.size() != 0) {
                                                loadindDialog.dismiss();
                                            }

                                        } else {
                                            loadindDialog.dismiss();
                                        }
                                        if (DBqueries.myratedIds.contains(productid)) {
                                            int index = DBqueries.myratedIds.indexOf(productid);
                                            initialrating = Integer.parseInt(String.valueOf(DBqueries.myrating.get(index))) - 1;
                                            setRating(initialrating);
                                        }
                                        if (DBqueries.cartlist.contains(productid)) {
                                            ALREADY_ADDED_TO_CART = true;

                                        } else {

                                            ALREADY_ADDED_TO_CART = false;
                                        }

                                        if (DBqueries.wishlist.contains(productid)) {

                                            ALREADY_ADDED_TO_WISHLIST = true;
                                            addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.progreesbarred));

                                        } else {
                                            addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#C3C3C3")));

                                            ALREADY_ADDED_TO_WISHLIST = false;
                                        }
                                        if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                            inStock = true;
                                            buynowbtn.setVisibility(View.VISIBLE);
                                            addtocart.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (currentUser == null) {
                                                        signindialog.show();
                                                    } else {
                                                        if (!running_cart_query) {
                                                            running_cart_query = true;
                                                            if (ALREADY_ADDED_TO_CART) {
                                                                running_cart_query = false;
                                                                Toast.makeText(ProductDetailsActivity.this, "Already added to cart!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Map<String, Object> addProduct = new HashMap<>();
                                                                addProduct.put("product_ID_" + String.valueOf(DBqueries.cartlist.size()), productid);
                                                                addProduct.put("list_size", (long) (DBqueries.cartlist.size() + 1));

                                                                firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                        .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            if (DBqueries.cartItemModelList.size() != 0) {
                                                                                DBqueries.cartItemModelList.add(0, new CartItemModel(CartItemModel.CART_ITEM, productid, documentSnapshot.get("product_image_1").toString()
                                                                                        , documentSnapshot.get("product_full_title").toString()
                                                                                        , (long) documentSnapshot.get("free_coupens")
                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                        , (long) 1
                                                                                        , (long) documentSnapshot.get("offers_applied")
                                                                                        , (long) 0
                                                                                        , inStock
                                                                                        , (long) documentSnapshot.get("max_quantity")
                                                                                        , (long) documentSnapshot.get("stock_quantity")
                                                                                        , (boolean) documentSnapshot.get("COD")

                                                                                ));
                                                                            }
                                                                            ALREADY_ADDED_TO_CART = true;
                                                                            DBqueries.cartlist.add(productid);
                                                                            Toast.makeText(ProductDetailsActivity.this, "Added to Cart successfully!", Toast.LENGTH_SHORT).show();
                                                                            invalidateOptionsMenu();
                                                                            running_cart_query = false;
                                                                        } else {
//                                        addtowishlistbtn.setEnabled(true);
                                                                            running_cart_query = false;

                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                        } else {
                                            inStock = false;
                                            buynowbtn.setVisibility(View.GONE);
                                            TextView outOfStock = (TextView) addtocart.getChildAt(0);
                                            outOfStock.setText("Sorry! Out of Stock");
                                            outOfStock.setTextColor(getResources().getColor(R.color.progreesbarred));
                                            outOfStock.setCompoundDrawables(null, null, null, null);
                                        }
                                    } else {
                                        ///errror
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            loadindDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        viewPagerIndicator.setupWithViewPager(productImagesViewpager, true);


        buynowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signindialog.show();
                } else {
                    DeliveryActivity.fromCart = false;
                    loadindDialog.show();
                    productDetailsActivity = ProductDetailsActivity.this;
                    DeliveryActivity.cartItemModelList = new ArrayList<>();

                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM, productid, documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_full_title").toString()
                            , (long) documentSnapshot.get("free_coupens")
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (long) 1
                            , (long) documentSnapshot.get("offers_applied")
                            , (long) 0
                            , inStock
                            , (long) documentSnapshot.get("max_quantity")
                            , (long) documentSnapshot.get("stock_quantity")
                            , (boolean) documentSnapshot.get("COD")
                    ));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));


                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAddresses(ProductDetailsActivity.this, loadindDialog, true);
                    } else {
                        loadindDialog.dismiss();
                        Intent deliveryintent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryintent);
                    }
                }
            }
        });

        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkCoupenPriceDialog.show();
            }

        });

        idcopier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", productid);
                clipboardManager.setPrimaryClip(clip);
                clip.getDescription();
                Toast.makeText(ProductDetailsActivity.this, "Link to Product is copied to clipBoard", Toast.LENGTH_LONG).show();
            }
        });

        addtowishlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser == null) {
                    signindialog.show();
                } else {

                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {

                            int index = DBqueries.wishlist.indexOf(productid);
                            DBqueries.removefromWishlist(index, ProductDetailsActivity.this);
                            addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#C3C3C3")));

                        } else {
                            addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.progreesbarred));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishlist.size()), productid);
                            addProduct.put("list_size", (long) (DBqueries.wishlist.size() + 1));

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        if (DBqueries.wishlistModelList.size() != 0) {
                                            DBqueries.wishlistModelList.add(new WishlistModel(productid, documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_full_title").toString()
                                                    , (long) documentSnapshot.get("free_coupens")
                                                    , documentSnapshot.get("average_rating").toString()
                                                    , (long) documentSnapshot.get("total_rating")
                                                    , documentSnapshot.get("product_price").toString()
                                                    , documentSnapshot.get("cutted_price").toString()
                                                    , (boolean) documentSnapshot.get("COD")
                                                    , inStock));
                                        }
                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        Toast.makeText(ProductDetailsActivity.this, "Added to Wishlist successfully!", Toast.LENGTH_SHORT).show();
                                        addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.progreesbarred));
                                        DBqueries.wishlist.add(productid);


//
                                    } else {
                                        addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.progreesbarred));
//                                        addtowishlistbtn.setEnabled(true);


                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }

                                    running_wishlist_query = false;

//                                    ///notification

//                                                                            Date currentTime = Calendar.getInstance().getTime();
//                                    Map<String, Object> addnotification = new HashMap<>();
//                                    addnotification.put("list_size", (int) (DBqueries.notificationList.size() + 1));
//                                    addnotification.put("Image_" + x, documentSnapshot.get("product_image_1"));
//                                    addnotification.put("Icon_" +x, documentSnapshot.get("product_image_1"));
//                                    addnotification.put("body_" +x, documentSnapshot.get("product_full_title").toString() + "has been added to wishlist");
//                                    addnotification.put("readed_" +x, false);
//                                    addnotification.put("Date_" + x,currentTime );
//
//                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_NOTIFICATIONS")
//                                            .set(addnotification).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//
//                                                Toast.makeText(ProductDetailsActivity.this, "New Notification arrived", Toast.LENGTH_SHORT).show();
//
////
//                                            } else {
//
//
//                                                String error = task.getException().getMessage();
//                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });

                                }
                            });

                        }
                    }
                }
            }
        });

        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ///rating/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ratenowlayout =

                findViewById(R.id.rate_now_container);
        for (
                int x = 0; x < ratenowlayout.getChildCount(); x++) {
            final int starposition = x;
//            final int finalX = x;
            ratenowlayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser == null) {
                        signindialog.show();
                    } else {
                        if (!running_rating_query) {
                            running_rating_query = true;
                            setRating(starposition);
                            Map<String, Object> updaterating = new HashMap<>();


                            if (DBqueries.myratedIds.contains(productid)) {

                                TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialrating - 1);
                                TextView finalRatings = (TextView) ratingsNoContainer.getChildAt(5 - starposition - 1);


                                updaterating.put(initialrating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                updaterating.put(starposition + 1 + "_star", Long.parseLong(finalRatings.getText().toString()) + 1);
                                updaterating.put("average_rating", calculateAverageRating((long) starposition - initialrating, true));
                            } else {
                                updaterating.put(starposition + 1 + "_star", (long) documentSnapshot.get(starposition + 1 + "_star") + 1);
                                updaterating.put("average_rating", calculateAverageRating((long) starposition, true) + 1);
                                updaterating.put("total_rating", (long) documentSnapshot.get("total_rating") + 1);
                            }
                            firebaseFirestore.collection("PRODUCTS").document(productid)
                                    .update(updaterating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        final Map<String, Object> myrating = new HashMap<>();

                                        if (DBqueries.myratedIds.contains(productid)) {

                                            myrating.put("rating_" + DBqueries.myratedIds.indexOf(productid), (long) starposition + 1);
                                        } else {
                                            myrating.put("list_size", (long) DBqueries.myratedIds.size() + 1);
                                            myrating.put("product_ID_" + DBqueries.myratedIds.size(), productid);
                                            myrating.put("rating_" + DBqueries.myratedIds.size(), (long) starposition + 1);
                                        }


                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    if (DBqueries.myratedIds.contains(productid)) {
                                                        DBqueries.myrating.set(DBqueries.myratedIds.indexOf(productid), (long) starposition + 1);

                                                        TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialrating - 1);
                                                        TextView finalRatings = (TextView) ratingsNoContainer.getChildAt(5 - starposition - 1);

                                                        oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                        finalRatings.setText(String.valueOf(Integer.parseInt(finalRatings.getText().toString()) + 1));

                                                        DBqueries.myrating.set(DBqueries.myratedIds.indexOf(productid), (long) starposition);
                                                    } else {


                                                        DBqueries.myratedIds.add(productid);
                                                        DBqueries.myrating.add((long) starposition + 1);

                                                        TextView ratings = (TextView) ratingsNoContainer.getChildAt(5 - starposition - 1);
                                                        ratings.setText(String.valueOf(Integer.parseInt(ratings.getText().toString())) + 1);
                                                        totalratingsminiview.setText("(" + ((long) documentSnapshot.get("total_rating") + 1) + ")  ratings");
                                                        totalRatings.setText((long) documentSnapshot.get("total_rating") + 1 + " ratings");
                                                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_rating") + 1));


                                                        Toast.makeText(ProductDetailsActivity.this, "Thanks a lot! for your rating!", Toast.LENGTH_SHORT).show();
                                                    }

                                                    for (int x = 0; x < 5; x++) {
                                                        TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                        ProgressBar progressBar = (ProgressBar) ratings_progressbar_container.getChildAt(x);
                                                        int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                        progressBar.setMax(maxProgress);

                                                        progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                    }
                                                    initialrating = starposition;
                                                    averageRating.setText(calculateAverageRating(0, true));
                                                    averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                    if (DBqueries.wishlist.contains(productid) && DBqueries.wishlistModelList.size() != 0) {
                                                        int index = DBqueries.wishlist.indexOf(productid);
                                                        DBqueries.wishlistModelList.get(index).setRating(averageRating.getText().toString());
                                                        DBqueries.wishlistModelList.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));


                                                    }

                                                } else {
                                                    setRating(initialrating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                running_rating_query = false;

                                            }
                                        });
                                    } else {
                                        running_rating_query = false;
                                        setRating(initialrating);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    }

                }
            });
        }
        ///rating


        //signin dialog
        signindialog = new

                BottomSheetDialog(ProductDetailsActivity.this);
        signindialog.setContentView(R.layout.sign_in_dialog);
        signindialog.setCancelable(true);

        signindialog.getWindow().

                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogsigninBtn = signindialog.findViewById(R.id.login_btn);
        Button dialogsignupBtn = signindialog.findViewById(R.id.signup_btn);
        dialogsigninBtn.setOnClickListener(new View.OnClickListener() {
            final Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);

            @Override
            public void onClick(View view) {
                SignInFragment.disableclodebtn = true;
                SignUpFragment.disableclodebtn = true;

                signindialog.dismiss();
                setSignupFragment = false;
                startActivity(registerIntent);

            }
        });

        dialogsignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment.disableclodebtn = true;
                SignInFragment.disableclodebtn = true;

                signindialog.dismiss();
                Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);
                setSignupFragment = true;
                startActivity(registerIntent);

            }
        });

        builder1 = new AlertDialog.Builder(ProductDetailsActivity.this);
        builder1.setMessage("Select type of QR");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Search by Scanning Product",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ProductDetailsActivity.this, QrScannerActivity.class);
                        startActivity(intent);
                    }
                });

        builder1.setNegativeButton(
                "Generate QrCode of this product",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ProductDetailsActivity.this, qrGeneratorActivity.class);
                        intent.putExtra("ProductID", productid);
                        startActivity(intent);
                    }
                });

///signindialog
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            coupenlinearlayout.setVisibility(View.GONE);
        } else {
            coupenlinearlayout.setVisibility(View.VISIBLE);
        }
        if (currentUser != null) {
            if (DBqueries.myrating.size() == 0) {
                DBqueries.loadratingList(ProductDetailsActivity.this);
            }
            if (DBqueries.wishlist.size() == 0) {
                DBqueries.loadWishlist(ProductDetailsActivity.this, loadindDialog, false);
            } else {
                loadindDialog.dismiss();
            }
            if (DBqueries.rewardModelList.size() == 0) {
                DBqueries.loadRewards(ProductDetailsActivity.this, loadindDialog, false);
            }
            if (DBqueries.cartlist.size() != 0 && DBqueries.wishlist.size() != 0 && DBqueries.rewardModelList.size() != 0) {
                loadindDialog.dismiss();
            }

        } else {
            loadindDialog.dismiss();
        }

        if (DBqueries.myratedIds.contains(productid)) {
            int index = DBqueries.myratedIds.indexOf(productid);
            initialrating = Integer.parseInt(String.valueOf(DBqueries.myrating.get(index))) - 1;
            setRating(initialrating);
        }


        if (DBqueries.cartlist.contains(productid)) {
            ALREADY_ADDED_TO_CART = true;

        } else {

            ALREADY_ADDED_TO_CART = false;
        }


        if (DBqueries.wishlist.contains(productid)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.progreesbarred));

        } else {
            addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#C3C3C3")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }

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

    public static void setRating(int starposition) {

        for (int x = 0; x < ratenowlayout.getChildCount(); x++) {
            ImageView starButton = (ImageView) ratenowlayout.getChildAt(x);
            starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#888888")));
            if (x <= starposition) {
                starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFB622")));
            }
        }
    }

    private String calculateAverageRating(long currentUserRating, boolean update) {

        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);
        }
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);

        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()))).substring(0, 3);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch = false;
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.qrmain) {
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void getcomments(String postId,TextView comments){}
//    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(productid);
//
    private void getComments(String productidd, final TextView viewAllthreeCommentss){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(productidd).child("-ML2COt_HK7XAy1t8e7S");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewAllthreeCommentss.setText("View all "+dataSnapshot.getChildrenCount()+" comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}