package com.example.shopper;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductSpecifictionAdaptor extends RecyclerView.Adapter<ProductSpecifictionAdaptor.ViewHolder> {
    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductSpecifictionAdaptor(List<ProductSpecificationModel> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @NonNull
    @Override
    public ProductSpecifictionAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {

            case ProductSpecificationModel.SPECIFICATION_TITLE:
                TextView title = new TextView(viewGroup.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDP(16, viewGroup.getContext())
                        , setDP(16, viewGroup.getContext())
                        , setDP(16, viewGroup.getContext())
                        , setDP(8, viewGroup.getContext()));
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);
            case ProductSpecificationModel.SPECIFICATION_BODY:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_specification_item_layout, viewGroup, false);
                return new ViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case 0:
                return ProductSpecificationModel.SPECIFICATION_TITLE;
            case 1:
                return ProductSpecificationModel.SPECIFICATION_BODY;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecifictionAdaptor.ViewHolder viewHolder, int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                viewHolder.setTitle(productSpecificationModelList.get(position).getTitle());


                break;
            case ProductSpecificationModel.SPECIFICATION_BODY:
                String featureTitle = productSpecificationModelList.get(position).getFeaturename();
                String featureDetails = productSpecificationModelList.get(position).getFeaturevalue();
                viewHolder.setFeatures(featureTitle, featureDetails);
                break;
            default:
                return;
        }


    }


    @Override
    public int getItemCount() {
        return productSpecificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView featurename;
        private TextView featurevalue;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

        }

        private void setFeatures(String featureTitle, String featureDetails) {
            featurename = itemView.findViewById(R.id.featureName);
            featurevalue = itemView.findViewById(R.id.featureValue);
            featurename.setText(featureTitle);
            featurevalue.setText(featureDetails);

        }

        private void setTitle(String titleText) {
            title = (TextView) itemView;
            title.setText(titleText);
        }
    }

    private int setDP(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}