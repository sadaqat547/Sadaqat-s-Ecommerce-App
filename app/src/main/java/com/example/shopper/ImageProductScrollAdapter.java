package com.example.shopper;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ImageProductScrollAdapter extends RecyclerView.Adapter<ImageProductScrollAdapter.ViewHolder> {

    private List<ImageProductScrollModel> imageProductScrollModelList;

    public ImageProductScrollAdapter(List<ImageProductScrollModel> imageProductScrollModelList) {
        this.imageProductScrollModelList = imageProductScrollModelList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_image_layout_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageProductScrollAdapter.ViewHolder viewHolder, int position) {
        String resource =  imageProductScrollModelList.get(position).getProductimage();
        String productid = imageProductScrollModelList.get(position).getProductID();
        viewHolder.setData(productid,resource);
    }

    @Override
    public int getItemCount() {
        return imageProductScrollModelList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.pi_product_image);

        }

        private void setData(final String productid, String resource) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.slider_background_light)).into(productImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("product_ID", productid);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });


        }
    }
}
