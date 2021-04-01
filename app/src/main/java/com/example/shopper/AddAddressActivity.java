package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    private ImageButton backbtn;
    private Button saveBtn;
    private BottomSheetDialog loadindDialog;


    ///a/dress
    private EditText city;
    private EditText locality;
    private EditText flatNo;
    private EditText pincode;
    private EditText landmark;
    private EditText name;
    private EditText mobileNo;
    private EditText alternateNo;

    private int position;


    private String[] statelist;
    private String selectedstates;
    private Spinner statespinner;

    private String[] countrylist;
    private String selectedcountry;
    private Spinner countryspinner;


    private boolean updateAddresss = false;
    private AddressesModel addressesModel;
    ///a/dress


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        backbtn = findViewById(R.id.backbtn);
        saveBtn = findViewById(R.id.save_address_btn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Add a new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        statelist = getResources().getStringArray(R.array.Pak_states);
        countrylist = getResources().getStringArray(R.array.Pakk_states);
        ////loading dialog

        loadindDialog = new BottomSheetDialog(this);
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog


        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flatNo = findViewById(R.id.flat_no);
        pincode = findViewById(R.id.pincodesss);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.nameeee);
        mobileNo = findViewById(R.id.phone_no);
        alternateNo = findViewById(R.id.alternate_phone_no);


        ///spinner states///////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////

        statespinner = findViewById(R.id.state_spinner);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statelist);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statespinner.setAdapter(spinnerAdapter);

        statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedstates = statelist[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ///spinner states///////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////


        ///spinner/ country//////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////

        countryspinner = findViewById(R.id.country_spinner);

        ArrayAdapter spinnercountryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countrylist);
        spinnercountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryspinner.setAdapter(spinnercountryAdapter);

        countryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedcountry = countrylist[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ///spinner country///////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                startActivity(backIntent);
            }
        });
        if (getIntent().getStringExtra("INTENT").equals("update_address")){
            updateAddresss = true;
            position = getIntent().getIntExtra("index",-1);
            addressesModel = DBqueries.addressesModelList.get(position);

            city.setText(addressesModel.getCity());
            locality.setText(addressesModel.getLocality());
            flatNo.setText(addressesModel.getFlatNo());
            pincode.setText(addressesModel.getPincode());
            landmark.setText(addressesModel.getLandmark());
            name.setText(addressesModel.getName());
            mobileNo.setText(addressesModel.getMobileNo());
            alternateNo.setText(addressesModel.getAlternateNo());

            for (int i = 0;i<statelist.length;i++) {
               if (statelist[i].equals(addressesModel.getStatespinner())){

                   statespinner.setSelection(i);
               }
            }
            for (int i = 0;i<countrylist.length;i++) {
                if (countrylist[i].equals(addressesModel.getCountryspinner())){

                    countryspinner.setSelection(i);
                }
            }
            saveBtn.setText("Update");



        }else {
            position = (int) DBqueries.addressesModelList.size();
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(city.getText())) {
                    if (!TextUtils.isEmpty(locality.getText())) {
                        if (!TextUtils.isEmpty(flatNo.getText())) {
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() < 7 && pincode.getText().length() > 4) {
                                if (!TextUtils.isEmpty(name.getText())) {
                                    if (!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() < 14 && mobileNo.getText().length() > 8) {

                                        loadindDialog.show();


                                        Map<String, Object> addAddress = new HashMap();
                                        addAddress.put("city_" + String.valueOf(position + 1), city.getText().toString());
                                        addAddress.put("locality_" + String.valueOf(position + 1), locality.getText().toString());
                                        addAddress.put("flat_no_" + String.valueOf(position + 1), flatNo.getText().toString());
                                        addAddress.put("pincode_" + String.valueOf(position + 1), pincode.getText().toString());
                                        addAddress.put("landmark_" + String.valueOf(position + 1), landmark.getText().toString());
                                        addAddress.put("name_" + String.valueOf(position + 1),name.getText().toString());
                                        addAddress.put("mobile_no_" + String.valueOf(position + 1),mobileNo.getText().toString());
                                        addAddress.put("alternate_mob_no_" + String.valueOf(position + 1), alternateNo.getText().toString());
                                        addAddress.put("state_" + String.valueOf(position + 1), selectedstates);
                                        addAddress.put("country_" + String.valueOf(position + 1), selectedcountry);
                                        if (!updateAddresss) {
                                            addAddress.put("list_size", (long) DBqueries.addressesModelList.size() + 1);
                                            if (getIntent().getStringExtra("INTENT").equals("manage")){
                                                if (DBqueries.addressesModelList.size() == 0){
                                                    addAddress.put("selected_" + String.valueOf(position + 1), true);

                                                }else{
                                                    addAddress.put("selected_" + String.valueOf(position + 1), false);

                                                }

                                            }else{
                                                addAddress.put("selected_" + String.valueOf(position + 1), true);

                                            }
                                            if (DBqueries.addressesModelList.size() > 0) {
                                                addAddress.put("selected_" + (DBqueries.selectedAddress + 1), false);
                                            }
                                        }

                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()
                                        ).collection("USER_DATA").document("MY_ADDRESSES")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (!updateAddresss) {
                                                        if (DBqueries.addressesModelList.size() > 0) {
                                                            DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                        }
                                                        DBqueries.addressesModelList.add(new AddressesModel(true, city.getText().toString(), locality.getText().toString(), flatNo.getText().toString(), pincode.getText().toString(), landmark.getText().toString(), name.getText().toString(), mobileNo.getText().toString(), alternateNo.getText().toString(), selectedstates, selectedcountry));
                                                        if (getIntent().getStringExtra("INTENT").equals("manage")){
                                                            if (DBqueries.addressesModelList.size() == 0){
                                                                DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;


                                                            }

                                                        }else{
                                                            DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;


                                                        }
                                                    }else {
                                                        DBqueries.addressesModelList.set(position,new AddressesModel(true, city.getText().toString(), locality.getText().toString(), flatNo.getText().toString(), pincode.getText().toString(), landmark.getText().toString(), name.getText().toString(), mobileNo.getText().toString(), alternateNo.getText().toString(), selectedstates, selectedcountry));

                                                    }



                                                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryIntent);
                                                    } else {
                                                        MyAddressesActivity.refreshItem(DBqueries.selectedAddress, DBqueries.addressesModelList.size() - 1);
                                                    }

                                                    finish();

                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadindDialog.dismiss();
                                            }
                                        });
                                    } else {
                                        mobileNo.requestFocus();
                                        Toast.makeText(AddAddressActivity.this, "Please provide valid and greater than 8  and " +
                                                "less than  14 digits Mobile No!", Toast.LENGTH_LONG).show();
                                        mobileNo.setError("Mobile No Required!");

                                    }
                                } else {
                                    name.requestFocus();
                                    name.setError("Name Required!");

                                }
                            } else {
                                pincode.requestFocus();
                                Toast.makeText(AddAddressActivity.this, "Please provide valid and less than 7 and  " +
                                        "greater than 4 digits Pincode or Postal code or Area code!", Toast.LENGTH_LONG).show();
                                pincode.setError("Postal code Required!");

                            }
                        } else {
                            flatNo.requestFocus();
                            flatNo.setError("Flat No, Building  Required!");


                        }
                    } else {
                        locality.requestFocus();
                        locality.setError("Locality Required!");


                    }
                } else {

                    city.requestFocus();
                    city.setError("City Required!");
                }


            }
        });
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
                Toast.makeText(AddAddressActivity.this, "An Unexpected error occured!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }else if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}