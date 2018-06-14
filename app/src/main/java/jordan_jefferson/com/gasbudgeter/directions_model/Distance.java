package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.gson.annotations.SerializedName;

public class Distance {

    @SerializedName("text")
    private String distance;

    @SerializedName("value")
    private long meters;

    public Distance(){
        this.distance = "";
        this.meters = 0;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public long getMeters() {
        return meters;
    }

    public void setMeters(long meters) {
        this.meters = meters;
    }
}
