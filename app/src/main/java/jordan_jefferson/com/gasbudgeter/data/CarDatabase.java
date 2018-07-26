package jordan_jefferson.com.gasbudgeter.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import jordan_jefferson.com.gasbudgeter.interface_files.CarDao;

/*
Abstract Class for the Room Library per the Android Documentation at:
https://developer.android.com/training/data-storage/room/
 */

@Database(entities = {Car.class}, version = 3, exportSchema = false)
public abstract class CarDatabase extends RoomDatabase {

    public abstract CarDao carDao();

    private static CarDatabase INSTANCE;

    public static CarDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CarDatabase.class, "user_garage")
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }

        return INSTANCE;
    }

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE cars_new (_id INTEGER NOT NULL, pic_path TEXT, carId INTEGER NOT NULL," +
                    "make TEXT, model TEXT, year INTEGER NOT NULL, vehicle_type TEXT, fuel_type TEXT, " +
                    "city_mpg INTEGER NOT NULL, hwy_mpg INTEGER NOT NULL, PRIMARY KEY(_id))");

            database.execSQL("INSERT INTO cars_new (carId, make, model, year, fuel_type, city_mpg, hwy_mpg)" +
                    "SELECT carId, make, model, year, fuel_type, city_mpg, hwy_mpg FROM cars");

            database.execSQL("DROP TABLE cars");

            database.execSQL("ALTER TABLE cars_new RENAME TO cars");
        }
    };

}
