package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.gson.annotations.SerializedName;

public class Waypoint {

    @SerializedName("location")
    private LatLngLocation waypointLocation;

    @SerializedName("step_index")
    private int stepIndex;

    @SerializedName("step_interpolation")
    private float stepInterpolation;

    public Waypoint() {
        this.stepIndex = 0;
        this.stepInterpolation = 0;
    }

    public LatLngLocation getWaypointLocation() {
        return waypointLocation;
    }

    public void setWaypointLocation(LatLngLocation waypointLocation) {
        this.waypointLocation = waypointLocation;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public float getStepInterpolation() {
        return stepInterpolation;
    }

    public void setStepInterpolation(float stepInterpolation) {
        this.stepInterpolation = stepInterpolation;
    }
}
