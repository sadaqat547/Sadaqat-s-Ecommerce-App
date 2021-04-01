package com.example.shopper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shopper.ProductDetailsActivity.addtowishlistbtn;
import static com.example.shopper.ProductDetailsActivity.initialrating;
import static com.example.shopper.ProductDetailsActivity.productid;


public class DBqueries {


    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesName = new ArrayList<String>();
    public static List<GridViewModel> griditemviewList = new ArrayList<>();
    public static List<String> wishlist = new ArrayList<String>();
    public static List<String> commentList = new ArrayList<String>();
    public static List<String> notificationList = new ArrayList<String>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<WishlistModel>();
    public static List<Comment> commentmodelList = new ArrayList<>();
    public static List<String> myratedIds = new ArrayList<String>();
    public static List<Long> myrating = new ArrayList<Long>();
    public static List<String> cartlist = new ArrayList<String>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<CartItemModel>();
    public static List<AddressesModel> addressesModelList = new ArrayList<AddressesModel>();
    public static List<RewardModel> rewardModelList = new ArrayList<RewardModel>();
    public static List<MyOrderitemModel> myOrderitemModelList = new ArrayList<MyOrderitemModel>();
    public static String fullname, email, profile;
    public static List<NotificationModel> notificationModelList = new ArrayList<>();
    public static ListenerRegistration registration;

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("CategoryName").toString()));
                    }
                    CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadFragmentData(final RecyclerView homePageRecyclerview, final Context context, final int index, String categoryName) {
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                    for (long x = 1; x < no_of_banners + 1; x++) {
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + x).toString()
                                                , documentSnapshot.get("banner_" + x + "_background").toString()
                                        ));
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModelList));


                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    lists.get(index).add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString(),
                                            documentSnapshot.get("background").toString(), documentSnapshot.get("product_ID").toString()));


                                } else if ((long) documentSnapshot.get("view_type") == 2) {


                                    List<WishlistModel> viewAllProductList = new ArrayList<WishlistModel>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<HorizontalProductScrollModel>();

                                    ArrayList<String> productids = (ArrayList<String>) documentSnapshot.get("products");
                                    for (String productid : productids) {

                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(productid
                                                , ""
                                                , ""
                                                , ""
                                                , ""));

                                        viewAllProductList.add(new WishlistModel(productid
                                                , ""
                                                , ""
                                                , 0
                                                , ""
                                                , 0
                                                , ""
                                                , ""
                                                , false
                                                , false));
                                    }
                                    lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList, viewAllProductList));

                                }
// else if ((long) documentSnapshot.get("view_type") == 3) {
////                                    List<HorizontalProductScrollModel> gridlayoutModelList = new ArrayList<HorizontalProductScrollModel>();
////                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
////                                    for (long x = 1; x < no_of_products + 1; x++) {
////                                        gridlayoutModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString()
////                                                , documentSnapshot.get("product_image_" + x).toString()
////                                                , documentSnapshot.get("product_title_" + x).toString()
////                                                , documentSnapshot.get("product_subtitle_" + x).toString()
////                                                , documentSnapshot.get("product_price_" + x).toString()));
////                                    }
////                                    homePageModelList.add(new HomePageModel(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), gridlayoutModelList));
//
//                                }
                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerview.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.refreshlayout.setRefreshing(false);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadWishlist(final Context getContext, final Dialog dialog, final boolean loadproductdata) {

        wishlist.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        wishlist.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBqueries.wishlist.contains(productid)) {
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                            ProductDetailsActivity.addtowishlistbtn.setSupportImageTintList(getContext.getResources().getColorStateList(R.color.progreesbarred));

                        } else {
//                            ProductDetailsActivity.addtowishlistbtn.setSupportImageTintList(getContext.getResources().getColorStateList(R.color.wishlistBlack));

                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        }
                        if (loadproductdata) {
                            wishlistModelList.clear();
                            final String productid = task.getResult().get("product_ID_" + x).toString();
                            FirebaseFirestore.getInstance().collection("PRODUCTS").document(productid)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        final DocumentSnapshot documentSnapshot = task.getResult();
                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productid).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING)
                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                                        wishlistModelList.add(new WishlistModel(ProductDetailsActivity.productid, documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_full_title").toString()
                                                                , (long) documentSnapshot.get("free_coupens")
                                                                , documentSnapshot.get("average_rating").toString()
                                                                , (long) documentSnapshot.get("total_rating")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (boolean) documentSnapshot.get("COD")
                                                                , true));
                                                    } else {
                                                        wishlistModelList.add(new WishlistModel(ProductDetailsActivity.productid, documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_full_title").toString()
                                                                , (long) documentSnapshot.get("free_coupens")
                                                                , documentSnapshot.get("average_rating").toString()
                                                                , (long) documentSnapshot.get("total_rating")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (boolean) documentSnapshot.get("COD")
                                                                , false));
                                                    }
