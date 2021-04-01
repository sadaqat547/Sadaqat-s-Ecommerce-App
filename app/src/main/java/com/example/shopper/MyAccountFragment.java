package com.example.shopper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyAccountFragment extends Fragment {
    private ImageButton viewAllAddressBtn;
    private TextView aaaa;
    public static final int MANAGE_ADDRESS = 1;


    private CircleImageView profileview;
    private LinearLayout linearLayout;
    private TextView name, email;
    private Dialog loadindDialog;
    private ImageView setting;


    private TextView recentOrderTitle;
    private RecyclerView recentOrderRecyclerview;
    private ConstraintLayout currentOrderConstraint;


    private TextView fullnameaddress, addresspincode, address, viewAllAddresstext;
    private ImageButton viewallBtn;
    private Button signOutBtn,delteAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        ////loading dialog

        loadindDialog = new Dialog(getContext());
        loadindDialog.setContentView(R.layout.loading_progress_dialog);
        loadindDialog.setCancelable(false);
//        loadindDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background_light));
        loadindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadindDialog.show();
        ////loading dialog
        viewAllAddressBtn = view.findViewById(R.id.viewallAddress);
        aaaa = view.findViewById(R.id.horizontlviewalltext);
        profileview = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.username);
        email = view.findViewById(R.id.useremail);
        setting = view.findViewById(R.id.settingBtn);
        linearLayout = view.findViewById(R.id.layout_container);
        recentOrderTitle = view.findViewById(R.id.recentorderstitle);
        recentOrderRecyclerview = view.findViewById(R.id.recent_orderRecyclerView);
        signOutBtn = view.findViewById(R.id.signoutbtn);
        delteAccount = view.findViewById(R.id.deleteAccountbtn);

        delteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteIntent = new Intent(getContext(),DeleteAccountActivity.class);
                deleteIntent.putExtra("Email", email.getText());
                startActivity(deleteIntent);

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recentOrderRecyclerview.setLayoutManager(layoutManager);
//        if (DBqueries.myOrderitemModelList.size() == 0){
//            loadindDialog.dismiss();
//            recentOrderRecyclerview.setVisibility(View.GONE);
//            recentOrderTitle.setVisibility(View.GONE);
//        }else {
//            recentOrderRecyclerview.setVisibility(View.VISIBLE);
//            recentOrderTitle.setVisibility(View.VISIBLE);
//        }
        MyOrderAdapter myOrderAdapterr = new MyOrderAdapter(DBqueries.myOrderitemModelList);
        recentOrderRecyclerview.setAdapter(myOrderAdapterr);
        DBqueries.loadOrders(getContext(),myOrderAdapterr,loadindDialog);



            recentOrderTitle.setText("Your recent orders");

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                DBqueries.clearData();
                Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
                startActivity(registerIntent);
                getActivity().finish();
            }
        });
        fullnameaddress = view.findViewById(R.id.address_fullname);
        addresspincode = view.findViewById(R.id.address_pincode);
        address = view.findViewById(R.id.addressss);

        viewAllAddressBtn = view.findViewById(R.id.viewallAddress);
        viewAllAddresstext = view.findViewById(R.id.viewalltextadddress);


//        linearLayout.getChildAt(1).setVisibility(View.GONE);
        loadindDialog.dismiss();
        DBqueries.loadOrders(getContext(), myOrderAdapterr, loadindDialog);
//        loadindDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {



        loadindDialog.show();
        loadindDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {


                loadindDialog.setOnDismissListener(null);

                if (DBqueries.addressesModelList.size() == 0) {
                    fullnameaddress.setText("No address");
                    addresspincode.setText("-");
                    address.setText("-");
                } else {

                    setAddress();
//
                }


            }
        });
        DBqueries.loadAddresses(getContext(), loadindDialog, false);
//            }
//        });


        viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myAddressesIntent = new Intent(getContext(), MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE", MANAGE_ADDRESS);
                startActivity(myAddressesIntent);

            }
        });
//        profileview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent updateUserInfo = new Intent(getContext(), UpdateUserInfoActivity.class);
//                updateUserInfo.putExtra("Name", name.getText());
//                updateUserInfo.putExtra("Email", email.getText());
//                updateUserInfo.putExtra("Profile", DBqueries.profile);
//                startActivity(updateUserInfo);
//            }
//        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateUserInfo = new Intent(getContext(), MainActivity2.class);
                updateUserInfo.putExtra("Name", name.getText());
                updateUserInfo.putExtra("Email", email.getText());
                updateUserInfo.putExtra("Profile", DBqueries.profile);
                startActivity(updateUserInfo);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        name.setText(DBqueries.fullname);
        email.setText(DBqueries.email);

        if (!DBqueries.profile.equals("")) {

            Glide.with(getContext()).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round)).into(profileview);
        } else {
            profileview.setImageResource(R.mipmap.ic_launcher);
        }
        if (!loadindDialog.isShowing()) {
            if (DBqueries.addressesModelList.size() == 0) {
                fullnameaddress.setText("No address");
                addresspincode.setText("-");
                address.setText("-");
            } else {
                setAddress();
//
            }
        }
    }

    private void setAddress() {
        String nametext = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        String mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        String flatno = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getStatespinner();
        String country = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCountryspinner();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateNo().equals("")) {
            fullnameaddress.setText(nametext + " - " + mobileNo);
        } else {
            fullnameaddress.setText(nametext + " - " + mobileNo + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateNo());

        }
        if (landmark.equals("")) {
            address.setText(flatno + " - " + locality + " - " + city + " - " + state + " - " + country);

        } else {
            address.setText(flatno + " - " + locality + " - " + landmark + " - " + city + " - " + state + " - " + country);

        }
        addresspincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

//
    }


}