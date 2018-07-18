package jordan_jefferson.com.gasbudgeter.interface_files;

import jordan_jefferson.com.gasbudgeter.directions_model.DirectionResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GoogleDirectionsClient {

    @GET()
    Call<DirectionResults> fetchDirectionResults(@Url String path);

}
