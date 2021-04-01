package com.example.shopper;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {
    private List<HomePageModel> homePageModelList;
    private int lastPosition = -1;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;

    }


    @Override
    public int getItemViewType(int position) {

        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_AD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;


            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View bannersliderview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sliding_ad_banner, viewGroup, false);
                return new BannerSliderViewHolder(bannersliderview);
            case HomePageModel.STRIP_AD_BANNER:
                View stripadview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.strip_ad_layout, viewGroup, false);
                return new StripadbannerViewholder(stripadview);
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalproductview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout, viewGroup, false);
                return new HorizontalproductviewHolder(horizontalproductview);

//
                default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) viewHolder).setBannerSliderViewPager(sliderModelList);
                break;

            case HomePageModel.STRIP_AD_BANNER:
                String resource = homePageModelList.get(position).getResource();
                String color = homePageModelList.get(position).getBackgrndcolor();
                String productId = homePageModelList.get(position).getProductId();
                ((StripadbannerViewholder) viewHolder).setStripad(resource, color,productId);
                break;


            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                String layoutcolor = homePageModelList.get(position).getBackgrndcolor();
                String horizontallayouttitle = homePageModelList.get(position).getTitle();
                List<WishlistModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((HorizontalproductviewHolder) viewHolder).setHorizontalProductLayout(horizontalProductScrollModelList, horizontallayouttitle, layoutcolor, viewAllProductList);


                break;
//
               default:
                return;

//            case HomePageModel.GRID_PRODUCT_VIEW:
////                String Resource = homePageModelList .get(position).getResource();
////                String Title = homePageModelList.get(position).getTitle();
////                String Description = homePageModelList.get(position).getHorizontalProductScrollModelList().toString();
////                String Price = homePageModelList.get(position).getHorizontalProductScrollModelList().toString();
////                String gridLayouttitle = homePageModelList.get(position).getTitle();
////                List<HorizontalProductScrollModel> gridProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
////                ((GridProductviewHolder) viewHolder).setGridProductLayout(gridProductScrollModelList,gridLayouttitle);
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.slide_from_left_slow);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();

    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        private ViewPager bannerSliderViewPager;
        private int currentPage;
        private Timer timer;
        final private long DELAY_TIME = 2000;
        final private long PERIOD_TIME = 2000;
        private List<SliderModel> arrangedList;

        public BannerSliderViewHolder(@NonNull View itemView) {


            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.banner_viewpager);


        }

        private void setBannerSliderViewPager(final List<SliderModel> sliderModelList) {
            currentPage = 2;
            if (timer != null) {
                timer.cancel();
            }
            arrangedList = new ArrayList<>();
            for (int x = 0; x < sliderModelList.size(); x++) {
                arrangedList.add(x, sliderModelList.get(x));
            }
            arrangedList.add(0, sliderModelList.get(sliderModelList.size() - 2));
            arrangedList.add(1, sliderModelList.get(sliderModelList.size() - 1));
            arrangedList.add(sliderModelList.get(0));
            arrangedList.add(sliderModelList.get(1));


            SliderAdapter sliderAdapter = new SliderAdapter(arrangedList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);

            bannerSliderViewPager.setCurrentItem(currentPage);
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int i, float v, int i1) {
//
                }

                @Override
                public void onPageSelected(int i) {
                    currentPage = i;


                }


                @Override
                public void onPageScrollStateChanged(int i) {
                    if (i == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(arrangedList);
                    }
                }
            };

            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
            startbannerslideshow(arrangedList);
            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {

                    pageLooper(arrangedList);
                    stopbannerslidershow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startbannerslideshow(arrangedList);
                    }
                    return false;
                }
            });
        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (currentPage == sliderModelList.size() - 2) {
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }

        }

        private void startbannerslideshow(final List<SliderModel> sliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()) {
                        currentPage = 1;
                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME);
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    handler.post(update);
            //               }
