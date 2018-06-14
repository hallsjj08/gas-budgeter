package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class PolyPoints {

    private String points;

    public PolyPoints(){
        this.points = "";
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    //TODO: Create logic to decode variable points into a list of LatLng values to show route overview.
    public List<LatLng> decodePolyPoints(){
        return null;
    }
}
