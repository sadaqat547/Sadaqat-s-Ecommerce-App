package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.shopper.DBqueries.cartlist;
import static com.example.shopper.DBqueries.firebaseFirestore;

public class DeliveryActivity extends AppCompatActivity {

    private RecyclerView deliveryRecyclerView;
    private Button changeOrAddNewAddressBtn;
    private TextView fullName;
    private TextView fullAddress;
    private TextView pincode;
    private ImageView bankbtn;

    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    public static List<CartItemModel> cartItemModelList;
    private Button continueBtn;
    public static BottomSheetDialog loadindDialog;
    private BottomSheetDialog paymentdialog;
    private BottomSheetDialog confirmdialog;
    private ImageView paytm;
    private ImageView cod;
    private ConstraintLayout order_confirmation_layout;
    private ConstraintLayout main_constraint_layout;
    private TextView orderId;
    public static String order_id;
    private Button confirm;
    private Button decline;
    private ImageView continue_shopping;
    public static boolean fromCart;
    public static boolean successResponse = false;

    public static boolean getQtyIds = true;
    public static CartAdapter cartAdapter;
    private String paymentMethod = "PAYTM";
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bankbtn = findViewById(R.id.bank_btn);
        confirm = findViewById(R.id.confirmbtn);
        decline = findViewById(R.id.decline_btn);
        fullName = findViewById(R.id.full_name);
        order_id = UUID.randomUUID().toString().substring(0, 28);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincodesss);
        deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        changeOrAddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);
        totalAmount = findViewById(R.id.total_cart_amount);
        continueBtn = findViewById(R.id.cart_continue_btn);
