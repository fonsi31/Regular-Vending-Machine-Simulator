import java.util.Arrays;

public class CashInventory {
    private final int[] denominations = {1, 5, 10, 20, 50, 100, 200, 500, 1000};
    private int[] reserves;

    public CashInventory(){
        this.reserves = new int[denominations.length];
    }

    public int[] getDenominations(){
        return denominations;
    }

    public int[] getReserves(){
        return this.reserves;
    }

    public boolean enoughChange(double change){ //dispenses and checks at the same time
        if (change == 0){
            return true;
        }

        int high = this.denominations.length-1;
        while(high > 0 && this.denominations[high] > change){
            high--;
        }

        int[] temp_reserves = this.reserves.clone();
        double temp_change = 0;
        while (temp_change != change && high >= 0){
            if (temp_reserves[high] >  0 && this.denominations[high] + temp_change <= change){
                temp_change += denominations[high];
                temp_reserves[high]--;
            }
            else{
                high--;
            }   
        }
        if (temp_change != change){
            return false;
        }
        return true;
    }

    public void dispenseChange(double change){
        int high = this.denominations.length-1;
        double temp_change = 0;
        System.out.println();
        System.out.println("Dispensing your change...Hang in there");
        while (temp_change != change && high >= 0){
            if (this.reserves[high] >  0 && this.denominations[high] + temp_change <= change){
                temp_change += denominations[high];
                this.reserves[high]--;

                try {
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

    public void setReserves(int newQuantity, int i){
        if (newQuantity >= 0){
           this.reserves[i] = newQuantity; 
        }
    }

    public void displayDenominations(){
        for(int i = 0; i < this.denominations.length; i++){
            System.out.println((i+1) + ". \u20B1" + this.denominations[i] + "  " + this.reserves[i]);
        }
    }

    public double extractCash(){
        double extracted_amt = 0;
        for (int i = 0; i < this.denominations.length; i++){
            extracted_amt += this.denominations[i] * this.reserves[i] * 1.0;
        }
        Arrays.fill(this.reserves, 0);
        return extracted_amt;
    }
}
