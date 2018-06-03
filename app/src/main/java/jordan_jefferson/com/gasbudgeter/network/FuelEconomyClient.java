package jordan_jefferson.com.gasbudgeter.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FuelEconomyClient {

    @GET("/ympg/shared/ympgVehicle/{id}")
    Call<Integer> getClientVehicleMpg(@Path("id") String id);

    @GET("vehicle/menu/year")
    Call<List<String>> getClientVehicleYear();

    @GET("/vehicle/menu/make?year={year}")
    Call<List<String>> getClientVehicleMake(@Path("year") String year);

    @GET("/vehicle/menu/model?year={year}&make={make}")
    Call<List<String>> getClientVehicleModel(@Path("year") String year, @Path("make") String make);

    @GET("/vehicle/menu/options?year={year}&make={make}&model={model}")
    Call<List<String>> getClientVehicleTypeId(@Path("year") String year,
                                         @Path("make") String make,
                                         @Path("model") String model);

}
