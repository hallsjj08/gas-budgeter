package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("distance")
    private Distance stepDistance;

    @SerializedName("duration")
    private TravelTime stepDuration;

    @SerializedName("end_location")
    private LatLngLocation endLocation;

    @SerializedName("start_location")
    private LatLngLocation startLocation;

    @SerializedName("html_instructions")
    private String htmlInstructions;

    @SerializedName("polyline")
    private PolyPoints stepPolyline;

    @SerializedName("travel_mode")
    private String travelMode;

    public Step() {
        this.htmlInstructions = "";
        this.travelMode = "";
    }

    public Distance getStepDistance() {
        return stepDistance;
    }

    public void setStepDistance(Distance stepDistance) {
        this.stepDistance = stepDistance;
    }

    public TravelTime getStepDuration() {
        return stepDuration;
    }

    public void setStepDuration(TravelTime stepDuration) {
        this.stepDuration = stepDuration;
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

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public PolyPoints getStepPolyline() {
        return stepPolyline;
    }

    public void setStepPolyline(PolyPoints stepPolyline) {
        this.stepPolyline = stepPolyline;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }
}
