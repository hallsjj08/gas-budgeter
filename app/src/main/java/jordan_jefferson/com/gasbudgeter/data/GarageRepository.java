package jordan_jefferson.com.gasbudgeter.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class GarageRepository {

    private CarDao mCarDao;
    private LiveData<List<Car>> mAllCars;

    public GarageRepository(Application application){
        CarDatabase db = CarDatabase.getDatabase(application);
        mCarDao = db.carDao();
        mAllCars = mCarDao.getCars();
    }

    public LiveData<List<Car>> getAllCars() {return mAllCars;}

    public void insert(Car car) {new insertAsyncTask(mCarDao).execute(car);}

    private static class insertAsyncTask extends AsyncTask<Car, Void, Void>{

        private CarDao mAsyncTaskDao;

        insertAsyncTask(CarDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Car... cars) {

            mAsyncTaskDao.insertCar(cars[0]);
            return null;
        }
    }

    public void delete(Car car) {new deleteAsyncTask(mCarDao).execute(car);}

    private static class deleteAsyncTask extends AsyncTask<Car, Void, Void>{

        private CarDao mAsyncTaskDao;

        deleteAsyncTask(CarDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Car... cars) {

            mAsyncTaskDao.delete(cars[0]);
            return null;
        }
    }

}
