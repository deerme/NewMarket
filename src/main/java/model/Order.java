package model;


import com.google.common.base.MoreObjects;

/**
 * Created by PBanasiak on 3/21/2016.
 */
public class Order {
    private int id;
    private String type;
    private int quantity;

    public Order(){
         //  /*  It is necessary in OrderDAO implementation  */
    }

    public Order(int id,String type,int quantity){
        this.id = id;
        this.type = type;
        this.quantity = quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
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
