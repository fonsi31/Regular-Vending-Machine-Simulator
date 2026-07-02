import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.InputMismatchException;

public class VendingMachine {
    private ArrayList<Slot> slots;
    private CashInventory cashInventory;
    private TransactionHistory transactionHistory;

    public VendingMachine(int maxQuantity){
        this.slots = new ArrayList<Slot>();
        
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

    public void displayItems(){
        int i = 1;
        for(Slot slot: this.slots){
            System.out.println(i + " " + slot.getItem().getName());
            System.out.println("Price: " + slot.getItem().getPrice());
            System.out.println("Calories: " + slot.getItem().getCal());
            System.out.println("Stocks: " + slot.getQuantity());
            i++;
        }
    }

    public void purchaseItem(Scanner scanner){
        int last_opt = this.slots.size() + 1;

        String input = "";
        int choice = 0;

        ArrayList<Item> cart = new ArrayList<Item>();
        double outsamt = 0; //outstanding amount
        int[] lessStock = new int[last_opt-1]; //To be added back to the quantity of items if ever the user decided to withdraw purchase

        do{
            this.displayItems();
            System.out.println(last_opt + ". Proceed to Payment");
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
            }
            else{
                if(this.slots.get(choice-1).getQuantity() == 0){
                    System.out.println("No stocks left for this item!");
                }
                else{
                    Item item = this.slots.get(choice-1).getItem();
                    cart.add(item);
                    System.out.println("Item added to cart!");
                    this.slots.get(choice-1).setQuantity(this.slots.get(choice-1).getQuantity()-1);
                    lessStock[choice-1]++;
                    outsamt += item.getPrice();
                }
            }
        }while(choice != last_opt);

        int j = 1;
        double payment = 0;
        double change = 0;
        double total_amt = outsamt; //snapshot
        System.out.println("Outstanding Amount: ₱" + outsamt);
        int[] denominations = this.cashInventory.getDenominations();
        boolean has_withdrawn = false;
        while (outsamt > 0){
            for (int denomination: denominations){
                System.out.println(j + ". " + "₱" + denomination);
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

            if(choice < 1 || choice > j + 1){
                System.out.println("Invalid Input");
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
            if(this.cashInventory.enoughChange(change)){
                System.out.println("Purchase successfuly withdrawn");
            }
        }
        else{
            change = -1 * outsamt;
            if(this.cashInventory.enoughChange(change)){
                this.transactionHistory.updateSales(total_amt);
                for(Item item: cart){
                    this.transactionHistory.updateQuantitySold(item);
                }
                System.out.println("Thank you for buying!");
            }
            else{
                for(int k = 0; k < this.slots.size(); k++){ //adds back the subtracted slots' quantity if the purchase doesn't push through
                    this.slots.get(k).setQuantity(this.slots.get(k).getQuantity() + lessStock[k]);
                }
                System.out.println("Purchase cancelled due to insufficient reserves to supply your change");
            }
        }
        System.out.println("Change: ₱" + change);
    }

    public void restockItems(Scanner scanner){
        int last_opt = this.slots.size() + 1;
        String input = "";
        int choice = 0;
        do{
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
            }
            else{
                int additional = -1;
                String input1 = "";
                do{
                    System.out.print("How many stocks you want to add for this item?: ");
                    input1 = scanner.nextLine();
                    try{
                        additional = Integer.parseInt(input);
                    }
                    catch (NumberFormatException e){
                        additional = -1;
                    }

                    if(additional < 0){
                        System.out.println("Invalid Input!");
                    }
                    else{
                        int curr_quantity = this.slots.get(choice-1).getQuantity();
                        this.slots.get(choice-1).setQuantity(curr_quantity + additional);

                        System.out.println("Successfuly restocked this item!");
                    }
                }while(additional < 0);
            }
        }while(choice != last_opt);
        this.transactionHistory.resetSalesSumarry(this.slots);
    }

    public void setPrices(Scanner scanner){
        int last_opt = this.slots.size() + 1;
        String input = "";
        int choice = 0;
        do{
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
            }
            else{
                double newPrice = -1;
                String input1 = "";
                do{
                    System.out.println("Enter new price for this item: ");
                    input1 = scanner.nextLine();
                    try{
                        newPrice = Double.parseDouble(input1);
                    }
                    catch (NumberFormatException e){
                        newPrice = -1;
                    }

                    if(newPrice < 0){
                        System.out.println("Invalid Input!");
                    }
                    else{
                        this.slots.get(choice-1).getItem().setPrice(newPrice);
                        System.out.println("Price successfully modified!");
                    }
                }while(newPrice < 0);
            }
        }while(choice != last_opt);
    }

    public void replenishCash(Scanner scanner){
        int[] denominations = this.cashInventory.getDenominations();
        int last_opt = denominations.length + 1;
        String input = "";
        int choice = 0;
        do{
            this.cashInventory.displayDenominations();
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
            }
            else{
                int additional = -1;
                String input1 = "";
                do{
                    System.out.print("How many ₱" + denominations[choice-1] + "s bills you want to add?: ");
                    input1 = scanner.nextLine();
                    try{
                        additional = Integer.parseInt(input);
                    }
                    catch (NumberFormatException e){
                        additional = -1;
                    }

                    if(additional < 0){
                        System.out.println("Invalid Input!");
                    }
                    else{
                        int curr_quantity = this.cashInventory.getReserves()[choice-1];
                        this.cashInventory.setReserves(curr_quantity+additional, choice-1);
                        System.out.println("Successfuly replenished this denomination!");
                    }
                }while(additional < 0);
            }
        }while(choice != last_opt);
    }

    public void displayTransactionSummary(){
        System.out.println("======= Sales Summary (Starting from the latest restock) =======");
        System.out.println("Gross Sales: ₱" + this.transactionHistory.getSales());

    }

    public void collectCash(){
        double extrct_amt = this.cashInventory.extractCash();
        System.out.println("Cash Collected Successfuly!");
        System.out.println("Garnered Amount: ₱" + extrct_amt);
    }
}