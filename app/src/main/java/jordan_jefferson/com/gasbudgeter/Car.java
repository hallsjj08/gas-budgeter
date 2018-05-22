package jordan_jefferson.com.gasbudgeter;

public class Car {

    private String make;
    private String model;
    private int year;
    private int mpg;
    private int tankCapacity_gallons;

    public Car(String make, String model, int year, int mpg, int tankCapacity_gallons){

        this.make = make;
        this.model = model;
        this.year = year;
        this.mpg = mpg;
        this.tankCapacity_gallons = tankCapacity_gallons;

    }

    public double costOfGas(double priceOfGas, int totalDistance_miles){
        return priceOfGas/(mpg/totalDistance_miles);
    }

    public int distanceTraveledOnFullTank(){
        return mpg * tankCapacity_gallons;
    }

}
