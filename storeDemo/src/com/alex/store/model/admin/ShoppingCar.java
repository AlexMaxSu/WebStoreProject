package com.alex.store.model.admin;


import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCar {
    private int sid;
    private User user;
    private Map<String,ShoppingItem> shoppingItem = new LinkedHashMap<>();
    private double total;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, ShoppingItem> getShoppingItem() {
        return shoppingItem;
    }

    public void setShoppingItem(Map<String, ShoppingItem> shoppingItem) {
        this.shoppingItem = shoppingItem;
    }

    public double getTotal() {
        return total;
    }

    // 清空购物车
    public void clearShoppingCar() {
        // 清空购物项
        shoppingItem.clear();
        // 总金额置零
        total = 0.0;
    }

    // 从购物车移除购物项
    public void removeShoppingCar(String key) {
        // 获取要移除的购物项
        ShoppingItem item = shoppingItem.remove(key);
        // 用总金额减去要移除的购物项的金额
        total -= item.getSubtotal();
    }

    // 向购物车中添加商品
    public void addShoppingCar(ShoppingItem item) {
        ShoppingItem items = shoppingItem.get(item.getProduct().getPid());
        // 如果不存在此购物项中，直接加入购物车
        if (items == null) {
            shoppingItem.put(item.getProduct().getPid(), item);
            total += item.getSubtotal();
            // 如果存在此购物项，将原有购物项的数量加上此购物项的数量
        } else {
            items.setSubcount(items.getSubcount() + item.getSubcount());
            total += item.getSubtotal();
        }
    }

   /* public void setTotal(double total) {
        this.total = total;
    }*/


}
