package com.example.shopper;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

import java.util.Objects;
import java.util.jar.Manifest;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.shopper.DBqueries.cartlist;
import static com.example.shopper.DBqueries.fullname;
import static com.example.shopper.HomeFragment.refreshlayout;
import static com.example.shopper.RegisterActivity.setSignupFragment;

public class MainActivity extends AppCompatActivity {
    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    private static final int AllProductsFragmentt = 6;
    public static DrawerLayout drawer;
    private BottomSheetDialog notificationsDialog;
    private BottomSheetDialog signindialog;
    private TextView badgecount;
    public static boolean resetMainActivity = false;
    public static Boolean showCart = false;
    public static Activity mainActivity;
    private CircleImageView profileview;
    private TextView emaill, fulllname;
    private ImageView addprofileBtn;
    AlertDialog.Builder builder2;

    private  ConstraintLayout frameLayout;
    private TextView actionbarlogo;
    private static int currentFragment = -1;
    private Window window;
    private Toolbar toolbar;
    private Button adbtn;
    private FirebaseUser currentUser;


    private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //       swiperefresh = findViewById(R.id.swipe_refresh);


        toolbar = findViewById(R.id.toolbar);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Are you sure to Signout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        DBqueries.clearData();
                        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(registerIntent);
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });

        final AlertDialog alert11 = builder1.create();


         builder2 = new AlertDialog.Builder(MainActivity.this);
        builder2.setMessage("Search by Filters");
        builder2.setCancelable(true);

        builder2.setPositiveButton(
                "Search by title",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(searchIntent);

                    }
                });

        builder2.setNegativeButton(
                "Search by product ID",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent searchbyproductid = new Intent(MainActivity.this,SearchLinkActivity.class);
                        startActivity(searchbyproductid);

                    }
                });
        

        builder2.setNeutralButton(
                "Search by Qr code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, QrScannerActivity.class);
                        startActivity(intent);
                    }
                }
        );
        final AlertDialog alert12 = builder2.create();


        actionbarlogo = findViewById(R.id.action_bar_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();
        toogle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));


        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        profileview = header.findViewById(R.id.nav_name);
        emaill = header.findViewById(R.id.main_email_addressssssss);
        fulllname = header.findViewById(R.id.nav_full_name);
        addprofileBtn = header.findViewById(R.id.adprofileBtnnnn);

        addprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    Intent updateUserInfo = new Intent(MainActivity.this, UpdateUserInfoActivity.class);
                    updateUserInfo.putExtra("Name", fulllname.getText());
                    updateUserInfo.putExtra("Email", emaill.getText());
                    updateUserInfo.putExtra("Profile", DBqueries.profile);
                    startActivity(updateUserInfo);
                } else {
                    signindialog.show();
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                if (currentUser != null) {


                    int id = item.getItemId();
                    if (id == R.id.nav_shopping_fire) {

                        actionbarlogo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
                    } else if (id == R.id.nav_my_orders) {
                        gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
                    } else if (id == R.id.nav_my_rewards) {
                        gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);
                    } else if (id == R.id.nav_my_cart) {
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);

                    } else if (id == R.id.nav_my_wishlist) {
                        gotoFragment("My WishList", new MyWishlistFragment(), WISHLIST_FRAGMENT);
                    } else if (id == R.id.nav_my_account) {
                        gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
                    } else if (id == R.id.myLocation) {
                        Intent location = new Intent(MainActivity.this, MyLocationActivity.class);
                        startActivity(location);
                    } else if (id == R.id.nav_username) {
                        Intent updateUserInfo = new Intent(MainActivity.this, MainActivity2.class);
                        updateUserInfo.putExtra("Name", fulllname.getText());
                        updateUserInfo.putExtra("Email", emaill.getText());
                        updateUserInfo.putExtra("Profile", DBqueries.profile);
                        startActivity(updateUserInfo);

                    } else if (id == R.id.nav_signout) {
                        alert11.show();
                    } else if (id == R.id.nav_recent) {
                        Intent passwordIntent = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(passwordIntent);

                    } else if (id == R.id.changePassword) {
                        alert12.show();
                    } else if (id == R.id.nav_privacyPolicy) {
                        Intent privacyIntent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                        startActivity(privacyIntent);

                    } else if (id == R.id.nav_terms) {
                        Intent privacyIntent = new Intent(MainActivity.this, TermsAndConditionActivity.class);
                        startActivity(privacyIntent);

                    } else if (id == R.id.nav_help) {
                        Intent privacyIntent = new Intent(MainActivity.this, HelpCenterActivity.class);
                        startActivity(privacyIntent);

                    } else if (id == R.id.nav_share) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "share Demo");
                            String sharemessage = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                            intent.putExtra(Intent.EXTRA_TEXT, sharemessage);
                            startActivity(Intent.createChooser(intent, "Share By"));
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "An Unexpected error occured!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    return true;

                } else {
                    signindialog.show();
                    return false;
                }
            }

        });
        navigationView.getMenu().getItem(0).setChecked(true);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        frameLayout = findViewById(R.id.main_frame_layout);
        setFragment(new HomeFragment(), HOME_FRAGMENT);
        signindialog = new BottomSheetDialog(MainActivity.this);
        signindialog.setContentView(R.layout.sign_in_dialog);
        signindialog.setCancelable(true);

        signindialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogsigninBtn = signindialog.findViewById(R.id.login_btn);
        Button dialogsignupBtn = signindialog.findViewById(R.id.signup_btn);
        dialogsigninBtn.setOnClickListener(new View.OnClickListener() {
            final Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);

            @Override
            public void onClick(View view) {
                SignInFragment.disableclodebtn = true;
                SignUpFragment.disableclodebtn = true;
                signindialog.dismiss();
                setSignupFragment = false;
                startActivity(registerIntent);

            }
        });
