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

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMpg() {
        return mpg;
    }

    public void setMpg(int mpg) {
        this.mpg = mpg;
    }

    public int getTankCapacity_gallons() {
        return tankCapacity_gallons;
    }

    public void setTankCapacity_gallons(int tankCapacity_gallons) {
        this.tankCapacity_gallons = tankCapacity_gallons;
    }

}
