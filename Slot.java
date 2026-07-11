/**
 * Represents a slot in the vending machine that holds a specific item and its quantity.
 * <p>
 * Each slot has a maximum quantity limit, and the current quantity of the item can be adjusted
 * within this limit. The slot also provides methods to retrieve the item, its quantity, and
 * the maximum quantity allowed.
 *
 * @author Alfonso S. Cauilan, Daviane Nate M. Abad
 * @version 1.0
 */
public class Slot {
    /** The item stored in this slot. */
    private Item item;
    /** The current quantity of the item in this slot. */
    private int quantity;
    /** The maximum quantity of the item that can be stored in this slot. */
    private final int maxQuantity;

    /**
     * Constructs a new Slot object.
     *
     * @param item the item this slot will hold
     * @param quantity the initial quantity of the item in this slot
     * @param maxQuantity the maximum quantity of the item that can be stored in this slot
     */
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

    /**
     * Returns the item stored in this slot.
     *
     * @return the item stored in this slot
     */
    public Item getItem(){
        return this.item;
    }

    /**
     * Returns the current quantity of the item in this slot.
     *
     * @return the current quantity of the item
     */
    public int getQuantity(){
        return this.quantity;
    }

    /**
     * Returns the maximum quantity of the item that can be stored in this slot.
     *
     * @return the maximum quantity of the item
     */
    public int getMaxQuantity(){
        return this.maxQuantity;    
    }

    /**
     * Sets the current quantity of the item in this slot.
     * <p>
     * If the new quantity exceeds the maximum quantity, it is set to the maximum.
     *
     * @param newQuantity the new quantity to set
     */
    public void setQuantity(int newQuantity){
        if(newQuantity > this.maxQuantity){
            this.quantity = this.maxQuantity;
        }
        else{
            this.quantity = newQuantity;
        }
    }

}