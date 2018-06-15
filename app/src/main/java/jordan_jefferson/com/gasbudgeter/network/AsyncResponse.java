package jordan_jefferson.com.gasbudgeter.network;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

public interface AsyncResponse {

    void onDirectionResultsUpdate(LatLngBounds routeBounds, PolylineOptions routeOverview);

}
