package jordan_jefferson.com.gasbudgeter.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/*
This class uses the Room Persistance Library.
Entity sets the contract for the table in the SQLite db.
@ColumnInfo is only needed to name columns, otherwise it uses the class fields to name columns.
@ColumnInfo is used here to help with code readability.
Getters and Setters are required for the Room Library to work.
Parameter carId will be assigned from the fueleconomy.gov API.
 */

@Entity(tableName = "cars")
@Root(name = "vehicle", strict = false)
public class Car implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "pic_path")
    private String carPicPath;

    @Element(name = "id")
    private int carId;

    @ColumnInfo(name = "make")
    @Element(name = "make")
    private String make;

    @ColumnInfo(name = "model")
    @Element(name = "model")
    private String model;

    @ColumnInfo(name = "year")
    @Element(name = "year")
    private int year;

    @ColumnInfo(name = "vehicle_type")
    private String vehicleType;

    @ColumnInfo(name = "fuel_type")
    @Element(name = "fuelType1")
    private String fuelType;

    @ColumnInfo(name = "city_mpg")
    @Element(name = "city08")
    private int city_mpg;

    @ColumnInfo(name = "hwy_mpg")
    @Element(name = "highway08")
    private int hwy_mpg;

//    @ColumnInfo(name = "tank_capacity_gallons")
//    private int tankCapacity_gallons;

    //Retrofit needs an empty default constructor to build the object from xml data.
    public Car(){}

    public Car(int id, String picPath, int carId, String make, String model,
               int year, String vehicleType, String fuelType, int city_mpg, int hwy_mpg){

        this.id = id;
        this.carPicPath = picPath;
        this.carId = carId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.city_mpg = city_mpg;
        this.hwy_mpg = hwy_mpg;
//        this.tankCapacity_gallons = tankCapacity_gallons;

    }

//    public int distanceTraveledOnFullTank(){
//        return hwy_mpg * tankCapacity_gallons;
//    }

    public double costOfGas(double priceOfGas, int totalDistance_miles){
        return priceOfGas/(hwy_mpg/totalDistance_miles);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarPicPath() {
        return carPicPath;
    }

    public void setCarPicPath(String carPicPath) {
        this.carPicPath = carPicPath;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getCity_mpg() {
        return city_mpg;
    }

    public void setCity_mpg(int city_mpg) {
        this.city_mpg = city_mpg;
    }

    public int getHwy_mpg() {
        return hwy_mpg;
    }

    public void setHwy_mpg(int hwy_mpg) {
        this.hwy_mpg = hwy_mpg;
    }

//        public int getTankCapacity_gallons() {
//        return tankCapacity_gallons;
//    }

//    public void setTankCapacity_gallons(int tankCapacity_gallons) {
//        this.tankCapacity_gallons = tankCapacity_gallons;
//    }

}
