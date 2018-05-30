package jordan_jefferson.com.gasbudgeter.view_model;

import android.content.Context;

import java.util.ArrayList;

import jordan_jefferson.com.gasbudgeter.data.Car;

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

    public Car getCar(int position){
        return cars.get(position);
    }

}
