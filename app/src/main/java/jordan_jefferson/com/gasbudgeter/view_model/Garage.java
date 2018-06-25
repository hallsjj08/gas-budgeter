package jordan_jefferson.com.gasbudgeter.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.data.GarageRepository;

public class Garage extends AndroidViewModel{

    private GarageRepository mGarageRepository;
    private LiveData<List<Car>> cars;

    public Garage(@NonNull Application application){
        super(application);

        mGarageRepository = new GarageRepository(application);
        cars = mGarageRepository.getAllCars();
    }

    public LiveData<List<Car>> getCars(){
        return cars;
    }

    public void insert(Car car){
        mGarageRepository.insert(car);
    }

    public void delete(Car car) { mGarageRepository.delete(car); }

}
