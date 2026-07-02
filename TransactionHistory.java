import java.util.HashMap;
import java.util.ArrayList;

public class TransactionHistory {
    private HashMap<Item, Integer> quantitySold;
    private double sales = 0;
    private HashMap<Item, Integer> startingInventory;

    public TransactionHistory(HashMap<Item, Integer> quantitySold, HashMap<Item, Integer> startingInventory){
        this.quantitySold = quantitySold;
        this.startingInventory = startingInventory;
    }

    public double getSales(){
        return this.sales;
    }

    public void updateSales(double newSales){
        if(newSales < 0){
            newSales = 0;
        }
        this.sales += newSales;
    }

    public int getQuantitySold(Item item){
        return this.quantitySold.get(item);
    }

    public void updateQuantitySold(Item item){
        this.quantitySold.put(item, this.getQuantitySold(item) + 1);
    }

    public void resetSalesSumarry(ArrayList<Slot> slots){ //happens every restocking
        for(Item item: this.quantitySold.keySet()){
            this.quantitySold.put(item, 0);
        }
        this.sales = 0;
        int i = 0;
        for(Item item: this.startingInventory.keySet()){
            this.startingInventory.put(item, slots.get(i).getQuantity());
            i++;
        }
    }

    public void displaySummary(){
        
    }
}