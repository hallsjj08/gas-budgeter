package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionResults {

    private String status;

    @SerializedName("available_travel_modes")
    private List<String> availableTravelModes = null;

    @SerializedName("error_message")
    private String errorMessage;

    @SerializedName("geocoded_waypoints")
    private List<GeocodedWaypoint> geocodedWaypoints = null;

    @SerializedName("routes")
    private List<Route> routes = null;

    public DirectionResults(){
        this.status = "";
        this.errorMessage = "";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getAvailableTravelModes() {
        return availableTravelModes;
    }

    public void setAvailableTravelModes(List<String> availableTravelModes) {
        this.availableTravelModes = availableTravelModes;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
