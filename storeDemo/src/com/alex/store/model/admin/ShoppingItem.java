package com.alex.store.model.admin;

public class ShoppingItem {
    private int itemid;
    private ShoppingCar shoppingCar;
    private Product product;
    private Integer subcount;
    private double subtotal;

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public ShoppingCar getShoppingCar() {
        return shoppingCar;
    }

    public void setShoppingCar(ShoppingCar shoppingCar) {
        this.shoppingCar = shoppingCar;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getSubcount() {
        return subcount;
    }

    public void setSubcount(Integer subcount) {
        this.subcount = subcount;
    }

    public double getSubtotal() {
        return product.getEstoreprice()*subcount;
    }

    //计算金额只能自己来做
   /* public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }*/
}
