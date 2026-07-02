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

    public boolean enoughChange(double change){
        int high = this.denominations.length-2;
        while(this.denominations[high] > change){
            high--;
        }

        double temp = 0;
        while (temp != change && high >= 0){
            if (this.reserves[high] != 0 && this.denominations[high] + temp <= change){
                temp += denominations[high];
                this.setReserves(this.reserves[high]-1, high);
            }
            else{
                high--;
            }
        }
        if (temp != change){
            return false;
        }
        return true;
    }

    public void setReserves(int newQuantity, int i){
        if (newQuantity >= 0){
           this.reserves[i] = newQuantity; 
        }
    }

    public void displayDenominations(){
        for(int i = 0; i < this.denominations.length; i++){
            System.out.println((i+1) + ". ₱" + this.denominations[i]);
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