//        Bundle intentt = getIntent().getExtras();
//        if (intentt != null){
//            String publisher = intentt.getString("publisherid");
//            SharedPreferences.Editor editor =  getSharedPreferences("PREFS",MODE_PRIVATE).edit();
//            editor.putString("profileid",publisher);
//            editor.apply();
//            setFragment(new MyAccountFragment(),ACCOUNT_FRAGMENT);
//        }
        dialogsignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableclodebtn = true;
                SignUpFragment.disableclodebtn = true;
                signindialog.dismiss();
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                setSignupFragment = true;
                startActivity(registerIntent);

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setVisibility(View.VISIBLE);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 4).setEnabled(false);
        } else {
                if (currentUser != null) {
                    FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())

                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                DBqueries.fullname = task.getResult().getString("fullname");
                                DBqueries.email = task.getResult().getString("email");
                                DBqueries.profile = task.getResult().getString("profile");

                                fulllname.setText(DBqueries.fullname);
                                emaill.setText(DBqueries.email);
                                if (DBqueries.profile.equals("")) {
                                    addprofileBtn.setVisibility(View.VISIBLE);
                                } else {
                                    addprofileBtn.setVisibility(View.GONE);

                                    Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.ic_profile_blankkkkkkkk)).into(profileview);
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);

        }



        if(resetMainActivity)

    {
        actionbarlogo.setVisibility(View.VISIBLE);
        setFragment(new HomeFragment(), HOME_FRAGMENT);
        navigationView.getMenu().getItem(0).setChecked(true);

    }


    invalidateOptionsMenu();

}

    @Override
    public void onBackPressed() {
        toolbar.setVisibility(View.VISIBLE);
        DBqueries.clearData();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == 0 && currentFragment == HOME_FRAGMENT) {
                DBqueries.clearData();
                finish();
                System.exit(0);
            } else {

                if (currentFragment != HOME_FRAGMENT || currentFragment == ACCOUNT_FRAGMENT) {
                    DBqueries.clearData();


                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                    actionbarlogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                }


            }
        }
        invalidateOptionsMenu();

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (currentUser != null) {
//            DBqueries.checkNotifications(true, null);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            MenuItem cartItem = menu.findItem(R.id.main_cart);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeicon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeicon.setImageResource(R.drawable.ic_cart_white);
            badgecount = cartItem.getActionView().findViewById(R.id.badge_count);


            if (currentUser != null) {


                if (DBqueries.cartlist.size() == 0) {
                    badgecount.setVisibility(View.GONE);
                    DBqueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this), false, new TextView(MainActivity.this));
                } else {
                    badgecount.setVisibility(View.VISIBLE);
                    if (DBqueries.cartlist.size() < 99) {
                        badgecount.setText(String.valueOf(DBqueries.cartlist.size()));
                    } else {
                        badgecount.setText("99+");
                    }
                }
            }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser == null) {
                        signindialog.show();
                    } else {
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);

                    }
                }
            });
//            MenuItem notificationIcon = menu.findItem(R.id.main_notifications);
//            notificationIcon.setActionView(R.layout.badge_layout);
//            ImageView notifyicon = notificationIcon.getActionView().findViewById(R.id.badge_icon);
//            notifyicon.setImageResource(R.drawable.ic_notifications_white);
//            TextView notifycount = notificationIcon.getActionView().findViewById(R.id.badge_count);
//
//            if (currentUser != null) {
//                DBqueries.checkNotifications(false, notifycount);
//
//            }
//            notificationIcon.getActionView().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent notificationsIntent = new Intent(MainActivity.this, NotificationsActivity.class);
//                    startActivity(notificationsIntent);
//
//                }
//            });
        }
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_search) {
            AlertDialog alert12 = builder2.create();
            alert12.show();
            return true;
        } else if (id == R.id.main_notifications) {

           setFragment(new MyOrdersFragment(),2);
            return true;

        } else if (id == R.id.main_cart) {
            if (currentUser == null) {
                signindialog.show();
            } else {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);

            }
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
        actionbarlogo.setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);

        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);
        if (fragmentNo == CART_FRAGMENT) {
            navigationView.getMenu().getItem(3).setChecked(true);

        }
    }


    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            if (fragmentNo == REWARDS_FRAGMENT) {
                window.setStatusBarColor(Color.parseColor("#E4045F"));
                toolbar.setBackgroundColor(Color.parseColor("#E4045F"));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }


}