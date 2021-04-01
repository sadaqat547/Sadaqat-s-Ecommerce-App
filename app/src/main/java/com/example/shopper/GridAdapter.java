package com.example.shopper;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private List<GridViewModel> gridViewModelList;

    public GridAdapter(List<GridViewModel> gridViewModelList) {
        this.gridViewModelList = gridViewModelList;
    }

    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout_item,viewGroup,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder viewHolder, int position) {
        String resource =  gridViewModelList.get(position).getProductimage();
        String title = gridViewModelList.get(position).getProductTitle();
        String description = gridViewModelList.get(position).getProductdescription();
        String price = gridViewModelList.get(position).getProductPrice();
        String productid = gridViewModelList.get(position).getProductID();

        viewHolder.setData(productid,resource,title,description,price);
    }

    @Override
    public int getItemCount() {
        return gridViewModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle;
        private TextView productdescription;
        private TextView productprice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.hs_productimage);
            productTitle = itemView.findViewById(R.id.hs_product_title);
            productdescription = itemView.findViewById(R.id.hs_product_description);
            productprice = itemView.findViewById(R.id.hs_product_price);

        }
        private void setData(final String productid, String resource, final String title, String description, String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.slider_background_light)).into(productImage);
            productprice.setText(price);
            productdescription.setText(description);
            productTitle.setText(title);

            if (!title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                        productDetailsIntent.putExtra("product_ID",productid);
                        productDetailsIntent.putExtra("title",title);
                        itemView.getContext().startActivity(productDetailsIntent);
                    }
                });
            }

        }
    }

}
