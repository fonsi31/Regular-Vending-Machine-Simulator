/**
 * Represents an item that can be stored and dispensed by the vending machine.
 * <p>
 * Each item has a name, selling price, and calorie count.
 *
 * @author Alfonso S. Cauilan, Daviane Nate M. Abad
 * @version 1.0
 */
public class Item {
    /** The name of the item. */
    private final String name;
    /** The selling price of the item. */
    private double price;
    /** The calorie count of the item. */
    private final int calories;

    /**
     * Constructs an Item with the specified name, price, and calorie count.
     *
     * @param name the name of the item
     * @param price the selling price of the item
     * @param calories the calorie count of the item
     */
    public Item(String name, double price, int calories){
        this.name = name;
        this.price = price;
        this.calories = calories;
    }

    /**
     * Returns the name of the item.
     *
     * @return the name of the item
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the selling price of the item.
     *
     * @return the selling price of the item
     */
    public double getPrice(){
        return this.price;
    }

    /**
     * Returns the calorie count of the item.
     *
     * @return the calorie count of the item
     */
    public int getCal(){
        return this.calories;
    }

    /**
     * Sets the selling price of the item.
     * <p>
     * The new price must be non-negative; otherwise, the price remains unchanged.
     *
     * @param newPrice the new selling price to set
     */
    public void setPrice(double newPrice){
        if(newPrice >= 0){
            this.price = newPrice;
        }
    }
}