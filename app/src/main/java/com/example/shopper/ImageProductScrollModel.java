package com.example.shopper;

public class ImageProductScrollModel {
    private String productID;
    private String productimage;

    public ImageProductScrollModel(String productID, String productimage) {
        this.productID = productID;
        this.productimage = productimage;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }
}

