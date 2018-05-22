package jordan_jefferson.com.gasbudgeter;

import android.content.Context;

import java.util.ArrayList;

public class Garage {

    private static Garage sGarage;
    private ArrayList<Car> cars;

    private Garage(){

    }

    public static Garage getInstance(Context appContext){
        if(sGarage == null){
            sGarage = new Garage();
        }

        return sGarage;
    }

    public double costOfGas(double priceOfGas, int totalDistance_miles, int position){
        return priceOfGas/(cars.get(position).getMpg()/totalDistance_miles);
    }

    public int distanceTraveledOnFullTank(int position){
        return cars.get(position).getMpg() * cars.get(position).getTankCapacity_gallons();
    }

    public Car getCar(int position){
        return cars.get(position);
    }

}
