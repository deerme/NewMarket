package com.market.model.changed;

import com.google.common.base.MoreObjects;

/**
 * Created by pizmak on 2016-07-05.
 */
public class OrderTemp {
    private int id;
    private String type;
    private int quantity;
    public final static String NAME ="id";

    public OrderTemp() {
    }

    public OrderTemp(int id, String type, int quantity) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("quantity", quantity)
                .toString();
    }
}
