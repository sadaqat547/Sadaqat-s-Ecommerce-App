package com.example.shopper;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private boolean fromSearch;
    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int lastPosition = -1;


    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public WishlistAdapter(List<WishlistModel> wishlistModelList, Boolean wishllist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishllist;
    }

    public List<WishlistModel> getWishlistModelList() {
        return wishlistModelList;
    }

    public void setWishlistModelList(List<WishlistModel> wishlistModelList) {
        this.wishlistModelList = wishlistModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_items_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String productid = wishlistModelList.get(position).getProductId();
        String productimage = wishlistModelList.get(position).getProductImage();
        String title = wishlistModelList.get(position).getProductTitle();
        long freeCoupen = wishlistModelList.get(position).getFreeCoupens();
        String rating = wishlistModelList.get(position).getRating();
        long totalRatings = wishlistModelList.get(position).getTotalRatings();
        String productPrice = wishlistModelList.get(position).getProductPrice();
        String cuttedprice = wishlistModelList.get(position).getCuttedPrice();
        boolean COD = wishlistModelList.get(position).isCOD();
        boolean inStock = wishlistModelList.get(position).isInStock();

        viewHolder.setData(productid, productimage, title, freeCoupen, rating, totalRatings, productPrice, cuttedprice, COD, position,inStock);
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.slide_from_left_slow);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }


    }


    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupen;
        private ImageView CoupenIcon;
        private TextView rating;
        private TextView totalRatings;
        private View priceCut;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView paymentMethod;
        private ImageButton deleteBtn;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupen = itemView.findViewById(R.id.free_coupons);
            CoupenIcon = itemView.findViewById(R.id.coupen_icon);
            rating = itemView.findViewById(R.id.rating);
            totalRatings = itemView.findViewById(R.id.total_ratings);
            priceCut = itemView.findViewById(R.id.price_cut);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            paymentMethod = itemView.findViewById(R.id.payment_options);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
        }


        private void setData(final String productid, String resource, String title, long freeCoupenNo, String averageRate, long totalRatingsNo, final String price, String cuttedPriceValue, boolean COD, final int index,boolean inStock) {
            productTitle.setText(title);
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.slider_background_light)).into(productImage);

            if (freeCoupenNo != 0 && inStock) {
                CoupenIcon.setVisibility(View.VISIBLE);
                if (freeCoupenNo == 1) {
                    freeCoupen.setText("free " + freeCoupenNo + " coupen");
                } else {
                    freeCoupen.setText("free " + freeCoupenNo + " coupens");


                }
            } else {
                CoupenIcon.setVisibility(View.INVISIBLE);
                freeCoupen.setVisibility(View.INVISIBLE);
            }
            LinearLayout linearLayout = (LinearLayout) rating.getParent();
            if (inStock){
                rating.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                cuttedPrice.setVisibility(View.VISIBLE);
                productPrice.setTextColor(itemView.getResources().getColor(R.color.black));
                rating.setText(averageRate);
                totalRatings.setText("(" + totalRatingsNo + ") ratings");
                if (wishlist) {
                    productPrice.setText("Rs." + price + "/-");
                }else{
                    productPrice.setText(price);

                }
                if (wishlist) {
                    cuttedPrice.setText("Rs."+cuttedPriceValue+"/-");
                }else{
                    cuttedPrice.setText(cuttedPriceValue);

                }
                    if (COD) {
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    paymentMethod.setVisibility(View.INVISIBLE);

                }
                linearLayout.setVisibility(View.VISIBLE);

            }else{
                linearLayout.setVisibility(View.INVISIBLE);
                rating.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                cuttedPrice.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getResources().getColor(R.color.progreesbarred));
                paymentMethod.setVisibility(View.INVISIBLE);

            }


            if (wishlist) {
                deleteBtn.setVisibility(View.VISIBLE);

            } else {
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ProductDetailsActivity.running_wishlist_query) {
                        ProductDetailsActivity.running_wishlist_query = true;
                        DBqueries.removefromWishlist(index, itemView.getContext());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fromSearch){
                        ProductDetailsActivity.fromSearch = true;
                    }
                    Intent producctDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    producctDetailsIntent.putExtra("product_ID", productid);
                    itemView.getContext().startActivity(producctDetailsIntent);
                }
            });
        }
    }
}











