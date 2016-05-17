package com.market;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.Optional;

/**
 * Created by PBanasiak on 3/21/2016.
 */
public class Execution2 {
    final private Optional<Integer> id;
    final private int idBuyer;
    final private int idSeller;
    final private int quantity;

    public Execution2(Optional<Integer> id, int idBuyer, int idSeller, int quantity) {
        this.id = id;
        this.idBuyer = idBuyer;
        this.idSeller = idSeller;
        this.quantity = quantity;
    }

    public Optional<Integer> getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Execution2 that = (Execution2) o;
        return getIdBuyer() == that.getIdBuyer() &&
                getIdSeller() == that.getIdSeller() &&
                getQuantity() == that.getQuantity() &&
                Objects.equal(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getIdBuyer(), getIdSeller(), getQuantity());
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
