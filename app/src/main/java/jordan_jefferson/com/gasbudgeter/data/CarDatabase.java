package jordan_jefferson.com.gasbudgeter.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import jordan_jefferson.com.gasbudgeter.interface_files.CarDao;

/*
Abstract Class for the Room Library per the Android Documentation at:
https://developer.android.com/training/data-storage/room/
 */

@Database(entities = {Car.class}, version = 2, exportSchema = false)
public abstract class CarDatabase extends RoomDatabase {

    public abstract CarDao carDao();

    private static CarDatabase INSTANCE;

    public static CarDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CarDatabase.class, "user_garage")
                    .build();
        }

        return INSTANCE;
    }

//    private static RoomDatabase.Callback sRoomDatabaseCallback = new Callback() {
//        /**
//         * Called when the database has been opened.
//         *
//         * @param db The database.
//         */
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            new PopulateDBAsync(INSTANCE).execute();
//        }
//    };
//
//    private static class PopulateDBAsync extends AsyncTask<Void, Void, Void> {
//
//        private final CarDao mDao;
//
//        PopulateDBAsync(CarDatabase db){
//            mDao = db.carDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            //mDao.deleteAll();
//
//            for(int i = 0; i < 5; i++){
//                mDao.insertCar(new Car(i, "Ford", "Taurus", 2010 + i, 24, 18));
//            }
//            return null;
//        }
//    }

}
