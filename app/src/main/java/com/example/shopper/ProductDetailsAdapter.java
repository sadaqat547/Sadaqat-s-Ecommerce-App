package com.example.shopper;


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ProductDetailsAdapter extends FragmentPagerAdapter {
    private int totaltabs;
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductDetailsAdapter(@NonNull FragmentManager fm, int totaltabs, String productDescription, String productOtherDetails, List<ProductSpecificationModel> productSpecificationModelList) {
        super(fm);
        this.totaltabs = totaltabs;
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                ProductDescriptionFragment  productDescriptionFragment = new ProductDescriptionFragment();
                productDescriptionFragment.productDescription = productDescription;
                return productDescriptionFragment;
            case 1:
                ProductSpecificationFragment  productSpecificationFragment = new ProductSpecificationFragment();
                productSpecificationFragment.productSpecificationModelList = productSpecificationModelList;
                return productSpecificationFragment;
            case 2:
                ProductDescriptionFragment  productDescriptionFragment1 = new ProductDescriptionFragment();
                productDescriptionFragment1.productDescription = productOtherDetails;
                return productDescriptionFragment1;
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return totaltabs;
    }
}
