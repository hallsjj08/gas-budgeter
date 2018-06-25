package jordan_jefferson.com.gasbudgeter.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/*
A public interface to define needed queries for database operations.
See https://developer.android.com/training/data-storage/room/accessing-data for query documentation.
 */

@Dao
public interface CarDao {

    @Query("SELECT * FROM cars ORDER BY make ASC")
    LiveData<List<Car>> getCars();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCar(Car car);

    @Delete
    void delete(Car car);

}
