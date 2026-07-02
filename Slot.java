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

    public void setQuantity(int newQuantity){
        if(newQuantity + this.quantity > this.maxQuantity || newQuantity > this.maxQuantity){
            this.quantity = this.maxQuantity;
        }
        else{
            this.quantity = newQuantity;
        }
    }

}