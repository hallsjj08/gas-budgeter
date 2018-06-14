package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.android.gms.maps.model.LatLngBounds;

public class RouteBound {

    private LatLngLocation northeast;
    private LatLngLocation southwest;

    public RouteBound(){

    }

    public LatLngLocation getNortheast() {
        return northeast;
    }

    public void setNortheast(LatLngLocation northeast) {
        this.northeast = northeast;
    }

    public LatLngLocation getSouthwast() {
        return southwest;
    }

    public void setSouthwast(LatLngLocation southwest) {
        this.southwest = southwest;
    }
    
    public LatLngBounds getLatLngBounds(){
        return new LatLngBounds(southwest.getLatLng(), northeast.getLatLng());
    }
}
