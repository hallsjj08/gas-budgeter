package jordan_jefferson.com.gasbudgeter.gui;


import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.network.AsyncResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripMaps#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripMaps extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        LocationListener, PlaceSelectionListener, AsyncResponse {

    private static final String TAG = "TripMapsFragment";

    private GoogleMap mMap;
//    private View view;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportPlaceAutocompleteFragment autocompleteFragment;

    public TripMaps() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
//        if(view == null){
//            view = inflater.inflate(R.layout.fragment_trip_maps, container, false);
//        }

        View view = inflater.inflate(R.layout.fragment_trip_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.tripMap);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager()
                .findFragmentById(R.id.trip_place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(this);

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

    }

    @Override
    public void onError(Status status) {

    }

    @Override
    public void onDirectionResultsUpdate(LatLngBounds routeBounds, PolylineOptions routeOverview) {

    }
}