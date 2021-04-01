package com.example.shopper;


import android.content.Intent;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;





    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {

        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder viewHolder, int position) {
        String resource =  horizontalProductScrollModelList.get(position).getProductimage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String description = horizontalProductScrollModelList.get(position).getProductdescription();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();
        String productid = horizontalProductScrollModelList.get(position).getProductID();

        viewHolder.setData(productid,resource,title,description,price);
    }

    @Override
    public int getItemCount() {

        if (horizontalProductScrollModelList.size() > 7 ){
            return 8;

        }else {
            return horizontalProductScrollModelList.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView productdescription;
        private TextView productprice;
        private ImageButton horizontalviewallbtn;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.hs_productimage);
            productTitle = itemView.findViewById(R.id.hs_product_title);
            productdescription = itemView.findViewById(R.id.hs_product_description);
            productprice = itemView.findViewById(R.id.hs_product_price);
            horizontalviewallbtn = itemView.findViewById(R.id.horizontalviewallbtn);








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
