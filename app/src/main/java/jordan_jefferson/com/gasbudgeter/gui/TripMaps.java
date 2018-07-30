package jordan_jefferson.com.gasbudgeter.gui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.interface_files.AsyncResponse;
import jordan_jefferson.com.gasbudgeter.network.GoogleDirectionsRetrofitBuilder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripMaps#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripMaps extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        LocationListener, PlaceSelectionListener, AsyncResponse {

    private static final String TAG = "TripMapsFragment";
    private static final float DEFAULT_ZOOM = 15f;

    private static final String[] TEST_URL = {"https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Knoxville,TN&key=AIzaSyBztmrBqLEv5fO-NjmNXg66ztVK_Si99Qw"};
    private static final String API_KEY = "key=AIzaSyBztmrBqLEv5fO-NjmNXg66ztVK_Si99Qw";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    private String origin = "origin=";
    private String destination = "destination=";
    private String[] directionsUrl = new String[1];

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private Location lastKnownLocation;

    private LinearLayout placeSelectedBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView tvPlace;

    public TripMaps() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    public static TripMaps newInstance() {
        TripMaps fragment = new TripMaps();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        Log.d(TAG, "Created");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trip_maps, container, false);

        placeSelectedBottomSheet = view.findViewById(R.id.place_selected_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(placeSelectedBottomSheet);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.tripMap);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager()
                .findFragmentById(R.id.trip_place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(this);

        tvPlace = view.findViewById(R.id.place_name);
        Button bDirections = view.findViewById(R.id.directions);

        bDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionsUrl[0] = BASE_URL + origin + "&" + destination + "&" + API_KEY;
                Log.d(TAG, directionsUrl[0]);
                GoogleDirectionsRetrofitBuilder googleDirectionsRetrofitBuilder = new GoogleDirectionsRetrofitBuilder();
                googleDirectionsRetrofitBuilder.delegate = TripMaps.this;
                googleDirectionsRetrofitBuilder.execute(directionsUrl);
            }
        });

        Log.d(TAG, "View Created");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Activity Created");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        getDeviceLocation();
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation(){
        assert getActivity() != null;
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    lastKnownLocation = location;
                    origin = origin + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                    LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    updateCamera(latLng, DEFAULT_ZOOM, "Current Location");
                }
            }
        });
    }

    private void updateCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        mMap.clear();

        if(!title.equals("Current Location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onPlaceSelected(Place place) {
        String placeName = place.getName().toString();
        tvPlace.setText(placeName);
        updateCamera(place.getLatLng(), DEFAULT_ZOOM, placeName);
        if(place.getId() != null){
            destination = destination + "place_id:" + place.getId();
        }else{
            destination = destination + place.getLatLng().latitude + "," + place.getLatLng().longitude;
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(getActivity(), "Unfortunately there was an error locating your place.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, status.getStatusMessage());
    }

    @Override
    public void onDirectionResultsUpdate(LatLngBounds routeBounds, PolylineOptions routeOverview) {
        if(mMap != null){
            mMap.addPolyline(routeOverview);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 150));
        }else{
            Log.d(TAG, "Error Loading Routes");
        }
    }
}
