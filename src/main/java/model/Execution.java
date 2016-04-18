package model;


/**
 * Created by PBanasiak on 3/21/2016.
 */
public class Execution {
    private int id;
    private int idBuyer;
    private int idSeller;
    private int quantityOfExecution;

    public Execution(){
        //  /*  It is necessary in ExecutionDAO implementation  */
    }

    public Execution( int idBuyer, int idSeller){
        this.idBuyer = idBuyer;
        this.idSeller = idSeller;
    }

    public Execution( int idBuyer, int idSeller,int id,int quantityOfExecution){
        this(idBuyer,idSeller);
        this.id = id;
        this.quantityOfExecution = quantityOfExecution;
    }

    public int getQuantityOfExecution() {
        return quantityOfExecution;
    }

    public void setQuantityOfExecution(int quantityOfExecution) {
        this.quantityOfExecution = quantityOfExecution;
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

    public int getId() {
        return id;
    }

    public int getIdBuyer() {
        return idBuyer;
    }

    public int getIdSeller() {
        return idSeller;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("idBuyer", idBuyer)
                .add("idSeller", idSeller)
                .add("quantity", quantityOfExecution)
                .toString();
    }
}
