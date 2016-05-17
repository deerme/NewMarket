package model;


/**
 * Created by PBanasiak on 3/21/2016.
 */
public class Execution {
    private int id;
    private int idBuyer;
    private int idSeller;
    private final int quantityOfExecution;
    private int quantityOfBuyer;
    private int quantityOfSeller;

    private Execution(ExecutionBuilder builder){
        id = builder.id;
        idBuyer = builder.idBuyer;
        idSeller = builder.idSeller;
        quantityOfExecution = builder.quantityOfExecution;
        quantityOfBuyer = builder.quantityOfBuyer;
        quantityOfSeller = builder.quantityOfSeller;
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

    public int getQuantityOfExecution() {
        return quantityOfExecution;
    }

    public int getQuantityOfBuyer() {
        return quantityOfBuyer;
    }

    public int getQuantityOfSeller() {
        return quantityOfSeller;
    }

        public static class ExecutionBuilder{
            private int id;
            private int idBuyer;
            private int idSeller;
            private final int quantityOfExecution;
            private int quantityOfBuyer;
            private int quantityOfSeller;

            public ExecutionBuilder(int quantityOfExecution) {
                this.quantityOfExecution = quantityOfExecution;
            }

            public ExecutionBuilder id(int idOfExecution){
                this.id = idOfExecution;
                return this;
            }

            public ExecutionBuilder idBuyer(int idBuyer){
                this.idBuyer = idBuyer;
                return this;
            }

            public ExecutionBuilder idSeller(int idSeller){
                this.idSeller = idSeller;
                return  this;
            }

            public ExecutionBuilder quantityOfSeller(int quantityOfSeller){
                this.quantityOfSeller = quantityOfSeller;
                return  this;
            }

            public ExecutionBuilder quantityOfBuyer(int quantityOfBuyer){
                this.quantityOfBuyer = quantityOfBuyer;
                return  this;
            }

            public Execution build(){
                return new Execution(this);
            }

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
