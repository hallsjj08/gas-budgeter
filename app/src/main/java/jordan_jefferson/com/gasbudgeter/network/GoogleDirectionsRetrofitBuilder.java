package jordan_jefferson.com.gasbudgeter.network;

import android.util.Log;

import java.io.IOException;

import jordan_jefferson.com.gasbudgeter.directions_model.DirectionResults;
import jordan_jefferson.com.gasbudgeter.interface_files.GoogleDirectionsClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleDirectionsRetrofitBuilder{

    private static final String DIRECTIONS_BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private static final String TAG = "DIRECTION_RESULTS";
    private GoogleDirectionsClient googleDirectionsClient;
    private DirectionResults directionResults;
    private Call<DirectionResults> data;

    public GoogleDirectionsRetrofitBuilder(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DIRECTIONS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleDirectionsClient = retrofit.create(GoogleDirectionsClient.class);
    }

    public DirectionResults fetchDirectionResults(String directionsPath){
        data = googleDirectionsClient.fetchDirectionResults(directionsPath);

        try {
            directionResults = data.execute().body();
            Log.d(TAG, "Executed Successfully");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Something went wrong.");
        }
        return directionResults;
    }

    public void cancelDirectionsFetch(){
        data.cancel();
    }
}
