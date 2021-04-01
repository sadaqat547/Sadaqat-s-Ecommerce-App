package com.example.shopper;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shopper.DeliveryActivity.SELECT_ADDRESS;
import static com.example.shopper.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.shopper.MyAddressesActivity.refreshItem;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {
    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preSelectedPosition;
    private boolean refresh = false;
    private Dialog loadingDialog;
    private Dialog editRemoveDialog;

    public AddressesAdapter(List<AddressesModel> addressesModelList, int MODE,  Dialog loadingDialog,  Dialog editRemoveDialog) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
        preSelectedPosition = DBqueries.selectedAddress;
        this.loadingDialog = loadingDialog;
        this.editRemoveDialog = editRemoveDialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String city = addressesModelList.get(position).getCity();
        String locality = addressesModelList.get(position).getLocality();
        String flatNo = addressesModelList.get(position).getFlatNo();
        String pincode = addressesModelList.get(position).getPincode();
        String landmark = addressesModelList.get(position).getLandmark();
        String name = addressesModelList.get(position).getName();
        String mobileNo = addressesModelList.get(position).getMobileNo();
        String alternateNo = addressesModelList.get(position).getAlternateNo();
        String stateSpinner = addressesModelList.get(position).getStatespinner();
        String CountrySpinner = addressesModelList.get(position).getCountryspinner();
        boolean selected = addressesModelList.get(position).getSelected();
        viewHolder.setData(name, city, pincode, selected, position, mobileNo, alternateNo, flatNo, locality, stateSpinner, CountrySpinner, landmark);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView icon;
        private LinearLayout optionsContainer;
        private Button removveAddress;
        private Button editAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.nameeee);
            address = itemView.findViewById(R.id.addressesss);
            pincode = itemView.findViewById(R.id.pincodesss);
            icon = itemView.findViewById(R.id.iconView);
            optionsContainer = itemView.findViewById(R.id.option_container);
            removveAddress = editRemoveDialog.findViewById(R.id.removeaddress);
            editAddress = editRemoveDialog.findViewById(R.id.editaddress);


        }

        private void setData(String username, String city, String userpincode, Boolean selected, final int position, String mobileNo, String alternateMobileNo, String flatno, String locality, String state, String country, String landmark) {
            if (alternateMobileNo.equals("")) {
                fullname.setText(username + " - " + mobileNo);
            } else {
                fullname.setText(username + " - " + mobileNo + " or " + alternateMobileNo);

            }
            if (landmark.equals("")) {
                address.setText(flatno + " - " + locality + " - " + city + " - " + state + " - " + country);

            } else {
                address.setTextSize(12);
                address.setText(flatno + " - " + locality + " - " + landmark + " - " + city + " - " + state + " - " + country);

            }
            pincode.setText(userpincode);


            if (MODE == SELECT_ADDRESS) {
                icon.setImageResource(R.drawable.ic_tick);
                if (selected) {
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                } else {
                    icon.setVisibility(View.GONE);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preSelectedPosition != position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(preSelectedPosition).setSelected(false);
                            refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DBqueries.selectedAddress = position;
                        }
                    }
                });
            } else if (MODE == MANAGE_ADDRESS) {
                editAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



//                optionsContainer.setVisibility(View.GONE);
//                optionsContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() { //////editaddress
//                    @Override
//                    public void onClick(View v) {
                        Intent addAddressintent = new Intent(itemView.getContext(), AddAddressActivity.class);
                        addAddressintent.putExtra("INTENT", "update_address");
                        addAddressintent.putExtra("index", position);
                        itemView.getContext().startActivity(addAddressintent);
                        refresh = false;
                    }
                });
                removveAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



//                optionsContainer.getChildAt(1).setOnClickListener(new View.OnClickListener() {/////removeAddress
//                    @Override
//                    public void onClick(View v) {
                        loadingDialog.show();

                        Map<String, Object> adreesses = new HashMap<>();
                        int x = 0;
                        int selected = -1;
                        for (int i = 0; i < addressesModelList.size(); i++) {
                            if (i != position) {
                                x++;
                                adreesses.put("city_" + x, addressesModelList.get(i).getCity());
                                adreesses.put("locality_" + x, addressesModelList.get(i).getLocality());
                                adreesses.put("flat_no_" + x, addressesModelList.get(i).getFlatNo());
                                adreesses.put("pincode_" + x, addressesModelList.get(i).getPincode());
                                adreesses.put("landmark_" + x, addressesModelList.get(i).getLandmark());
                                adreesses.put("name_" + x, addressesModelList.get(i).getName());
                                adreesses.put("mobile_no_" + x, addressesModelList.get(i).getMobileNo());
                                adreesses.put("alternate_mob_no_" + x, addressesModelList.get(i).getAlternateNo());
                                adreesses.put("state_" + x, addressesModelList.get(i).getStatespinner());
                                adreesses.put("country_" + x, addressesModelList.get(i).getCountryspinner());

                                if (addressesModelList.get(position).getSelected()){

                                    if (position >=0){
                                        if (x == position-1){
                                            adreesses.put("selected_" + x,true);

                                            selected = x;
                                        }else {
                                            adreesses.put("selected_" + x, addressesModelList.get(i).getSelected());

                                        }
                                    }else{
                                        if (x == 1){
                                            adreesses.put("selected_" + x,true);
                                            selected = x;

                                        }else {
                                            adreesses.put("selected_" + x, addressesModelList.get(i).getSelected());

                                        }
                                    }
                                }else{
                                    adreesses.put("selected_" + x, addressesModelList.get(i).getSelected());


                                }
//                                if (position > 0){
//                                    if (addressesModelList.get(position).getSelected()){
//                                        adreesses.put("selected_"+x,true);
//                                    }
//                                }else{
//                                    adreesses.put("selected_"+x,false);
//
//                                }
                            }
                        }
                        adreesses.put("list_size",x);

                        final int finalSelected = selected;
                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES").set(adreesses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    DBqueries.addressesModelList.remove(position);
                                    if (finalSelected == -1) {
                                    }else{
                                        DBqueries.selectedAddress = finalSelected - 1;
                                        DBqueries.addressesModelList.get(preSelectedPosition).setSelected(true);
                                    }
                                    notifyDataSetChanged();

                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });





                        refresh = false;
                    }
                });
                icon.setImageResource(R.drawable.ic_threedots);

                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        editRemoveDialog.show();
//                        optionsContainer.setVisibility(View.VISIBLE);
                        if (refresh) {

                            refreshItem(preSelectedPosition, preSelectedPosition);
                        } else {
                            refresh = true;
                        }
                        preSelectedPosition = position;
                    }
                });

            }

        }
    }
}
