package jordan_jefferson.com.gasbudgeter.network;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.data.Car;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class FuelEconomyRetrofitBuilder {

    private static final String FUEL_ECONOMY_BASE_URL = "https://www.fueleconomy.gov/ws/rest/";
    private String year;
    private String make;
    private String model;
    private Car myCar;
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

        data.enqueue(new Callback<ClientListItems>() {
            @Override
            public void onResponse(Call<ClientListItems> call, Response<ClientListItems> response) {
                clientItems = response.body().clientItemList;
            }

            @Override
            public void onFailure(Call<ClientListItems> call, Throwable t) {

            }
        });

        return clientItems;
    }

    private Car newCar(String id){
        Call<Car> car = fuelEconomyClient.getClientVehicleData(id);

        car.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                myCar = response.body();

            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {

            }
        });

        return myCar;
    }

    public void previousDataType(){
        dataType--;
    }

}
