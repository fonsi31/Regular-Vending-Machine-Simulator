public class Item {
    private final String name;
    private double price;
    private final int calories;

    public Item(String name, double price, int calories){
        this.name = name;
        this.price = price;
        this.calories = calories;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public int getCal(){
        return this.calories;
    }

    public void setPrice(double newPrice){
        if(newPrice >= 0){
            this.price = newPrice;
        }
    }
}