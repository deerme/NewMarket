package com.market.model.changed;

import com.google.common.base.MoreObjects;

/**
 * Created by pizmak on 2016-07-06.
 */
public class ExecutionTemp {
    private int id;
    private int idBuyer;
    private int idSeller;
    private int quantity;

    public ExecutionTemp() {
    }

    public ExecutionTemp(int id, int idBuyer, int idSeller, int quantity) {
        this.id = id;
        this.idBuyer = idBuyer;
        this.idSeller = idSeller;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getIdBuyer() {
        return idBuyer;
    }

    public int getIdSeller() {
        return idSeller;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdBuyer(int idBuyer) {
        this.idBuyer = idBuyer;
    }

    public void setIdSeller(int idSeller) {
        this.idSeller = idSeller;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("idBuyer", idBuyer)
                .add("idSeller", idSeller)
                .add("quantity", quantity)
                .toString();
    }
}
