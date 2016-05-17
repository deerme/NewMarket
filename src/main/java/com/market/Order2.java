package com.market;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Optional;

/**
 * Created by PBanasiak on 3/21/2016.
 */
final public class Order2 {
    final private Optional<Integer> id;
    final private String type;
    final private int quantity;

    public Order2(Optional<Integer> id, String type, int quantity) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
    }

    public Optional<Integer> getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order2 order2 = (Order2) o;
        return getQuantity() == order2.getQuantity() &&
                Objects.equal(getId(), order2.getId()) &&
                Objects.equal(getType(), order2.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getType(), getQuantity());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", Optional.of(id))
                .add("type", type)
                .add("quantity", quantity)
                .toString();
    }
}
