package com.example.shopper;


public class CategoryModel {
    private  String CategotyIconLink;
    private String categoryname;

    public CategoryModel(String categotyIconLink, String categoryname) {
        CategotyIconLink = categotyIconLink;
        this.categoryname = categoryname;
    }

    public String getCategotyIconLink() {
        return CategotyIconLink;
    }

    public void setCategotyIconLink(String categotyIconLink) {
        CategotyIconLink = categotyIconLink;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
}
