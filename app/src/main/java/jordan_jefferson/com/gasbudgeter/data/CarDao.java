package jordan_jefferson.com.gasbudgeter.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.data.Car;

/*
A public interface to define needed queries for database operations.
See https://developer.android.com/training/data-storage/room/accessing-data for query documentation.
 */

@Dao
public interface CarDao {

    @Query("SELECT * FROM cars")
    List<Car> getCars();

    @Insert
    void insertAll(Car... cars);

    @Delete
    void delete(Car car);

}