//        order_confirmation_layout = findViewById(R.id.order_confirmation_layout);
//        main_constraint_layout = findViewById(R.id.main_constraint_layout);
//        orderId = findViewById(R.id.order_id);
//        continue_shopping = findViewById(R.id.continue_shopping_btn);
//        final List<CartItemModel> cartItemModelList = new ArrayList<>();
//        cartItemModelList.add(new CartItemModel(0,R.mipmap.image,"pixel 2 (black)",2,"Rs.49999/-","Rs.59999/-",1,0,0));
//        cartItemModelList.add(new CartItemModel(0,R.mipmap.image,"pixel 2 (black)",0,"Rs.49999/-","Rs.59999/-",1,1,0));
//        cartItemModelList.add(new CartItemModel(0,R.mipmap.image,"pixel 2 (black)",2,"Rs.49999/-","Rs.59999/-",1,2,0));

        cartAdapter = new CartAdapter(DeliveryActivity.cartItemModelList, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        firebaseFirestore = FirebaseFirestore.getInstance();

        ////loading dialog

        loadindDialog = new BottomSheetDialog((DeliveryActivity.this));
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(true);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog

//        bankbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent chat = new Intent(DeliveryActivity.this,hatbot.class);
//                startActivity(chat);
//            }
//        });

        ////loading dialog

        paymentdialog = new BottomSheetDialog((DeliveryActivity.this));
        paymentdialog.setContentView(R.layout.payment_method_dialog);
        paymentdialog.setCancelable(true);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        paymentdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog

        getQtyIds = true;
        ////loading dialog

        confirmdialog = new BottomSheetDialog((DeliveryActivity.this));
        confirmdialog.setContentView(R.layout.confirmdialog);
        confirmdialog.setCancelable(true);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        confirmdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog

        paytm = paymentdialog.findViewById(R.id.paytm_btn);
        cod = paymentdialog.findViewById(R.id.cod_btn);
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = "COD";
                placeOrderDetails();
                if (!fromCart) {
                    Toast.makeText(DeliveryActivity.this, " If taking longer than Add Product in your cart. Then Buy From Cart", Toast.LENGTH_LONG).show();
                }
//                loadindDialog.dismiss();
//                Intent backInntent = new Intent(DeliveryActivity.this, ConfirmActivity.class);
//                startActivity(backInntent);


            }
        });


        changeOrAddNewAddressBtn.setVisibility(View.VISIBLE);
        changeOrAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQtyIds = true;
                Intent myAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressesIntent);

            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean allProductsAvailable = true;
                for (CartItemModel cartItemModel : cartItemModelList) {
                    if (cartItemModel.isQtyError()) {
                        allProductsAvailable = false;
                        break;
                    }
                    if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                        if (!cartItemModel.isCOD()) {
//                        cod.setVisibility(View.INVISIBLE);
                            cod.setAlpha((float) 0.4);
                            cod.setEnabled(false);
                            Toast.makeText(DeliveryActivity.this, "Some of your cart Product has COD unavailable!", Toast.LENGTH_LONG).show();
                            break;
                        } else {
                            cod.setEnabled(true);
                            cod.setVisibility(View.VISIBLE);
                            cod.setAlpha((float) 1.0);

                        }
                    }
                }
                if (allProductsAvailable) {
                    paymentdialog.show();
                }
            }

        });
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = "PAYTM";
                placeOrderDetails();
            }
        });


    }


    @Override
    protected void onStart() {

        super.onStart();
        getQtyIds = true;

///////accessing quantity
        if (getQtyIds) {
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {

                for (int y = 0; y < cartItemModelList.get(x).getProductQuantity(); y++) {
                    final String quantitydocumentName = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("time", FieldValue.serverTimestamp());
                    final int finalX = x;
                    final int finalY = y;
                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductid()).collection("QUANTITY").document(quantitydocumentName)
                            .set(timestamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            cartItemModelList.get(finalX).getQtyIds().add(quantitydocumentName);
                            if (finalY + 1 == cartItemModelList.get(finalX).getProductQuantity()) {
                                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductid()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING)
                                        .limit(cartItemModelList.get(finalX).getStockQuantity()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            List<String> serverQuantities = new ArrayList<>();

                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                serverQuantities.add(queryDocumentSnapshot.getId());
                                            }
                                            long AvailableQty = 0;
                                            boolean noLongerAvailable = true;
                                            for (String qtyId : cartItemModelList.get(finalX).getQtyIds()) {
                                                if (!serverQuantities.add(qtyId)) {
                                                    if (noLongerAvailable) {
                                                        cartItemModelList.get(finalX).setInstock(false);
                                                    } else {
                                                        cartItemModelList.get(finalX).setQtyError(true);
                                                        cartItemModelList.get(finalX).setMaxQuantity(AvailableQty);
                                                        Toast.makeText(DeliveryActivity.this, "All products may not be available at required quantity!", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    AvailableQty++;
                                                    noLongerAvailable = false;
                                                }

                                            }
                                            cartAdapter.notifyDataSetChanged();
                                        } else {
                                            ///errror
                                            String error = task.getException().getMessage();
                                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }
        } else {
            getQtyIds = true;
        }

///////accessing quantity

        String name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        String mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        String flatno = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getStatespinner();
        String country = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCountryspinner();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateNo().equals("")) {
            fullName.setText(name + " - " + mobileNo);
        } else {
            fullName.setText(name + " - " + mobileNo + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateNo());

        }
        if (landmark.equals("")) {
            fullAddress.setText(flatno + " - " + locality + " - " + city + " - " + state + " - " + country);

        } else {
            fullAddress.setText(flatno + " - " + locality + " - " + landmark + " - " + city + " - " + state + " - " + country);

        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

    }

    @Override
    protected void onPause() {
        super.onPause();
        loadindDialog.dismiss();
        if (getQtyIds) {
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                if (!successResponse) {
                    for (final String qtyId : cartItemModelList.get(x).getQtyIds()) {
                        final int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductid()).collection("QUANTITY").document(qtyId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        if (qtyId.equals(cartItemModelList.get(finalX).getQtyIds().get(cartItemModelList.get(finalX).getQtyIds().size() - 1))) {
                                            cartItemModelList.get(finalX).getQtyIds().clear();


                                        }
                                    }
                                });
                    }
                } else {

                    cartItemModelList.get(x).getQtyIds().clear();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        loadindDialog.dismiss();
        super.onBackPressed();

        finish();
        return;
//        if (successResponse) {
//            finish();
//            return;
//        }
    }

    private void placeOrderDetails() {

        String userId = FirebaseAuth.getInstance().getUid();
        loadindDialog.show();

        for (CartItemModel cartItemModel : DBqueries.cartItemModelList) {
            if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER ID", order_id);
                orderDetails.put("Product ID", cartItemModel.getProductid());
                orderDetails.put("Product Image", cartItemModel.getProductImage());
                orderDetails.put("Product Title", cartItemModel.getProductTitle());
                orderDetails.put("User ID", userId);
                orderDetails.put("Product Quantity", cartItemModel.getProductQuantity());
                if (cartItemModel.getCuttedPrice() != null) {
                    orderDetails.put("Cutted Price", cartItemModel.getCuttedPrice());
                } else {
                    orderDetails.put("Cutted Price", "");

                }
                orderDetails.put("Product Price", cartItemModel.getProductPrice());
                if (cartItemModel.getSelectedCoupenId() != null) {
                    orderDetails.put("Coupen Id", cartItemModel.getSelectedCoupenId());
                } else {
                    orderDetails.put("Coupen Id", "");

                }
                if (cartItemModel.getDiscountedPrice() != null) {
                    orderDetails.put("Discounted Price", cartItemModel.getDiscountedPrice());
                } else {
                    orderDetails.put("Discounted Price", "");

                }
                orderDetails.put("Ordered Date", FieldValue.serverTimestamp());
                orderDetails.put("Packed Date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped Date", FieldValue.serverTimestamp());
                orderDetails.put("Delivered Date", FieldValue.serverTimestamp());
                orderDetails.put("Cancelled Date", FieldValue.serverTimestamp());
                orderDetails.put("ORDER Status", "Ordered");
                orderDetails.put("Payment Method", paymentMethod);
                orderDetails.put("Address", fullAddress.getText());
                orderDetails.put("Full name", fullName.getText());
                orderDetails.put("Pin code", pincode.getText());
                orderDetails.put("Free coupens", cartItemModel.getFreeCoupens());
                orderDetails.put("Delivery Price", cartItemModelList.get(cartItemModelList.size() - 1).getDeliveryPrice());
                orderDetails.put("Cancellation requested", false);


                firebaseFirestore.collection("ORDERS").document(order_id).collection("OrderItems")
                        .document(cartItemModel.getProductid())
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();

                        }
                    }

                });

            } else {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("Total Items", cartItemModel.getTotalItems());
                orderDetails.put("Total Items Price", cartItemModel.getTotalItemsPrice());
                orderDetails.put("Delivery Price", cartItemModel.getDeliveryPrice());
                orderDetails.put("Total Amount", cartItemModel.getTotalAmount());
                orderDetails.put("Saved Amount", cartItemModel.getSavedAmount());
                orderDetails.put("Payment Status", "not_paid");
                orderDetails.put("ORDER Status", "Canceled");
                firebaseFirestore.collection("ORDERS").document(order_id).set(orderDetails)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (paymentMethod.equals("PAYTM")) {
                                        paytm();
                                    } else {
                                        if (paymentMethod.equals("COD")) {
                                            cod();
                                        }
                                    }
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        }

    }

    private void paytm() {
        getQtyIds = false;

        paymentdialog.dismiss();
        loadindDialog.show();
        if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }


        final String m_Id = "YlutwW84809962366406";
        final String customerid = FirebaseAuth.getInstance().getUid();
        final String orderid = UUID.randomUUID().toString().substring(0, 28);
        String url = "https://mymalll.000webhostapp.com/paytm/generateChecksum.php";
        final String callbackurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")) {
                        String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                        PaytmPGService paytmPGService = PaytmPGService.getStagingService(CHECKSUMHASH);
                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", m_Id);
                        paramMap.put("ORDER_ID", orderid);
                        paramMap.put("CUST_ID", customerid);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callbackurl);
                        paramMap.put("CHECKSUMHASH", CHECKSUMHASH);


                        PaytmOrder paytmOrder = new PaytmOrder(paramMap);

                        paytmPGService.initialize(paytmOrder, null);
                        paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
                                order_confirmation_layout.setVisibility(View.VISIBLE);
                                main_constraint_layout.setVisibility(View.GONE);

