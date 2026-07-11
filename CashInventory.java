import java.util.Arrays;

/**
 * Represents the cash reserves of the vending machine.
 * <p>
 * This class manages the available bill and coin denominations,
 * determines whether enough change can be produced for a transaction,
 * dispenses change, replenishes cash reserves, and allows collection
 * of all money stored in the machine.
 *
 * @author Alfonso S. Cauilan, Daviane Nate M. Abad
 * @version 1.0
 */
public class CashInventory {

    /** The supported bill and coin denominations in Philippine Pesos. */
    private final int[] denominations = {1, 5, 10, 20, 50, 100, 200, 500, 1000};

    /** The quantity available for each denomination. */
    private int[] reserves;

    /**
     * Constructs an empty cash inventory.
     * <p>
     * All denominations are initialized with zero reserves.
     */
    public CashInventory(){
        this.reserves = new int[denominations.length];
    }

    /**
     * Returns the list of supported denominations.
     *
     * @return an array containing all supported denominations
     */
    public int[] getDenominations(){
        return denominations;
    }

    /**
     * Returns the current cash reserves.
     *
     * @return an array containing the quantity of each denomination
     */
    public int[] getReserves(){
        return this.reserves;
    }

    /**
     * Determines whether the vending machine has sufficient cash
     * reserves to provide the specified amount of change.
     *
     * <p>This method simulates dispensing change without modifying
     * the actual cash reserves.
     *
     * @param change the amount of change to be produced
     * @return {@code true} if the machine can produce the required
     *         change; {@code false} otherwise
     */
    public boolean enoughChange(double change){
        if (change == 0){
            return true;
        }

        int high = this.denominations.length - 1;
        while(high > 0 && this.denominations[high] > change){
            high--;
        }

        int[] temp_reserves = this.reserves.clone();
        double temp_change = 0;

        while (temp_change != change && high >= 0){
            if (temp_reserves[high] > 0 &&
                this.denominations[high] + temp_change <= change){

                temp_change += denominations[high];
                temp_reserves[high]--;
            }
            else{
                high--;
            }
        }

        return temp_change == change;
    }

    /**
     * Dispenses the specified amount of change using the
     * available cash reserves.
     *
     * @param change the amount of change to dispense
     */
    public void dispenseChange(double change){
        int high = this.denominations.length - 1;
        double temp_change = 0;

        System.out.println();
        System.out.println("Dispensing your change...Hang in there");

        while (temp_change != change && high >= 0){
            if (this.reserves[high] > 0 &&
                this.denominations[high] + temp_change <= change){

                temp_change += denominations[high];
                this.reserves[high]--;

                try{
                    Thread.sleep(1500);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

                System.out.println("\u20B1" + denominations[high]);
            }
            else{
                high--;
            }
        }

        System.out.println("Change: \u20B1" + change);
    }

    /**
     * Updates the quantity available for a specific denomination.
     *
     * @param newQuantity the new quantity to be stored
     * @param i the index of the denomination
     */
    public void setReserves(int newQuantity, int i){
        if (newQuantity >= 0){
            this.reserves[i] = newQuantity;
        }
    }

    /**
     * Displays all supported denominations together with their
     * available quantities.
     */
    public void displayDenominations(){
        System.out.printf("%-3s %-15s %-10s%n",
                "ID", "Denomination", "Quantity");

        for(int i = 0; i < this.denominations.length; i++){
            System.out.printf("%-3d %s%-14d %-10d%n",
                    i + 1,
                    "\u20B1",
                    this.denominations[i],
                    this.reserves[i]);
        }
    }

    /**
     * Collects all money currently stored in the vending machine.
     * <p>
     * After the cash is collected, all cash reserves are reset to zero.
     *
     * @return the total amount of money collected
     */
    public double extractCash(){
        double extracted_amt = 0;

        for (int i = 0; i < this.denominations.length; i++){
            extracted_amt += this.denominations[i] * this.reserves[i];
        }

        Arrays.fill(this.reserves, 0);

        return extracted_amt;
    }
}