//                                                    if (wishlist.size() == 0) {
//                                                        wishlistModelList.clear();
//                                                    }
                                                    MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();

                                                } else {
                                                    ///errror
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    } else {

                                    }
                                }
                            });
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
    public static void loadComments(final Context getContext, String postid, final String publisherid) {

        firebaseFirestore.collection("Comments").document("bs6iM5oKJx4rOigp0zQL")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    commentmodelList.add(new Comment(task.getResult().getString("Comment"),publisherid));

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void removefromWishlist(final int index, final Context getContext) {

        final String removeproductid = wishlist.get(index);

        wishlist.remove(index);
        Map<String, Object> updateWishList = new HashMap<>();
        for (int x = 0; x < wishlist.size(); x++) {
            updateWishList.put("product_ID_" + x, wishlist.get(x));

        }
        updateWishList.put("list_size", (long) wishlist.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (wishlistModelList.size() != 0) {
                        wishlistModelList.remove(index);
                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                    }
                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                    Toast.makeText(getContext, "Removed successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    if (addtowishlistbtn != null) {
                        addtowishlistbtn.setSupportImageTintList(getContext.getResources().getColorStateList(R.color.progreesbarred));
                    }
                    wishlist.add(index, removeproductid);

                    String error = task.getException().getMessage();
                    Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                }
//                if (addtowishlistbtn != null) {
//////
//////                    addtowishlistbtn.setEnabled(true);
//////                }
                ProductDetailsActivity.running_wishlist_query = false;
            }
        });
    }

    public static void loadratingList(final Context getContext) {
        if (!ProductDetailsActivity.running_rating_query) {
            ProductDetailsActivity.running_rating_query = true;
            myratedIds.clear();
            myrating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> orderProductId = new ArrayList<>();
                        for (int x = 0; x < myOrderitemModelList.size(); x++) {
                            orderProductId.add(myOrderitemModelList.get(x).getProductId());
                        }

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myratedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myrating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_ID_" + x).toString().equals(productid)) {
                                ProductDetailsActivity.initialrating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if (ProductDetailsActivity.ratenowlayout != null) {
                                    ProductDetailsActivity.setRating(initialrating);
                                }
                            }


                        }
                        if (MyOrdersFragment.myOrderAdapter != null) {
                            MyOrdersFragment.myOrderAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailsActivity.running_rating_query = false;
                }
            });
        }
    }

    public static void loadCartList(final Context getContext, final Dialog dialog, final boolean loadProductdata, final TextView cartTotalAmount) {
        cartlist.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        cartlist.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBqueries.cartlist.contains(ProductDetailsActivity.productid)) {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;
                        } else {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                        }
                        if (loadProductdata) {
                            cartItemModelList.clear();
                            final String productid = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        final DocumentSnapshot documentSnapshot = task.getResult();
                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productid).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING)
                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    int index = 0;
                                                    if (cartlist.size() >= 2) {
                                                        index = cartlist.size() - 2;
                                                    }

                                                    if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                                        cartItemModelList.add(index, new CartItemModel(CartItemModel.CART_ITEM, productid, documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_full_title").toString()
                                                                , (long) documentSnapshot.get("free_coupens")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (long) 1
                                                                , (long) documentSnapshot.get("offers_applied")
                                                                , (long) 0
                                                                , true
                                                                , (long) documentSnapshot.get("max_quantity")
                                                                , (long) documentSnapshot.get("stock_quantity")
                                                                , (boolean) documentSnapshot.get("COD")
                                                        ));
                                                    } else {
                                                        cartItemModelList.add(index, new CartItemModel(CartItemModel.CART_ITEM, productid, documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_full_title").toString()
                                                                , (long) documentSnapshot.get("free_coupens")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (long) 1
                                                                , (long) documentSnapshot.get("offers_applied")
                                                                , (long) 0
                                                                , false
                                                                , (long) documentSnapshot.get("max_quantity")
                                                                , (long) documentSnapshot.get("stock_quantity")
                                                                , (boolean) documentSnapshot.get("COD")
                                                        ));
                                                    }
