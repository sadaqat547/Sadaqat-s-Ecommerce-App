package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.shopper.DBqueries.firebaseFirestore;
import static com.example.shopper.DBqueries.myOrderitemModelList;

public class OrderconfirmedActivity extends AppCompatActivity {
    private FloatingActionButton continueshoppingbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirmed);
        continueshoppingbtn = (FloatingActionButton) findViewById(R.id.continue_shopping_btn);



//        firebaseFirestore.collection("ORDERS").document(String.valueOf(DeliveryActivity.order_id)).update(updatestatus).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//
//                }else{
//
//                }
//            }
//        });

        continueshoppingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(OrderconfirmedActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();


        Map<String, Object> updateStatus = new HashMap<>();
            updateStatus.put("ORDER Status", "Ordered");
        final String orderId = getIntent().getStringExtra("orderId");
        FirebaseFirestore.getInstance().collection("ORDERS").document(orderId).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> userOrder = new HashMap<>();
                        userOrder.put("ORDER_ID", orderId);
                        userOrder.put("Time", FieldValue.serverTimestamp());
                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(orderId).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(OrderconfirmedActivity.this, "Order Placed Successfully!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(OrderconfirmedActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            });

    }
}