//                                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {
                                    Map<String, Object> updateStatus = new HashMap<>();
                                    updateStatus.put("Payment Status", "paid");
                                    updateStatus.put("ORDER Status", "Ordered");
                                    firebaseFirestore.collection("ORDERS").document(String.valueOf(order_id)).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Map<String, Object> userOrder = new HashMap<>();
                                                userOrder.put("ORDER_ID", orderid);
                                                firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document("order id").set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        } else {
                                                            Toast.makeText(DeliveryActivity.this, "Failed to update CartList", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(DeliveryActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    successResponse = true;
                                    if (ProductDetailsActivity.productDetailsActivity != null) {
                                        ProductDetailsActivity.productDetailsActivity.finish();
                                        ProductDetailsActivity.productDetailsActivity = null;
                                    }
                                    if (fromCart) {
                                        loadindDialog.show();

                                        Map<String, Object> updateCartlist = new HashMap<>();
                                        long cartListSize = 0;
                                        final List<Integer> indexList = new ArrayList<>();
                                        for (int x = 0; x < cartlist.size(); x++) {
                                            if (!cartItemModelList.get(x).isInstock()) {
                                                updateCartlist.put("product_ID_" + cartListSize, cartItemModelList.get(x).getProductid());

                                                cartListSize++;
                                            } else {
                                                indexList.add(x);
                                            }
                                        }

                                        updateCartlist.put("list_size", cartListSize);

                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                                                .set(updateCartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    for (int x = 0; x < indexList.size(); x++) {
                                                        cartlist.remove(indexList.get(x).intValue());
                                                        cartItemModelList.remove(indexList.get(x).intValue());
                                                        cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);
                                                    }
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadindDialog.dismiss();
                                            }
                                        });
                                    }
                                    continueBtn.setEnabled(false);
                                    changeOrAddNewAddressBtn.setEnabled(false);
