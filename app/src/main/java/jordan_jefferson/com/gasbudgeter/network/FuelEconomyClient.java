package jordan_jefferson.com.gasbudgeter.network;

import jordan_jefferson.com.gasbudgeter.data.Car;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FuelEconomyClient {

    @GET("vehicle/{id}")
    Call<Car> getClientVehicleData(@Path("id") String id);

    @GET("vehicle/menu/year")
    Call<ClientListItems> getClientVehicleYear();

    @GET("vehicle/menu/make")
    Call<ClientListItems> getClientVehicleMake(@Query("year") String year);

    @GET("vehicle/menu/model")
    Call<ClientListItems> getClientVehicleModel(@Query("year") String year, @Query("make") String make);

    @GET("vehicle/menu/options")
    Call<ClientListItems> getClientVehicleTypeId(@Query("year") String year,
                                         @Query("make") String make,
                                         @Query("model") String model);

}
