package com.example.shopper;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ProductSpecificationFragment extends Fragment {
    public static List<ProductSpecificationModel> productSpecificationModelList;
    private RecyclerView productSpecificationRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_product_specification, container, false);
        productSpecificationRecyclerView = view.findViewById(R.id.product_specification_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

       productSpecificationModelList = new ArrayList<>();
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"General"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"Title"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"General"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"Title"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","16GB"));
//



        productSpecificationRecyclerView.setLayoutManager(linearLayoutManager);

        ProductSpecifictionAdaptor productSpecifictionAdaptor = new ProductSpecifictionAdaptor(productSpecificationModelList  );
        productSpecificationRecyclerView.setAdapter(productSpecifictionAdaptor);
        productSpecifictionAdaptor.notifyDataSetChanged();

        return view;
    }
}