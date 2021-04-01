package com.example.shopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;

public class MyAppIntro extends AppIntro {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance(
                "Welcome to SHOPPING FIRE!",
                 "Best Shopping Platform Provided by CODING CREATORS!",
                R.mipmap.ic_launcher,

                ContextCompat.getColor(getApplicationContext(),R.color.black)));
        addSlide(AppIntroFragment.newInstance(
                "Easy Access",
                "You can Login/Signup by Easy Steps!",
                R.mipmap.ic_launcher,

                ContextCompat.getColor(getApplicationContext(),R.color.black)));
        addSlide(AppIntroFragment.newInstance(
                "Free Rewards",
                "You can get free Coupons by refer a friend! EveryDay. EveryTime.",
                R.mipmap.ic_launcher,


                ContextCompat.getColor(getApplicationContext(),R.color.black)));


        sharedPreferences=getApplicationContext().getSharedPreferences("MyRefrences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        if (sharedPreferences != null){
            boolean checkShared = sharedPreferences.getBoolean("checkState",false);
            if (checkShared == true){
                startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                finish();
            }
        }
    }
    @Override
    public  void  onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(),SplashActivity.class));
        editor.putBoolean("checkState",false).commit();

        finish();
    }

    @Override
    public  void  onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(),SplashActivity.class));
        editor.putBoolean("checkState",true).commit();
        finish();

    }

}