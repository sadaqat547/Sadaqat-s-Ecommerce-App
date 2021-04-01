package com.example.shopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserInfoActivity extends AppCompatActivity {

   private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private UpdateUserInfo updateinfoFragment;
    private UpdatePassword updatePassword;
    private String name;
    private String email;
    private String profile;
    private String cover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
//        tabLayout = findViewById(R.id.tab_layoutttt);
        frameLayout = findViewById(R.id.frameLayoutttttt);
        updateinfoFragment = new UpdateUserInfo();
        updatePassword = new UpdatePassword();


        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        profile = getIntent().getStringExtra("Profile");
        cover = getIntent().getStringExtra("Cover");



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFragment(updateinfoFragment,true);
                if (tab.getPosition() == 0) {
                    setFragment(updateinfoFragment,true);

                }else
                    {
                        setFragment(updatePassword,false);
                    }


//

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setFragment(updateinfoFragment,true);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setFragment(updateinfoFragment,true);

            }
        });


    }

    private void setFragment(Fragment fragment, boolean setBundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (setBundle) {
            Bundle bundle = new Bundle();
            bundle.putString("Name", name);
            bundle.putString("Email", email);
            bundle.putString("Profile", profile);
            bundle.putString("Cover", cover);
            fragment.setArguments(bundle);
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("Email", email);
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

}