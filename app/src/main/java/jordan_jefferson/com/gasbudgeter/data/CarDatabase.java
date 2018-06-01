package jordan_jefferson.com.gasbudgeter.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.data.CarDao;

/*
Abstract Class for the Room Library per the Android Documentation at:
https://developer.android.com/training/data-storage/room/
 */

@Database(entities = {Car.class}, version = 1)
public abstract class CarDatabase extends RoomDatabase {

    public abstract CarDao carDao();

    private static CarDatabase INSTANCE;

    public static CarDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CarDatabase.class, "user_garage").build();
        }

        return INSTANCE;
    }

}
