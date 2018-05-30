package jordan_jefferson.com.gasbudgeter.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/*
This class uses the Room Persistance Library.
Entity sets the contract for the table in the SQLite db.
@ColumnInfo is only needed to name columns, otherwise it uses the class fields to name columns.
@ColumnInfo is used here to help with code readability.
Getters and Setters are required for the Room Library to work.
Parameter carId will be assigned from the fueleconomy.gov API.
 */

@Entity(tableName = "cars")
public class Car {

    @PrimaryKey
    private int carId;

    @ColumnInfo(name = "make")
    private String make;

    @ColumnInfo(name = "model")
    private String model;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "mpg")
    private int mpg;

    @ColumnInfo(name = "tank_capacity_gallons")
    private int tankCapacity_gallons;

    public Car(int carId, String make, String model, int year, int mpg, int tankCapacity_gallons){

        this.carId = carId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.mpg = mpg;
        this.tankCapacity_gallons = tankCapacity_gallons;

    }

    public int distanceTraveledOnFullTank(){
        return mpg * tankCapacity_gallons;
    }

    public double costOfGas(double priceOfGas, int totalDistance_miles){
        return priceOfGas/(mpg/totalDistance_miles);
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
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
