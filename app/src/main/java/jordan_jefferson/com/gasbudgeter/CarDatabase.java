package jordan_jefferson.com.gasbudgeter;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/*
Abstract Class for the Room Library per the Android Documentation at:
https://developer.android.com/training/data-storage/room/
 */

@Database(entities = {Car.class}, version = 1)
public abstract class CarDatabase extends RoomDatabase {

    public abstract CarDao carDao();

}
