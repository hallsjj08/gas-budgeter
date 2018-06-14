package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {

    private String copyrights;

    private String summary;

    private List<String> warnings = null;

    private List<Leg> legs = null;

    @SerializedName("bounds")
    private RouteBound routeBounds;

    @SerializedName("overview_polyline")
    private PolyPoints routePolyline;

    @SerializedName("waypoint_order")
    private List<String> waypointOrder = null;

    public Route(){
        this.copyrights = "";
        this.summary = "";
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public RouteBound getRouteBounds() {
        return routeBounds;
    }

    public void setRouteBounds(RouteBound routeBounds) {
        this.routeBounds = routeBounds;
    }

    public PolyPoints getRoutePolyline() {
        return routePolyline;
    }

    public void setRoutePolyline(PolyPoints routePolyline) {
        this.routePolyline = routePolyline;
    }

    public List<String> getWaypointOrder() {
        return waypointOrder;
    }

    public void setWaypointOrder(List<String> waypointOrder) {
        this.waypointOrder = waypointOrder;
    }
}
