package jordan_jefferson.com.gasbudgeter.data;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import jordan_jefferson.com.gasbudgeter.network.ClientItem;
import jordan_jefferson.com.gasbudgeter.network.FuelEconomyRetrofitBuilder;

public class FuelEconomyRepository {

    private FuelEcoDataAsync fuelEcoDataAsync;
    private FuelEcoCarDataAsync fuelEcoCarDataAsync;
    private FuelEconomyRetrofitBuilder fuelEconomyRetrofitBuilder;
    private static final String TAG = "REPO";

    private static String sVehicleType;

    public interface AsyncResponseCallbacks{
        void onPreExecute();
        void onPostDataExecute(List<ClientItem> newClientItems);
        void onPostCarExecute(Car newCar);
        void onPostErrorExecute();
    }

    public static AsyncResponseCallbacks callbacks = null;

    public FuelEconomyRepository() {

        this.fuelEconomyRetrofitBuilder = new FuelEconomyRetrofitBuilder();

    }

    public int getDataType(){
        return fuelEconomyRetrofitBuilder.getDataType();
    }

    public void setDataType(ArrayList<String> carParams, int dataType) {
        fuelEconomyRetrofitBuilder.setDataType(carParams, dataType);
    }

    public void fetchApiListData(String selectedItem){
        this.fuelEcoDataAsync = new FuelEcoDataAsync(fuelEconomyRetrofitBuilder);
        fuelEcoDataAsync.execute(selectedItem);
    }

    public void fetchApiCarData(String selectedItem, String vehicleType) {
        this.fuelEcoCarDataAsync = new FuelEcoCarDataAsync(fuelEconomyRetrofitBuilder);
        sVehicleType = vehicleType;
        fuelEcoCarDataAsync.execute(selectedItem); }

    public void cancelDataFetch(){
        if(fuelEcoDataAsync != null){
            fuelEconomyRetrofitBuilder.previousDataType();
            fuelEcoDataAsync.cancel(true);
        }

        if(fuelEcoCarDataAsync != null){
            fuelEconomyRetrofitBuilder.previousDataType();
            fuelEcoCarDataAsync.cancel(true);
        }

    }

    private static class FuelEcoDataAsync extends AsyncTask<String, Void, Boolean> {

        private FuelEconomyRetrofitBuilder fuelEconomyRetrofitBuilder;
        private List<ClientItem> clientItems;

        FuelEcoDataAsync(FuelEconomyRetrofitBuilder feRetroBuild){ this.fuelEconomyRetrofitBuilder = feRetroBuild; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callbacks.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            clientItems = fuelEconomyRetrofitBuilder.fetchData(strings[0]);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean && clientItems != null){
                callbacks.onPostDataExecute(clientItems);
            }else{
//                Log.d(TAG, "Execution Failed");
                fuelEconomyRetrofitBuilder.cancelDataTransaction();
                callbacks.onPostErrorExecute();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
//            Log.d(TAG, "Canceled Transactions");
            fuelEconomyRetrofitBuilder.cancelDataTransaction();
        }
    }

    private static class FuelEcoCarDataAsync extends AsyncTask<String, Void, Boolean> {

        private FuelEconomyRetrofitBuilder fuelEconomyRetrofitBuilder;
        private Car newCar;

        FuelEcoCarDataAsync(FuelEconomyRetrofitBuilder feRetroBuild){ this.fuelEconomyRetrofitBuilder = feRetroBuild; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callbacks.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            newCar = fuelEconomyRetrofitBuilder.fetchCar(strings[0]);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean && newCar != null){
                newCar.setVehicleType(sVehicleType);
                callbacks.onPostCarExecute(newCar);
            }else{
//                Log.d(TAG, "Failed Retrieving Car");
                fuelEconomyRetrofitBuilder.cancelCarTransaction();
                callbacks.onPostErrorExecute();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            fuelEconomyRetrofitBuilder.cancelCarTransaction();
        }
    }
}
