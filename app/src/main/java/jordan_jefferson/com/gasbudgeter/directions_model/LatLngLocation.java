package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.android.gms.maps.model.LatLng;

public class LatLngLocation {

    private double lat;
    private double lng;

    public LatLngLocation(){
        this.lat = 0;
        this.lng = 0;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng getLatLng(){
        return new LatLng(lat, lng);
    }
}
