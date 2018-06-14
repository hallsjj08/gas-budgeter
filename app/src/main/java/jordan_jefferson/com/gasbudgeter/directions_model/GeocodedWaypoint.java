package jordan_jefferson.com.gasbudgeter.directions_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeocodedWaypoint {

    @SerializedName("geocoder_status")
    private String geocoderStatus;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("types")
    private List<String> types = null;

    public GeocodedWaypoint(){
        this.geocoderStatus = "";
        this.placeId = "";
    }

    public String getGeocoderStatus() {
        return geocoderStatus;
    }

    public void setGeocoderStatus(String geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
