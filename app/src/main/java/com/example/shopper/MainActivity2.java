package com.example.shopper;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity2 extends AppCompatActivity {
    private ConstraintLayout frameLayout;
    private UpdateUserInfo updateinfoFragment;
    private UpdatePassword updatePassword;
    private String name;
    private String email;
    private String profile;
    private String cover;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        frameLayout = findViewById(R.id.frameLayoutttttt);
        updateinfoFragment = new UpdateUserInfo();
        updatePassword = new UpdatePassword();

        navigationView = findViewById(R.id.nav_view);

        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        profile = getIntent().getStringExtra("Profile");

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_change_email, R.id.navigation_change_password)
                .build();
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigationView.getMenu().getItem(0).setChecked(true);
                setFragment(updateinfoFragment,true);
                if (!FirebaseAuth.getInstance().getCurrentUser().equals(email)){
                    int id = item.getItemId();
                    if (id == R.id.navigation_change_email){
                        navigationView.getMenu().getItem(0).setChecked(true);

                        setFragment(updateinfoFragment,true);

                    }else if (id == R.id.navigation_change_password){
                        navigationView.getMenu().getItem(1).setChecked(true);
                        setFragment(updatePassword,false);

                    }
                }

                return false;


            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.getMenu().getItem(0).setChecked(true);
        setFragment(updateinfoFragment,true);
    }

    private void setFragment(Fragment fragment, boolean setBundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (setBundle) {
            Bundle bundle = new Bundle();
            bundle.putString("Name", name);
            bundle.putString("Email", email);
            bundle.putString("Profile", profile);
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