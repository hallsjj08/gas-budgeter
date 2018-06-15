package jordan_jefferson.com.gasbudgeter.network;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import jordan_jefferson.com.gasbudgeter.directions_model.DirectionResults;
import jordan_jefferson.com.gasbudgeter.test_gui.TestMapsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleDirectionsRetrofitBuilder extends AsyncTask<String, Void, String> {

    private static final String DIRECTIONS_BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private static final String TAG = "DIRECTION_RESULTS";
    private GoogleDirectionsClient googleDirectionsClient;
    private DirectionResults directionResults;
    private List<LatLng> polylineOverview;
    public AsyncResponse delegate = null;
    private LatLngBounds bounds;
    private PolylineOptions polyOverview;

    public GoogleDirectionsRetrofitBuilder(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DIRECTIONS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleDirectionsClient = retrofit.create(GoogleDirectionsClient.class);
    }

    @Override
    protected String doInBackground(String... urls) {
        Call<DirectionResults> data = googleDirectionsClient.fetchDirectionResults(urls[0]);

        try {
            directionResults = data.execute().body();
            polylineOverview = directionResults.getRoutes().get(0).getRoutePolyline().decodePolyPoints();
//            Thread.sleep(10000);
            Iterable<LatLng> asdf = polylineOverview;
            polyOverview = new PolylineOptions().addAll(asdf);
            bounds = directionResults.getRoutes().get(0).getRouteBounds().getLatLngBounds();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, directionResults.getRoutes().get(0).getRoutePolyline().getPoints());
        return "executed";
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.onDirectionResultsUpdate(bounds, polyOverview);
    }

    public DirectionResults getDirectionResults(String url) {

//        Call<DirectionResults> data = googleDirectionsClient.fetchDirectionResults(url);
//
//        data.enqueue(new Callback<DirectionResults>() {
//            @Override
//            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
//                directionResults = response.body();
//                try{
//                    Log.d(TAG, "Result Status: " + directionResults.getStatus());
//                    Log.d(TAG, "Number of Routes: " + directionResults.getRoutes().size());
//                }catch(NullPointerException e){
//                    Log.e(TAG, "Null Object");
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<DirectionResults> call, Throwable t) {
//                directionResults = null;
//                Log.d(TAG, t.getMessage());
//            }
//        });

        return directionResults;
    }
}
