package jordan_jefferson.com.gasbudgeter;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

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
