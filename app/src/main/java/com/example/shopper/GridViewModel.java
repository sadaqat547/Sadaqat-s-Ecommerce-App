package com.example.shopper;

public class GridViewModel {
    private String productID;
    private String productimage;
    private String productTitle;
    private String productdescription;
    private String productPrice;

    public GridViewModel(String productID, String productimage, String productTitle, String productdescription, String productPrice) {
        this.productID = productID;
        this.productimage = productimage;
        this.productTitle = productTitle;
        this.productdescription = productdescription;
        this.productPrice = productPrice;
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

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
