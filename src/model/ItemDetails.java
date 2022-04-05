package model;

public class ItemDetails {
    private String itemCode;
    private double unitPrice;
    private double discount;
    private int qtyForSell;

    public ItemDetails(String s, String s1) {
    }

    public ItemDetails(String itemCode, double unitPrice, double discount, int qtyForSell) {
        this.setItemCode(itemCode);
        this.setUnitPrice(unitPrice);
        this.setDiscount(discount);
        this.setQtyForSell(qtyForSell);
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyForSell() {
        return qtyForSell;
    }

    public void setQtyForSell(int qtyForSell) {
        this.qtyForSell = qtyForSell;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "ItemDetails{" +
                "itemCode='" + itemCode + '\'' +
                ", unitPrice=" + unitPrice +
                ", discount=" + discount +
                ", qtyForSell=" + qtyForSell +
                '}';
    }
}