//                                            order_id.setText("Order ID "+inResponse.getString("ORDERID"));
                                    order_confirmation_layout.setVisibility(View.VISIBLE);
                                    continue_shopping.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                            Intent mainIntent = new Intent(DeliveryActivity.this, MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void networkNotAvailable() {
                                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onBackPressedCancelTransaction() {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

                                Toast.makeText(getApplicationContext(), "Transaction cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadindDialog.dismiss();
                Toast.makeText(DeliveryActivity.this, "Something went wrong! Please try again!", Toast.LENGTH_LONG).show();
                getQtyIds = true;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("MID", m_Id);
                paramMap.put("ORDER_ID", orderid);
                paramMap.put("CUST_ID", customerid);
                paramMap.put("CHANNEL_ID", "WAP");
                paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(2, totalAmount.getText().length() - 2));
                paramMap.put("WEBSITE", "WEBSTAGING");
                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                paramMap.put("CALLBACK_URL", callbackurl);
                return paramMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void cod() {
        loadindDialog.show();
        getQtyIds = false;
        if (DBqueries.cartItemModelList.size() == 1) {
            for (int x = 0; x < DBqueries.cartItemModelList.size() - 1; x++) {
                for (String qtyId : DBqueries.cartItemModelList.get(x).getQtyIds()) {
                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductid()).collection("QUANTITY").document(qtyId).update("user_id", FirebaseAuth.getInstance().getUid());

                }
            }
        }

        if (fromCart) {
            loadindDialog.show();

            Map<String, Object> updateCartlist = new HashMap<>();
            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();
            for (int x = 0; x < cartlist.size(); x++) {
                if (!cartItemModelList.get(x).isInstock()) {
                    updateCartlist.put("product_ID_" + cartListSize, cartItemModelList.get(x).getProductid());

                    cartListSize++;
                } else {
                    indexList.add(x);
                }
            }

            updateCartlist.put("list_size", cartListSize);

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                    .set(updateCartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        for (int x = 0; x < indexList.size(); x++) {
                            cartlist.remove(indexList.get(x).intValue());
                            cartItemModelList.remove(indexList.get(x).intValue());
                            cartItemModelList.remove(DBqueries.cartItemModelList.size() - 2);
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    loadindDialog.dismiss();
                }
            });
        }
        continueBtn.setEnabled(false);

        Intent codintent = new Intent(DeliveryActivity.this, ConfirmActivity.class);
        codintent.putExtra("orderId", order_id);
        startActivity(codintent);

        loadindDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_share) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "share Demo");
                String sharemessage = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                intent.putExtra(Intent.EXTRA_TEXT, sharemessage);
                startActivity(Intent.createChooser(intent, "Share By"));
            } catch (Exception e) {
                Toast.makeText(DeliveryActivity.this, "An Unexpected error occured!", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}