//                                                    LinearLayout parentcontinue = (LinearLayout) MyCartFragment.continueBtn.getParent();
                                                    if (cartlist.size() >= 1) {
                                                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                        parent.setVisibility(View.VISIBLE);
//                                                      parentcontinue.setVisibility(View.VISIBLE);
                                                        cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                                    }

                                                    if (cartlist.size() == 0) {
                                                        cartItemModelList.clear();
//                                                      parentcontinue.setVisibility(View.GONE);
                                                    }
                                                    MyCartFragment.cartAdapter.notifyDataSetChanged();
                                                } else {
                                                    ///errror
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    } else {

                                    }
                                }
                            });
                        }
                    }
//                    if (cartlist.size() != 0){
//                        badgecount.setVisibility(View.VISIBLE);
//                    }else{
//                        badgecount.setVisibility(View.INVISIBLE);
//                    }
//                    if (DBqueries.cartlist.size() < 99) {
//                        badgecount.setText(String.valueOf(DBqueries.cartlist.size()));
//                    }else{
//                        badgecount.setText("99+");
//                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removefromCart(final int index, final Context getContext, final TextView cartTotalAmount) {

        final String removeproductid = cartlist.get(index);

        cartlist.remove(index);
        Map<String, Object> updateCartlist = new HashMap<>();
        for (int x = 0; x < cartlist.size(); x++) {
            updateCartlist.put("product_ID_" + x, cartlist.get(x));

        }
        updateCartlist.put("list_size", (long) cartlist.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .set(updateCartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (cartItemModelList.size() != 0) {
                        cartItemModelList.remove(index);
                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if (cartlist.size() == 0) {
                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);
                        cartItemModelList.clear();
                    }
                    Toast.makeText(getContext, "Removed successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    cartlist.add(index, removeproductid);

                    String error = task.getException().getMessage();
                    Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                }
//                if (addtowishlistbtn != null) {
//////
//////                    addtowishlistbtn.setEnabled(true);
//////                }
                ProductDetailsActivity.running_cart_query = false;
            }
        });
    }

    public static int selectedAddress = -1;

    public static void loadAddresses(final Context getContext, final Dialog loadingDialog, final boolean gotoDeliveryActivity) {
        addressesModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            Intent deliveryIntent = null;
                            if ((long) task.getResult().get("list_size") == 0) {

                                deliveryIntent = new Intent(getContext, AddAddressActivity.class);
                                deliveryIntent.putExtra("INTENT", "deliveryIntent");

                            } else {
                                for (long x = 1; x < (long) task.getResult().get("list_size") + 1; x++) {
                                    addressesModelList.add(new AddressesModel(task.getResult().getBoolean("selected_" + x),
                                            task.getResult().getString("city_" + x),
                                            task.getResult().getString("locality_" + x),
                                            task.getResult().getString("flat_no_" + x),
                                            task.getResult().getString("pincode_" + x),
                                            task.getResult().getString("landmark_" + x),
                                            task.getResult().getString("name_" + x),
                                            task.getResult().getString("mobile_no_" + x),
                                            task.getResult().getString("alternate_mob_no_" + x),
                                            task.getResult().getString("state_" + x),
                                            task.getResult().getString("country_" + x)
                                    ));

                                    if ((boolean) task.getResult().get("selected_" + x)) {
                                        selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                                    }
                                }
                                if (gotoDeliveryActivity) {
                                    deliveryIntent = new Intent(getContext, DeliveryActivity.class);
                                }
                            }
                            if (gotoDeliveryActivity) {
                                getContext.startActivity(deliveryIntent);
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });

    }

    public static void loadRewards(final Context getContext, final Dialog loadingDialog, final Boolean onRewardFragment) {
        rewardModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            final Date lastSeenDate = task.getResult().getDate("Last seen");
                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            if (documentSnapshot.get("type").toString().equals("Discount") && lastSeenDate.before(documentSnapshot.getDate("validity"))) {
                                                rewardModelList.add(new RewardModel(documentSnapshot.getId(), documentSnapshot.get("type").toString(),
                                                        documentSnapshot.get("lower_limit").toString(),
                                                        documentSnapshot.get("upper_limit").toString(),
                                                        documentSnapshot.get("percentage").toString(),
                                                        documentSnapshot.get("body").toString(),
                                                        documentSnapshot.getDate("validity"),
                                                        (boolean) documentSnapshot.get("alreadyUsed")
                                                ));
                                            } else if (documentSnapshot.get("type").toString().equals("Flat Rs.* OFF") && lastSeenDate.before(documentSnapshot.getDate("validity"))) {
                                                rewardModelList.add(new RewardModel(documentSnapshot.getId(), documentSnapshot.get("type").toString(),
                                                        documentSnapshot.get("lower_limit").toString(),
                                                        documentSnapshot.get("upper_limit").toString(),
                                                        documentSnapshot.get("amount").toString(),
                                                        documentSnapshot.get("body").toString(),
                                                        documentSnapshot.getDate("validity"),
                                                        (boolean) documentSnapshot.get("alreadyUsed")
                                                ));
                                            }
                                        }
                                        if (onRewardFragment) {
                                            MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialog.dismiss();

                                }
                            });
                        } else {
                            loadingDialog.dismiss();

                            String error = task.getException().getMessage();
                            Toast.makeText(getContext, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    public static void loadOrders(final Context context, @Nullable final MyOrderAdapter myOrderAdapter, final Dialog loadingDialog) {
        myOrderitemModelList.clear();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").orderBy("Time", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        firebaseFirestore.collection("ORDERS").document(documentSnapshot.getString("ORDER_ID")).collection("OrderItems").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot ordertems : task.getResult().getDocuments()) {
                                        MyOrderitemModel myOrderitemModel = new MyOrderitemModel(ordertems.getString("Delivery Price"), ordertems.getString("Product ID"), ordertems.getString("Product Image"), ordertems.getString("Product Title"), ordertems.getString("ORDER Status"), ordertems.getString("Address"), ordertems.getString("Coupen Id"), ordertems.getString("Cutted Price"), ordertems.getDate("Ordered Date"), ordertems.getDate("Packed Date"), ordertems.getDate("Shipped Date"), ordertems.getDate("Delivered Date"), ordertems.getDate("Cancelled Date"), ordertems.getString("Discounted Price"), ordertems.getLong("Free coupens"), ordertems.getString("Full name"), ordertems.getString("ORDER ID"), ordertems.getString("Payment Method"), ordertems.getString("Pin code"), ordertems.getString("Product Price"), ordertems.getLong("Product Quantity"), ordertems.getString("User ID"), ordertems.getBoolean("Cancellation requested"));
                                        myOrderitemModelList.add(myOrderitemModel);


                                    }
                                    if (myOrderAdapter != null) {
                                        myOrderAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
                    }
//                    }
                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();


                }
            }

        });
    }

    public static void checkNotifications(boolean remove, @Nullable final TextView notifyCount) {
        if (remove) {
            registration.remove();
        } else {
            registration = firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_NOTIFICATIONS").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        notificationModelList.clear();
                        int unread = 0;
                        for (long x = 0; x < (long) documentSnapshot.get("list_size"); x++) {

                            notificationModelList.add(0, new NotificationModel(documentSnapshot.getString("Image_" + x), documentSnapshot.getString("Icon_" + x), documentSnapshot.getString("body_" + x), documentSnapshot.getBoolean("readed_" + x), documentSnapshot.getDate("Date_" + x)));


                            if (!documentSnapshot.getBoolean("readed_" + x)) {
                                unread++;

                                if (notifyCount != null) {
                                    if (unread > 0) {
                                        notifyCount.setVisibility(View.VISIBLE);
                                        if (unread < 99) {
                                            notifyCount.setText(String.valueOf(unread));
                                        } else {
                                            notifyCount.setText("99+");
                                        }
                                    } else {
                                        notifyCount.setVisibility(View.GONE);

                                    }
                                }
                            }
                        }
                        if (NotificationsActivity.adapter != null) {
                            NotificationsActivity.adapter.notifyDataSetChanged();
                        }
                    }
                }
            });

        }

    }

    public static void loadgrid(final Context context,@Nullable final GridAdapter gridAdapter) {
        griditemviewList.clear();
        final String productId = firebaseFirestore.collection("PRODUCTS").document().getId();
        firebaseFirestore.collection("PRODUCTS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        List<GridViewModel> gridViewModelList = new ArrayList<>();
//                       gridViewModelList.add(new GridViewModel(productId,))
//                    }
                    for (final GridViewModel model : griditemviewList) {
                        if (!model.getProductID().isEmpty() && model.getProductPrice().isEmpty()) {
                            firebaseFirestore.collection("PRODUCTS").document(model.getProductID()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                model.setProductTitle(task.getResult().getString("product_title"));
                                                model.setProductdescription(task.getResult().getString("product_description"));
                                                model.setProductPrice(task.getResult().getString("product_price"));
                                                model.setProductimage(task.getResult().getString("product_image_1"));
                                                gridAdapter.notifyDataSetChanged();


                                            } else {
                                                ////do nothing
                                            }
                                        }
                                    });
                        }
                    }

                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public static void clearData() {
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesName.clear();
        wishlist.clear();
        wishlistModelList.clear();
        cartlist.clear();
        cartItemModelList.clear();
        myratedIds.clear();
        myrating.clear();
        addressesModelList.clear();
        rewardModelList.clear();
        myOrderitemModelList.clear();

    }

}
