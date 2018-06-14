package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Leg {

    @SerializedName("distance")
    private Distance legDistance;

    @SerializedName("duration")
    private TravelTime legDuration;

    @SerializedName("end_address")
    private String endAddress;

    @SerializedName("start_address")
    private String startAddress;

    @SerializedName("end_location")
    private LatLngLocation endLocation;

    @SerializedName("start_location")
    private LatLngLocation startLocation;

    private List<Step> steps = null;

    @SerializedName("traffic_speed_entry")
    private List<String> trafficSpeedEntry = null;

    @SerializedName("via_waypoint")
    private List<Waypoint> viaWaypoint = null;

    public Leg() {
        this.endAddress = "";
        this.startAddress = "";
    }

    public Distance getLegDistance() {
        return legDistance;
    }

    public void setLegDistance(Distance legDistance) {
        this.legDistance = legDistance;
    }

    public TravelTime getLegDuration() {
        return legDuration;
    }

    public void setLegDuration(TravelTime legDuration) {
        this.legDuration = legDuration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public LatLngLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLngLocation endLocation) {
        this.endLocation = endLocation;
    }

    public LatLngLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLngLocation startLocation) {
        this.startLocation = startLocation;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<String> getTrafficSpeedEntry() {
        return trafficSpeedEntry;
    }

    public void setTrafficSpeedEntry(List<String> trafficSpeedEntry) {
        this.trafficSpeedEntry = trafficSpeedEntry;
    }

    public List<Waypoint> getViaWaypoint() {
        return viaWaypoint;
    }

    public void setViaWaypoint(List<Waypoint> viaWaypoint) {
        this.viaWaypoint = viaWaypoint;
    }
}
