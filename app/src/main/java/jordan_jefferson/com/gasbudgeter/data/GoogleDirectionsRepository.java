package jordan_jefferson.com.gasbudgeter.data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.directions_model.DirectionResults;
import jordan_jefferson.com.gasbudgeter.network.GoogleDirectionsRetrofitBuilder;

public class GoogleDirectionsRepository {

    private GoogleDirectionsRetrofitBuilder googleDirectionsRetrofitBuilder;

    public interface DirectionsAsyncResponseCallbacks{
        void onPreExecute();
        void onSuccessPostExecute(LatLngBounds routeBounds, PolylineOptions routeOverview,
                           String miles, String travelTime, long meters);
        void onErrorPostExecute(String status, String errorMessage);
    }

    public static DirectionsAsyncResponseCallbacks callbacks = null;

    public GoogleDirectionsRepository(){
        this.googleDirectionsRetrofitBuilder = new GoogleDirectionsRetrofitBuilder();
    }

    public void fetchDirections(String directionsPath){
        GoogleDirectionsAsync googleDirectionsAsync = new GoogleDirectionsAsync(googleDirectionsRetrofitBuilder);
        googleDirectionsAsync.execute(directionsPath);
    }

    private static class GoogleDirectionsAsync extends AsyncTask<String, Void, Void>{

        private GoogleDirectionsRetrofitBuilder googleDirectionsRetrofitBuilder;
        private DirectionResults directionResults;
        private LatLngBounds bounds;
        private PolylineOptions polyOverview;
        private List<LatLng> polylineOverview;
        private String miles;
        private String travelTime;
        private long meters;

        String status;
        String errorMessage;

        GoogleDirectionsAsync(GoogleDirectionsRetrofitBuilder googleDirectionsRetrofitBuilder){
            this.googleDirectionsRetrofitBuilder = googleDirectionsRetrofitBuilder;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callbacks.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            this.directionResults = googleDirectionsRetrofitBuilder.fetchDirectionResults(strings[0]);
            if(directionResults != null){
                Log.d("STATUS", directionResults.getStatus());
                if(directionResults.getStatus().equals("OK")){
                    status = directionResults.getStatus();
                    polylineOverview = directionResults.getRoutes().get(0).getRoutePolyline().decodePolyPoints();
                    Iterable<LatLng> iteratedLatLng = polylineOverview;
                    polyOverview = new PolylineOptions().addAll(iteratedLatLng);
                    bounds = directionResults.getRoutes().get(0).getRouteBounds().getLatLngBounds();
                    miles = directionResults.getRoutes().get(0).getLegs().get(0).getLegDistance().getDistance();
                    meters = directionResults.getRoutes().get(0).getLegs().get(0).getLegDistance().getMeters();
                    travelTime = directionResults.getRoutes().get(0).getLegs().get(0).getLegDuration().getTravelDuration();
                }else{
                    status = directionResults.getStatus();
                    errorMessage = directionResults.getErrorMessage();
                    Log.e("RESULTS", status + ", " + errorMessage);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(status.equals("OK")){
                callbacks.onSuccessPostExecute(bounds, polyOverview,
                        miles, travelTime, meters);
            }else{
                callbacks.onErrorPostExecute(status, errorMessage);
            }
        }
    }

}
