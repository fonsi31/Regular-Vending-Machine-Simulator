import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Simulates a regular vending machine.
 * <p>
 * This class manages item slots, customer purchases,
 * maintenance operations, cash reserves, and transaction history.
 *
 * @author Alfonso S. Cauilan, Daviane Nate M. Abad
 * @version 1.0
 */
public class VendingMachine {
    private ArrayList<Slot> slots;
    private CashInventory cashInventory;
    private TransactionHistory transactionHistory;

    /**
     * Constructs a vending machine with a specified slot capacity.
     * <p>
     * The machine is initialized with predefined items,
     * an empty cash inventory, and a transaction history.
     *
     * @param maxQuantity the maximum number of items each slot can hold
     */
    public VendingMachine(int maxQuantity){
        this.slots = new ArrayList< >();
        
        this.slots.add(new Slot(new Item("Flour Tortilla", 30, 210), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Wheat Tortilla", 35, 190), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Grilled Chicken", 70, 180), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Seasoned Beef", 80, 250), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Mexican Rice", 25, 160), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Black Beans", 20, 120), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Cheddar Cheese", 25, 110), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Romaine Lettuce", 15, 10), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Red Onion", 10, 8), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Salsa", 20, 20), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Guacamole", 40, 100), 10, maxQuantity));
        this.slots.add(new Slot(new Item("Garlic Ranch", 25, 120), 10, maxQuantity));
        
        HashMap<Item, Integer> quantitySold = new HashMap<>();
        HashMap<Item, Integer> startingInventory = new HashMap<>();
        for (Slot slot: slots){
            quantitySold.put(slot.getItem(), 0);
            startingInventory.put(slot.getItem(), slot.getQuantity());
        }
        this.cashInventory = new CashInventory();
        this.transactionHistory = new TransactionHistory(quantitySold, startingInventory);
    }

    /**
     * Displays all available items in the vending machine.
     * <p>
     * Each item's ID, name, price, calorie count,
     * and remaining stock are shown.
     */
    public void displayItems(){
        int i = 1;
        System.out.printf("%-3s %-30s %-15s %-15s %-20s%n", "ID", "Product", "Price", "kcal", "Available Stocks");
        for(Slot slot: this.slots){
            System.out.printf("%-3d %-30s %s%-14.2f %-15d %-20d%n", i,
             slot.getItem().getName(), "\u20B1", slot.getItem().getPrice(), slot.getItem().getCal(), slot.getQuantity());
            i++;
        }
    }

    /**
     * Allows a customer to purchase one or more items.
     * <p>
     * The customer selects items, inserts payment,
     * receives change when applicable, and the
     * transaction history is updated.
     *
     * @param scanner the Scanner object used to receive user input
     */
    public void purchaseItem(Scanner scanner){
        int last_opt = this.slots.size() + 1;

        String input = "";
        int choice = 0;

        HashMap<Item, Integer> cart = new HashMap<>();
        double outsamt = 0; //outstanding amount
        int[] lessStock = new int[last_opt-1]; //To be added back to the quantity of items if ever the user decided to withdraw purchase

        do{ //The user chooses items to purchase
            Main.clear_terminal();
            this.displayItems();
            System.out.println(last_opt + ". Proceed to Payment");
            System.out.println();
            System.out.print("Enter choice: ");
            input = scanner.nextLine();

            try{
               choice = Integer.parseInt(input); 
            }
            catch(NumberFormatException e){
                choice = 0;
            }

            if (choice < 1 || choice > last_opt){
                System.out.println("Invalid Input");
                Main.pause(scanner);
            }
            else if(choice < last_opt){
                if(this.slots.get(choice-1).getQuantity() == 0){
                    System.out.println("No stocks left for this item!");
                }
                else{
                    Item item = this.slots.get(choice-1).getItem();
                    cart.put(item, cart.getOrDefault(item, 0) + 1);
                    System.out.println("Item added to cart!");
                    this.slots.get(choice-1).setQuantity(this.slots.get(choice-1).getQuantity()-1);
                    lessStock[choice-1]++;
                    outsamt += item.getPrice();
                    Main.pause(scanner);
                }
            }
        }while(choice != last_opt);

        int j = 1;
        double payment = 0;
        double change = 0;
        double total_amt = outsamt; //snapshot
        int[] denominations = this.cashInventory.getDenominations();
        boolean has_withdrawn = false;
        while (outsamt > 0){
            j = 1;
            Main.clear_terminal();
            System.out.println("Your Cart:");
            for(Item item: cart.keySet()){
                System.out.printf("%-20s x%-3d%n", item.getName(), cart.get(item));
            }
            System.out.println();
            System.out.println("Outstanding Amount: \u20B1" + outsamt); 
            System.out.println();
            for (int denomination: denominations){
                System.out.println(j + ". " + "\u20B1" + denomination);
                j++;
            }
            System.out.println(j + " .Withdraw Purchase");
            System.out.print("Enter choice for the denomination you want to insert: ");
            input = scanner.nextLine();

            try{
               choice = Integer.parseInt(input); 
            }
            catch(NumberFormatException e){
                choice = 0;
            }

            if(choice < 1 || choice > j){
                System.out.println("Invalid Input");
                Main.pause(scanner);
            }
            else if(choice == j){ //user withdraws from purchase
                for(int k = 0; k < this.slots.size(); k++){
                    this.slots.get(k).setQuantity(this.slots.get(k).getQuantity() + lessStock[k]);
                }
                change = payment;
                has_withdrawn = true;
                break;
            }
            else{
                this.cashInventory.setReserves(this.cashInventory.getReserves()[choice-1]+1, choice-1);
                outsamt -= denominations[choice-1];
                payment += denominations[choice-1];
            }
        }

        if (has_withdrawn){
            this.cashInventory.dispenseChange(change);
            System.out.println("Purchase successfuly withdrawn");
        }
        else{ //user didn't cancel purchase
            change = -1 * outsamt;
            if(this.cashInventory.enoughChange(change)){
                this.transactionHistory.updateSales(total_amt);
                System.out.println();
                System.out.println("Dispensing your items...Please wait");
                for(Item item: cart.keySet()){
                    this.transactionHistory.updateQuantitySold(item, cart.get(item));
                    try{
                        Thread.sleep(1500);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    for(int i = 0; i < cart.get(item); i++){
                        System.out.println(item.getName());
                    }
                }
                this.cashInventory.dispenseChange(change);
                System.out.println();
                System.out.println("Thank you for buying!");
            }
            else{ //No reserves to supply change
                change = payment;
                for(int k = 0; k < this.slots.size(); k++){ //adds back the subtracted slots' quantity if the purchase doesn't push through
                    this.slots.get(k).setQuantity(this.slots.get(k).getQuantity() + lessStock[k]);
                }
                System.out.println("Purchase cancelled due to insufficient cash reserves to produce your change");
                this.cashInventory.dispenseChange(change);
            }
        }
        Main.pause(scanner);
    }

    /**
     * Restocks items in the vending machine.
     * <p>
     * The operator selects an item and specifies the quantity
     * to be added. The transaction summary is reset after
     * restocking is completed.
     *
     * @param scanner the Scanner object used to receive user input
     */
    public void restockItems(Scanner scanner){
        int last_opt = this.slots.size() + 1;
        String input = "";
        int choice = 0;
        do{
            Main.clear_terminal();
            this.displayItems();
            System.out.println(last_opt + ". Finish");
            System.out.print("Choice: ");
            input = scanner.nextLine();

            try{
               choice = Integer.parseInt(input); 
            }
            catch(NumberFormatException e){
                choice = 0;
            }

            if(choice < 1 || choice > last_opt){
                System.out.println("Invalid Input!");
                Main.pause(scanner);
            }
            else if(choice < last_opt){
                int additional = -1;
                String input1 = "";
                String itemName = this.slots.get(choice-1).getItem().getName();
                int curr_quantity = this.slots.get(choice-1).getQuantity();
                do{
                    Main.clear_terminal();
                    System.out.println(itemName + " " + curr_quantity);
                    System.out.println();
                    System.out.print("How many stocks you want to add for this item?: ");
                    input1 = scanner.nextLine();
                    try{
                        additional = Integer.parseInt(input1);
                    }
                    catch (NumberFormatException e){
                        additional = -1;
                    }

                    if(additional < 0){
                        System.out.println("Invalid Input!");
                        Main.pause(scanner);
                    }
                    else{
                        this.slots.get(choice-1).setQuantity(additional + curr_quantity);

                        System.out.println("Successfuly restocked this item!");
                    }
                }while(additional < 0);
            }
        }while(choice != last_opt);
        this.transactionHistory.resetSummary(this.slots);
        Main.pause(scanner);
    }

    /**
     * Updates the prices of items in the vending machine.
     * <p>
     * The operator selects an item and specifies a new price.
     * non-negative prices are enforced
     * @param scanner the Scanner object used to receive user input
     */
    public void setPrices(Scanner scanner){
        int last_opt = this.slots.size() + 1;
        String input = "";
        int choice = 0;
        do{
            Main.clear_terminal();
            this.displayItems();
            System.out.println(last_opt + ". Finish");
            System.out.print("Choice: ");
            input = scanner.nextLine();

            try{
               choice = Integer.parseInt(input); 
            }
            catch(NumberFormatException e){
                choice = 0;
            }

            if(choice < 1 || choice > last_opt){
                System.out.println("Invalid Input!");
                Main.pause(scanner);
            }
            else if(choice < last_opt){
                double newPrice = -1;
                String input1 = "";
                String itemName = this.slots.get(choice-1).getItem().getName();
                double curr_price = this.slots.get(choice-1).getItem().getPrice();
                do{
                    Main.clear_terminal();
                    System.out.println(itemName + " \u20B1" + curr_price);
                    System.out.println();
                    System.out.print("Enter new price for this item: ");
                    input1 = scanner.nextLine();
                    try{
                        newPrice = Double.parseDouble(input1);
                    }
                    catch (NumberFormatException e){
                        newPrice = -1;
                    }

                    if(newPrice < 0){
                        System.out.println("Invalid Input!");
                        Main.pause(scanner);
                    }
                    else{
                        this.slots.get(choice-1).getItem().setPrice(newPrice);
                        System.out.println("Price successfully modified!");
                    }
                }while(newPrice < 0);
            }
        }while(choice != last_opt);
        Main.pause(scanner);
    }
    /**
     * Replenishes the cash reserves of the vending machine.
     * <p>
     * The operator selects a denomination and specifies the quantity
     * @param scanner the Scanner object used to receive user input
     */
    public void replenishCash(Scanner scanner){
        int[] denominations = this.cashInventory.getDenominations();
        int last_opt = denominations.length + 1;
        String input = "";
        int choice = 0;
        do{
            Main.clear_terminal();
            this.cashInventory.displayDenominations();
            System.out.println();
            System.out.println(last_opt + ". Finish");
            System.out.print("Choice: ");
            input = scanner.nextLine();

            try{
               choice = Integer.parseInt(input); 
            }
            catch(NumberFormatException e){
                choice = 0;
            }

            if(choice < 1 || choice > last_opt){
                System.out.println("Invalid Input!");
                Main.pause(scanner);
            }
            else if(choice < last_opt){
                int additional = -1;
                String input1 = "";
                double denomination = denominations[choice-1];
                int curr_qty = this.cashInventory.getReserves()[choice-1];
                do{
                    Main.clear_terminal();
                    System.out.println("\u20B1" + denomination + " " + curr_qty);
                    System.out.println();
                    System.out.print("How many \u20B1" + denomination + "s bill you want to add?: ");
                    input1 = scanner.nextLine();
                    try{
                        additional = Integer.parseInt(input1);
                    }
                    catch (NumberFormatException e){
                        additional = -1;
                    }

                    if(additional < 0){
                        System.out.println("Invalid Input!");
                        Main.pause(scanner);
                    }
                    else{
                        int curr_quantity = this.cashInventory.getReserves()[choice-1];
                        this.cashInventory.setReserves(curr_quantity+additional, choice-1);
                        System.out.println("Successfuly replenished this denomination!");
                        Main.pause(scanner);
                    }
                }while(additional < 0);
            }
        }while(choice != last_opt);
        Main.pause(scanner);
    }

    /**
     * Displays the transaction summary menu.
     * <p>
     * The operator can view sales summaries and inventory levels since the last restocking.
     * @param scanner the Scanner object used to receive user input
     */
    public void displayTransactionSummary(Scanner scanner){
        String input = "";
        int choice = 0;

        do{
            Main.clear_terminal();
            System.out.println("1. View Sales Summary");
            System.out.println("2. View Inventory");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            input = scanner.nextLine();

            try{
                choice = Integer.parseInt(input);
            }
            catch(NumberFormatException e){
                choice = 0;
            }

            if(choice < 1 || choice > 3){
                System.out.println("invalid Input!");
            }
            else if(choice == 1){
                Main.clear_terminal();
                this.transactionHistory.displaySalesSummary(this.slots);
                Main.pause(scanner);
            }
            else if(choice == 2){
                Main.clear_terminal();
                this.transactionHistory.displayInventory(this.slots);
                Main.pause(scanner);
            }
        } while(choice != 3);

    }
    
    /**
     * Collects all money currently stored in the vending machine.
     * <p>
     * After collection, the machine's cash reserves are reset to zero,
     * and the total amount collected is displayed.
     */
    public void collectCash(){
        double extrct_amt = this.cashInventory.extractCash();
        System.out.println("Cash Collected Successfuly!");
        System.out.println("Garnered Amount: \u20B1" + extrct_amt);
    }
}