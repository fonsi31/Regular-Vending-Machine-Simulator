import java.util.HashMap;
import java.util.ArrayList;

/**
 * Stores and manages the transaction history of the vending machine.
 * <p>
 * This class records the total sales, quantity sold for each item,
 * and the starting inventory after the latest restocking. It also
 * provides methods for displaying sales and inventory summaries.
 *
 * @author Alfonso S. Cauilan, Daviane Nate M. Abad
 * @version 1.0
 */
public class TransactionHistory {

    /** Stores the quantity sold for each item. */
    private HashMap<Item, Integer> quantitySold;

    /** Stores the total sales since the last restock. */
    private double sales = 0;

    /** Stores the inventory count immediately after the latest restock. */
    private HashMap<Item, Integer> startingInventory;

    /**
     * Constructs a TransactionHistory object.
     *
     * @param quantitySold a map containing the quantity sold for each item
     * @param startingInventory a map containing the starting inventory of each item
     */
    public TransactionHistory(HashMap<Item, Integer> quantitySold,
                              HashMap<Item, Integer> startingInventory){
        this.quantitySold = quantitySold;
        this.startingInventory = startingInventory;
    }

    /**
     * Returns the total sales recorded since the last restock.
     *
     * @return the total sales amount
     */
    public double getSales(){
        return this.sales;
    }

    /**
     * Adds the specified amount to the total sales.
     * Negative values are ignored.
     *
     * @param newSales the amount to be added to total sales
     */
    public void updateSales(double newSales){
        if(newSales < 0){
            newSales = 0;
        }

        this.sales += newSales;
    }

    /**
     * Returns the quantity sold for a specific item.
     *
     * @param item the item to retrieve
     * @return the quantity sold
     */
    public int getQuantitySold(Item item){
        return this.quantitySold.get(item);
    }

    /**
     * Updates the quantity sold for a specific item.
     *
     * @param item the item whose sales quantity will be updated
     * @param additional the quantity to add to the current sales count
     */
    public void updateQuantitySold(Item item, int additional){
        this.quantitySold.put(item,
                this.getQuantitySold(item) + additional);
    }

    /**
     * Resets the transaction summary after a restocking operation.
     * <p>
     * This method clears all sales records and updates the starting
     * inventory based on the current stock levels.
     *
     * @param slots the list of vending machine slots
     */
    public void resetSummary(ArrayList<Slot> slots){
        for(Item item : this.quantitySold.keySet()){
            this.quantitySold.put(item, 0);
        }

        this.sales = 0;

        int i = 0;

        for(Item item : this.startingInventory.keySet()){
            this.startingInventory.put(item,
                    slots.get(i).getQuantity());
            i++;
        }
    }

    /**
     * Displays the sales summary since the latest restocking.
     * <p>
     * The summary includes the gross sales and the quantity sold
     * for every item.
     */
    public void displaySalesSummary(){
        System.out.println("======= Sales Summary (Starting from the latest restock) =======");
        System.out.println("Gross Sales: ₱" + this.sales);
        System.out.println();

        System.out.printf("%-20s %-15s%n",
                "Product", "Sales Volume");

        System.out.println("-".repeat(36));

        for(Item item : this.quantitySold.keySet()){
            System.out.printf("%-20s %-15d%n",
                    item.getName(),
                    this.quantitySold.get(item));
        }
    }

    /**
     * Displays the inventory summary.
     * <p>
     * The summary includes both the starting inventory after the latest
     * restock and the current inventory levels.
     *
     * @param slots the list of vending machine slots
     */
    public void displayInventory(ArrayList<Slot> slots){
        System.out.println("======= Inventory Summary (Starting from the latest restock) =======");

        System.out.println("Starting Inventory");
        System.out.println("----------------------------------------");

        System.out.printf("%-20s %-15s%n",
                "Product", "Stocks");

        for(Item item : this.startingInventory.keySet()){
            System.out.printf("%-20s %-15d%n",
                    item.getName(),
                    this.startingInventory.get(item));
        }

        System.out.println();

        System.out.println("Current Inventory");
        System.out.println("----------------------------------------");

        System.out.printf("%-20s %-15s%n",
                "Product", "Stocks");

        for(Slot slot : slots){
            System.out.printf("%-20s %-15d%n",
                    slot.getItem().getName(),
                    slot.getQuantity());
        }
    }
}