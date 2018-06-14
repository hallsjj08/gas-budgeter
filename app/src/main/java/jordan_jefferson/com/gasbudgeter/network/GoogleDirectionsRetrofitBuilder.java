package jordan_jefferson.com.gasbudgeter.network;

import android.util.Log;

import jordan_jefferson.com.gasbudgeter.directions_model.DirectionResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleDirectionsRetrofitBuilder {

    private static final String DIRECTIONS_BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private static final String TAG = "DIRECTION_RESULTS";
    private GoogleDirectionsClient googleDirectionsClient;
    private DirectionResults directionResults;

    public GoogleDirectionsRetrofitBuilder(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DIRECTIONS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleDirectionsClient = retrofit.create(GoogleDirectionsClient.class);
    }

    public synchronized DirectionResults getDirectionResults(String url) {

        Call<DirectionResults> data = googleDirectionsClient.fetchDirectionResults(url);

        data.enqueue(new Callback<DirectionResults>() {
            @Override
            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
                directionResults = response.body();
                try{
                    Log.d(TAG, "Result Status: " + directionResults.getStatus());
                    Log.d(TAG, "Number of Routes: " + directionResults.getRoutes().size());
                }catch(NullPointerException e){
                    Log.e(TAG, "Null Object");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<DirectionResults> call, Throwable t) {
                directionResults = null;
                Log.d(TAG, t.getMessage());
            }
        });

        return directionResults;
    }
}
