package jordan_jefferson.com.gasbudgeter.network;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.interface_files.FuelEconomyClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class FuelEconomyRetrofitBuilder {

    private static final String FUEL_ECONOMY_BASE_URL = "https://www.fueleconomy.gov/ws/rest/";
    private static final String TAG = "Fuel Retrofit Builder";
    private String year;
    private String make;
    private String model;
    private Car myCar;
    private Call<Car> car;
    private Call<ClientListItems> data;
    private FuelEconomyClient fuelEconomyClient;
    private List<ClientItem> clientItems;
    private int dataType;

    public FuelEconomyRetrofitBuilder(){
        dataType = 0;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FUEL_ECONOMY_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        fuelEconomyClient = retrofit.create(FuelEconomyClient.class);
    }

    public List<ClientItem> fetchData(String clientItemSelected){

        switch (dataType){
            case 0:
                data = fuelEconomyClient.getClientVehicleYear();
                dataType++;
                break;
            case 1:
                year = clientItemSelected;
                data = fuelEconomyClient.getClientVehicleMake(year);
                dataType++;
                break;
            case 2:
                make = clientItemSelected;
                data = fuelEconomyClient.getClientVehicleModel(year, make);
                dataType++;
                break;
            case 3:
                model = clientItemSelected;
                data = fuelEconomyClient.getClientVehicleTypeId(year, make, model);
                dataType++;
                break;
        }

        try {
            clientItems = data.execute().body().clientItemList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientItems;
    }

    public Car fetchCar(String id){
        car = fuelEconomyClient.getClientVehicleData(id);

        try {
            myCar = car.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myCar;
    }

    public void previousDataType(){
        dataType--;
    }

    public int getDataType(){
        return dataType;
    }

    public void setDataType(ArrayList<String> params, int dataType){
        this.dataType = dataType;
        this.year = params.get(0);
        this.make = params.get(1);
        this.model = params.get(2);
    }

    public void cancelDataTransaction(){
        data.cancel();
        Log.d(TAG, "Data transaction cancelled.");
    }

    public void cancelCarTransaction(){
        car.cancel();
        Log.d(TAG, "Car transaction cancelled.");
    }

}
