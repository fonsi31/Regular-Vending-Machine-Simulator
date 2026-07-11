public class Slot {
    private Item item;
    private int quantity;
    private final int maxQuantity;

    public Slot(Item item, int quantity, int maxQuantity){
        this.item  = item;
        this.quantity = quantity;
        if (maxQuantity < this.quantity){
            this.maxQuantity = this.quantity;
        }
        else{
            this.maxQuantity = maxQuantity;
        }
    }

    public Item getItem(){
        return this.item;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public int getMaxQuantity(){
        return this.maxQuantity;    
    }

    public void setQuantity(int newQuantity){
        if(newQuantity > this.maxQuantity){
            this.quantity = this.maxQuantity;
        }
        else{
            this.quantity = newQuantity;
        }
    }

}