//            }, DELAY_TIME, PERIOD_TIME);


        }

        private void stopbannerslidershow() {
            timer.cancel();
        }
    }

    public class StripadbannerViewholder extends RecyclerView.ViewHolder {
        private ImageView stripadimage;
        private ConstraintLayout stripAdContainer;

        public StripadbannerViewholder(@NonNull View itemView) {
            super(itemView);
            stripadimage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
        }

        private void setStripad(String resource, String color, final String productId) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.slider_light_small)).into(stripadimage);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("product_ID",productId);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }

    }

    public class HorizontalproductviewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout horizontal_container;
        private TextView horizontallayouttitle;
        private ImageButton horizontallayoutviewallbtn;
        private RecyclerView horizontalrecyclerview;
        private TextView horizontallayoutviewalltext;

        public HorizontalproductviewHolder(@NonNull View itemView) {
            super(itemView);

            horizontal_container = itemView.findViewById(R.id.horizontal_container);
            horizontallayouttitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontallayoutviewallbtn = itemView.findViewById(R.id.horizontalviewallbtn);
            horizontalrecyclerview = itemView.findViewById(R.id.horizontal_scrolllayout_recyclerview);
            horizontallayoutviewalltext = itemView.findViewById(R.id.horizontlviewalltext);
        }

        private void setHorizontalProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color, final List<WishlistModel> viewAllProductList) {
            horizontal_container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontallayouttitle.setText(title);

            for (final HorizontalProductScrollModel model : horizontalProductScrollModelList) {
                if (!model.getProductID().isEmpty() && model.getProductPrice().isEmpty()) {
                    firebaseFirestore.collection("PRODUCTS").document(model.getProductID()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        model.setProductTitle(task.getResult().getString("product_title"));
                                        model.setProductdescription(task.getResult().getString("product_description"));
                                        model.setProductPrice(task.getResult().getString("product_price"));
                                        model.setProductimage(task.getResult().getString("product_image_1"));

                                        WishlistModel wishlistModel = viewAllProductList.get(horizontalProductScrollModelList.indexOf(model));

                                        wishlistModel.setTotalRatings(task.getResult().getLong("total_rating"));
                                        wishlistModel.setRating(task.getResult().getString("average_rating"));
                                        wishlistModel.setProductImage(task.getResult().getString("product_image_1"));
                                        wishlistModel.setProductTitle(task.getResult().getString("product_title"));
                                        wishlistModel.setProductPrice(task.getResult().getString("product_price"));
                                        wishlistModel.setCuttedPrice(task.getResult().getString("cutted_price"));
                                        wishlistModel.setFreeCoupens(task.getResult().getLong("free_coupens"));
                                        wishlistModel.setCOD(task.getResult().getBoolean("COD"));
                                        wishlistModel.setInStock(task.getResult().getLong("stock_quantity") > 0);
                                        if (horizontalProductScrollModelList.indexOf(model) == horizontalProductScrollModelList.size() - 1) {
                                            if (horizontalrecyclerview.getAdapter() != null) {
                                                horizontalrecyclerview.getAdapter().notifyDataSetChanged();
                                            }
                                        }
                                    } else {
                                        ////do nothing
                                    }
                                }
                            });
                }
            }
            if (horizontalProductScrollModelList.size() > 8) {
                horizontallayoutviewallbtn.setVisibility(View.VISIBLE);
                horizontallayoutviewalltext.setVisibility(View.VISIBLE);

                horizontallayoutviewallbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewAllActivity.wishlistModelList = viewAllProductList;
                        Intent viewAllintent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllintent.putExtra("title",title);
                        itemView.getContext().startActivity(viewAllintent);
                    }
                });
            } else {
                horizontallayoutviewallbtn.setVisibility(View.INVISIBLE);
                horizontallayoutviewalltext.setVisibility(View.INVISIBLE);
            }

            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalrecyclerview.setLayoutManager(linearLayoutManager);

            horizontalrecyclerview.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }
    }



}
//
//
//


