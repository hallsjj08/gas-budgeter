package jordan_jefferson.com.gasbudgeter.view_model;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.data.CarDatabase;

public class Garage {

    private static Garage sGarage;
    private ArrayList<Car> cars;

    private CarDatabase carDB;

    private Garage(Context context){

        InitDBTask userGarage = new InitDBTask(context);
        userGarage.execute();

    }

    public static Garage getInstance(Context appContext){
        if(sGarage == null){
            sGarage = new Garage(appContext);
        }

        return sGarage;
    }

    private class InitDBTask extends AsyncTask<Void, Void, Void>{

        private Context mContext;

        private InitDBTask(Context context){
            this.mContext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            carDB = Room.databaseBuilder(mContext, CarDatabase.class, "user_garage")
                    .build();

            cars = carDB.carDao().getCars();

            return null;

        }
    }

    public void addCarToGarage(Car car){
        cars.add(car);
        AddCarToGarageTask addCarToGarageTask = new AddCarToGarageTask();
        addCarToGarageTask.execute(car);
    }

    private class AddCarToGarageTask extends AsyncTask<Car, Void, Void>{

        @Override
        protected Void doInBackground(Car... cars) {

            carDB.carDao().insertCar(cars[0]);

            return null;
        }
    }

    public void removeCarFromGarage(int position){
        RemoveCarFromGarageTask removeCarFromGarageTask = new RemoveCarFromGarageTask();
        removeCarFromGarageTask.execute(cars.get(position));
        cars.remove(position);
    }

    private class RemoveCarFromGarageTask extends AsyncTask<Car, Void, Void>{

        @Override
        protected Void doInBackground(Car... cars) {

            carDB.carDao().delete(cars[0]);

            return null;
        }
    }

    public Car getCar(int position){
        return cars.get(position);
    }

    public ArrayList<Car> getAllCars(){
        return cars;
    }

}
