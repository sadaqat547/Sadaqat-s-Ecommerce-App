package com.example.shopper;


import java.util.List;

public class HomePageModel {
    public static final int BANNER_SLIDER = 0;
    public static final int STRIP_AD_BANNER = 1;
    public static final int HORIZONTAL_PRODUCT_VIEW = 2;


    private int type;
    private String backgrndcolor;

    /////baner slider
    private List<SliderModel> sliderModelList;

    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }

    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    /////baner slider


    /////strip_ad_layout
    private String resource;
    private String productId;
    public HomePageModel(int type, String resource, String backgrndcolor,String productId) {
        this.type = type;
        this.resource = resource;
        this.backgrndcolor = backgrndcolor;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getBackgrndcolor() {
       return backgrndcolor;
    }

    public void setBackgrndcolor(String backgrndcolor) {
        this.backgrndcolor = backgrndcolor;
         }
    /////strip_ad_layout

    ///// imageProduct
    ///// imageProduct
    ////horizontalprodyct layout  && grid
    private String title;
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;


    ////horizonytal
    private List<WishlistModel> viewAllProductList;

    public HomePageModel(int type, String title, String backgrndcolor, List<HorizontalProductScrollModel> horizontalProductScrollModelList, List<WishlistModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgrndcolor = backgrndcolor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.viewAllProductList = viewAllProductList;
    }

    public List<WishlistModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<WishlistModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }

    private String imageRes;


    ///horizontal

    public String getImageRes() {
        return imageRes;
    }

    public void setImageRes(String imageRes) {
        this.imageRes = imageRes;
    }


    //grid

    public HomePageModel(int type, String title, String backgrndcolor, List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.backgrndcolor = backgrndcolor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;

    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }

    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }


    /////grid





}
