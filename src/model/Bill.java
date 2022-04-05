package model;

public class Bill {
    private String itemCode;
    private String orderId;
    private int sellQty;
    private double price;
    private double discount;

    public Bill() {
    }

    public Bill(String itemCode, String orderId, int sellQty, double price, double discount) {
        this.setItemCode(itemCode);
        this.setOrderId(orderId);
        this.setSellQty(sellQty);
        this.setPrice(price);
        this.setDiscount(discount);
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getSellQty() {
        return sellQty;
    }

    public void setSellQty(int sellQty) {
        this.sellQty = sellQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


}
