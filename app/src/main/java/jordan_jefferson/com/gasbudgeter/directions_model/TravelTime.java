package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.gson.annotations.SerializedName;

public class TravelTime {

    @SerializedName("text")
    private String travelDuration;

    @SerializedName("value")
    private long seconds;

    public TravelTime() {
        this.seconds = 0;
        this.travelDuration = "";
    }

    public String getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(String travelDuration) {
        this.travelDuration = travelDuration;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }
}
