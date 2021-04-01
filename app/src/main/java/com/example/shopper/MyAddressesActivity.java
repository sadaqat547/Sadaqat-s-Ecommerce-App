package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shopper.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {
    private FloatingActionButton addnewAddressbtn;
    private ImageButton backbtn;
    private int previousAddress;
    private Button deliverHereBtn;
    private RecyclerView myAddressesRecyclerView;
    private static AddressesAdapter addressesAdapter;
    private TextView addresssaved;
    private int mode;
    private BottomSheetDialog loadindDialog;
    private BottomSheetDialog editRemoveDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        backbtn = findViewById(R.id.backbtn);
        myAddressesRecyclerView = findViewById(R.id.addresses_recyclerview);
        deliverHereBtn = findViewById(R.id.delivere_here_btn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(layoutManager);
        addnewAddressbtn = findViewById(R.id.add_new_address_btn);
        addresssaved = findViewById(R.id.address_saved);
        previousAddress = DBqueries.selectedAddress;
        ////loading dialog

        loadindDialog = new BottomSheetDialog(this);
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadindDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                addresssaved.setText(String.valueOf(DBqueries.addressesModelList.size())+" Addresses Saved Successfully!");

            }
        });
        ////loading dialog
        ////loading dialog

        editRemoveDialog = new BottomSheetDialog(this);
        editRemoveDialog.setContentView(R.layout.editremove_dialog);
        editRemoveDialog.setCancelable(true);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        editRemoveDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ////loading dialog







         mode = getIntent().getIntExtra("MODE", -1);
        if (mode == SELECT_ADDRESS) {
            deliverHereBtn.setVisibility(View.VISIBLE);
        } else {
            deliverHereBtn.setVisibility(View.GONE);
        }
        addressesAdapter = new AddressesAdapter(DBqueries.addressesModelList, mode,loadindDialog,editRemoveDialog);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator) myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();


        addnewAddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAddressintent = new Intent(MyAddressesActivity.this, AddAddressActivity.class);
                if (mode != SELECT_ADDRESS){
                    addAddressintent.putExtra("INTENT","manage");

                }else {
                    addAddressintent.putExtra("INTENT", "null");
                }
                startActivity(addAddressintent);
            }
        });



        deliverHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DBqueries.selectedAddress != previousAddress) {
                    final int previousaddressindex = previousAddress;
                    loadindDialog.show();

                    Map<String, Object> updateSelection = new HashMap<>();
                    updateSelection.put("selected_" + String.valueOf(previousAddress + 1), false);
                    updateSelection.put("selected_" + String.valueOf(DBqueries.selectedAddress + 1), true);
                    previousAddress = DBqueries.selectedAddress;

                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                            .document("MY_ADDRESSES")
                            .update(updateSelection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                finish();
                            } else {
                                previousAddress = previousaddressindex;
                                String error = task.getException().getMessage();
                                Toast.makeText(MyAddressesActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            loadindDialog.dismiss();

                        }
                    });
                } else {

                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        editRemoveDialog.dismiss();
        addresssaved.setText(String.valueOf(DBqueries.addressesModelList.size())+" Addresses Saved Successfully!");

    }

    public static void refreshItem(int deselect, int select) {
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);
    }


    @Override
    public void onBackPressed() {
        if (mode == SELECT_ADDRESS) {
            if (DBqueries.selectedAddress != previousAddress) {
                DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                DBqueries.addressesModelList.get(previousAddress).setSelected(true);
                DBqueries.selectedAddress = previousAddress;
            }
        }
            super.onBackPressed();
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
                intent.putExtra(Intent.EXTRA_SUBJECT,"share Demo");
                String sharemessage = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID+"\n\n";
                intent.putExtra(Intent.EXTRA_TEXT,sharemessage);
                startActivity(Intent.createChooser(intent,"Share By"));
            }catch (Exception e){
                Toast.makeText(MyAddressesActivity.this, "An Unexpected error occured!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }else if (id == android.R.id.home){
            if (DBqueries.selectedAddress != previousAddress ){
                DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                DBqueries.addressesModelList.get(previousAddress).setSelected(true);
                DBqueries.selectedAddress = previousAddress;
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}