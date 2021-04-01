package com.example.shopper;


import java.util.Date;

public class MyOrderitemModel {
    private String productId;
    private String productImage;
    private String productTitle;

    private String orderStatus;
    private String Address;
    private String CoupenId;
    private String CuttedPrice;
    private Date OrdeedrDate;
    private Date PackedDate;
    private Date Shippeddate;
    private Date DeliveredDate;
    private Date CancelledDate;
    private String DiscountedPrice;
    private Long Freecoupens;
    private String Fullname;
    private String OrderId;
    private String PaymentMethod;
    private String Pincode;
    private String ProductPrice;
    private Long ProductQty;
    private String userId;
    private String deliveryPrice;
    private Boolean cancellationRequested;


    public MyOrderitemModel(String deliveryPrice, String productId, String productImage, String productTitle, String orderStatus, String address, String coupenId, String cuttedPrice, Date ordeedrDate, Date packedDate, Date shippeddate, Date deliveredDate, Date cancelledDate, String discountedPrice, Long freecoupens, String fullname, String orderId, String paymentMethod, String pincode, String productPrice, Long productQty, String userId,Boolean cancellationRequested) {
        this.deliveryPrice = deliveryPrice;
        this.productId = productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.orderStatus = orderStatus;
        Address = address;
        CoupenId = coupenId;
        CuttedPrice = cuttedPrice;
        OrdeedrDate = ordeedrDate;
        PackedDate = packedDate;
        Shippeddate = shippeddate;
        DeliveredDate = deliveredDate;
        CancelledDate = cancelledDate;
        DiscountedPrice = discountedPrice;
        Freecoupens = freecoupens;
        Fullname = fullname;
        OrderId = orderId;
        PaymentMethod = paymentMethod;
        Pincode = pincode;
        ProductPrice = productPrice;
        ProductQty = productQty;
        this.userId = userId;
        this.cancellationRequested = cancellationRequested;



    }



    public Boolean getCancellationRequested() {
        return cancellationRequested;
    }

    public void setCancellationRequested(Boolean cancellationRequested) {
        this.cancellationRequested = cancellationRequested;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCoupenId() {
        return CoupenId;
    }

    public void setCoupenId(String coupenId) {
        CoupenId = coupenId;
    }

    public String getCuttedPrice() {
        return CuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        CuttedPrice = cuttedPrice;
    }

    public Date getOrdeedrDate() {
        return OrdeedrDate;
    }

    public void setOrdeedrDate(Date ordeedrDate) {
        OrdeedrDate = ordeedrDate;
    }

    public Date getPackedDate() {
        return PackedDate;
    }

    public void setPackedDate(Date packedDate) {
        PackedDate = packedDate;
    }

    public Date getShippeddate() {
        return Shippeddate;
    }

    public void setShippeddate(Date shippeddate) {
        Shippeddate = shippeddate;
    }

    public Date getDeliveredDate() {
        return DeliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        DeliveredDate = deliveredDate;
    }

    public Date getCancelledDate() {
        return CancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        CancelledDate = cancelledDate;
    }

    public String getDiscountedPrice() {
        return DiscountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        DiscountedPrice = discountedPrice;
    }

    public Long getFreecoupens() {
        return Freecoupens;
    }

    public void setFreecoupens(Long freecoupens) {
        Freecoupens = freecoupens;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public Long getProductQty() {
        return ProductQty;
    }

    public void setProductQty(Long productQty) {
        ProductQty = productQty;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

