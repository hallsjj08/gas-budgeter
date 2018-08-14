package jordan_jefferson.com.gasbudgeter.gui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
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

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.data.GoogleDirectionsRepository;
import jordan_jefferson.com.gasbudgeter.data_adapters.BottomSheetAdapter;
import jordan_jefferson.com.gasbudgeter.view_model.Garage;
import jordan_jefferson.com.gasbudgeter.view_model.GoogleDirections;


public class TripMaps extends Fragment implements OnMapReadyCallback, PlaceSelectionListener,
        GoogleDirectionsRepository.DirectionsAsyncResponseCallbacks {

    private static final float DEFAULT_ZOOM = 15f;
    private int locationRequestCount = 0;

    private String[] myPermissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE};

    //private static final String[] TEST_URL = {"https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Decatur,IL&key=AIzaSyBztmrBqLEv5fO-NjmNXg66ztVK_Si99Qw"};
    private static final String TAG = "TripMapsFragment";
    private static final String API_KEY = "key=AIzaSyBztmrBqLEv5fO-NjmNXg66ztVK_Si99Qw";
    private static final String FORMAT = "json?";
    private static final String PLACE = "Place Selected";
    private static final String ROUTE_OVERVIEW = "Directions";
    private static final String DEVICE_LOCATION = "User Location";
    private String mapState;
    private String origin;
    private String destination;
    private String directionsUrl;
    private String travelInfo;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private RecyclerView recyclerView;
    private BottomSheetAdapter bottomSheetAdapter;
    private SupportMapFragment mapFragment;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private GoogleDirections directionsViewModel;
    private ViewStub viewStub;
    private View inflatedStub;
    private FloatingActionButton myLocationFab;

    private BottomSheetBehavior placesBottomSheetBehavior;
    private TextView tvPlace;
    private TextView tvPlaceTitle;
    private Button bStart;
    private Button directions;
    private ProgressBar progressBar;
    private ImageButton autocompleteClearButton;
    private TextView retrieveLocText;
    private ProgressBar pbRetrieveLocation;

    private Place place;
    private boolean hasPlaceChanged;
    private MarkerOptions options;
    private PolylineOptions routeOverview;
    private LatLngBounds routeBounds;

    public TripMaps() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    public static TripMaps newInstance() {
        TripMaps fragment = new TripMaps();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        Log.d(TAG, "New Instance.");
        return new TripMaps();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d(TAG, "Has saved state.");
        }
        Log.d(TAG, "Created");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GoogleDirectionsRepository.callbacks = this;

        View view = inflater.inflate(R.layout.fragment_trip_maps, container, false);
        viewStub = view.findViewById(R.id.place_stub);
        inflatedStub = viewStub.inflate();
        viewStub.setVisibility(View.GONE);

        directions = view.findViewById(R.id.place_directions);
        retrieveLocText = view.findViewById(R.id.retrieve_location_text);
        pbRetrieveLocation = view.findViewById(R.id.pb_retrieving_location);
        tvPlace = view.findViewById(R.id.place_name);
        tvPlaceTitle = view.findViewById(R.id.place_title);

        bStart = view.findViewById(R.id.start);
        progressBar = view.findViewById(R.id.pbDirectionsLoading);

        recyclerView = view.findViewById(R.id.bottom_sheet_recyclerView);
        bottomSheetAdapter = new BottomSheetAdapter(getContext());

        ConstraintLayout placeSelectedBottomSheet = view.findViewById(R.id.place_selected_bottom_sheet);
        placesBottomSheetBehavior = BottomSheetBehavior.from(placeSelectedBottomSheet);
        if(mapState == null){
            setBottomSheetVisibility(false);
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.tripMap);
        mapFragment.getMapAsync(this);

        placesBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                View mapView = mapFragment.getView();

                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    resizeMap(mapView, LinearLayout.LayoutParams.MATCH_PARENT,
                            mapView.getMeasuredHeight() - bottomSheet.getMeasuredHeight());
                }else if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    resizeMap(mapView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                }

                Log.d(TAG, "Map Resized");
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager()
                .findFragmentById(R.id.trip_place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(this);

        assert autocompleteFragment.getView() != null;
        autocompleteClearButton = autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button);
        autocompleteClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autocompleteFragment.setText("");
                        if(mMap != null){
                            mMap.clear();
                            Log.d(TAG, "Map Cleared, Place Cleared.");
                        }
                        if(placesBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED
                                || placesBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                            setBottomSheetVisibility(false);
                        }
                    }
                });

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkLocationPermission() && isLocationEnabled()){
                    if(hasPlaceChanged){
                        origin = "origin=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                        directionsUrl = FORMAT + origin + "&" + destination + "&" + API_KEY;
                        Log.d(TAG, directionsUrl);
                        directionsViewModel = ViewModelProviders.of(TripMaps.this).get(GoogleDirections.class);
                        directionsViewModel.fetchDirections(directionsUrl);
                        viewStub.setVisibility(View.GONE);
                        hasPlaceChanged = false;
                    }else{
                        updateMap(mapState);
                    }
                }else{
                    if(!checkLocationPermission()){
                        AlertDialog.Builder alb = new AlertDialog.Builder(getContext());
                        alb.setTitle("Permissions Needed")
                                .setMessage("This feature requires your location to display the route overview " +
                                        "and directions. Would you like to change location permissions for this app?")
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "User denied Permission");
                                    }
                                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(myPermissions, 2);
                            }
                        })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .create().show();
                    }else if(!isLocationEnabled()){
                        AlertDialog.Builder alb = new AlertDialog.Builder(getContext());
                        alb.setTitle("Location Settings")
                                .setMessage("Location settings is currently disabled and is needed " +
                                        "for this feature. Would you like to change location settings " +
                                        "for this app?")
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "User denied Permission");
                                    }
                                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                getContext().startActivity(myIntent);
                            }
                        })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .create().show();
                    }

                }

            }
        });

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + place.getLatLng().latitude + ","
                                            + place.getLatLng().longitude + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if(mapIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivity(mapIntent);
                }else {
                    Snackbar.make(getView(), "Please install Google Maps to start navigation.", Snackbar.LENGTH_LONG).show();
                }

            }
        });

        myLocationFab = view.findViewById(R.id.my_location_fab);
        myLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation(true);
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

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Started.");
        lastKnownLocation = null;
        locationRequestCount = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed");
        if(checkLocationPermission() && isLocationEnabled() && mMap != null){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            getDeviceLocation(false);
            gettingLocationStub(true);
        }
    }

    private void gettingLocationStub(boolean showStub){
        if(showStub){
            tvPlaceTitle.setVisibility(View.INVISIBLE);
            directions.setVisibility(View.INVISIBLE);
            myLocationFab.setVisibility(View.INVISIBLE);
            retrieveLocText.setVisibility(View.VISIBLE);
            pbRetrieveLocation.setVisibility(View.VISIBLE);
        }else {
            tvPlaceTitle.setVisibility(View.VISIBLE);
            directions.setVisibility(View.VISIBLE);
            myLocationFab.setVisibility(View.VISIBLE);
            retrieveLocText.setVisibility(View.INVISIBLE);
            pbRetrieveLocation.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(checkLocationPermission() && isLocationEnabled()){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            myLocationFab.setVisibility(View.VISIBLE);
            if(mapState != null){
                updateMap(mapState);
            }else{
                getDeviceLocation(true);
            }
        }else {
            mMap.setMyLocationEnabled(false);
            myLocationFab.setVisibility(View.INVISIBLE);
            if(mapState != null){
                updateMap(mapState);
            }
        }

        mMap.setIndoorEnabled(false);

        Log.d(TAG, "Map Ready");
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation(final boolean showUserLocation){
        assert getActivity() != null;
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null && isLocationEnabled()){
                    lastKnownLocation = location;
                    gettingLocationStub(false);
                    if(showUserLocation){
                        updateMap(DEVICE_LOCATION);
                    }
                    Log.d(TAG, "Device Found. Location requested " + locationRequestCount + " times.");
                }else if(isLocationEnabled()){
                    getDeviceLocation(false);
                    locationRequestCount = locationRequestCount + 1;
                }
            }
        });
    }

    private boolean checkLocationPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(String permission : myPermissions){
                if (ContextCompat.checkSelfPermission(getContext(), permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.w(permission, "Permission Denied");
                    return false;
                } else {
                    Log.w(permission, "Permission Already Granted");
                }
            }
        }
        return true;
    }

    private boolean isLocationEnabled(){
        assert getContext() != null;
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void updateMap(String cameraSetting) {

        if(options == null){
            options = new MarkerOptions();
        }

        switch(cameraSetting){

            case PLACE:
                if(inflatedStub == null){
                    inflatedStub = viewStub.inflate();
                }
                if(place.getAddress() != null){
                    options.title(place.getAddress().toString());
                    tvPlaceTitle.setText(place.getAddress().toString());
                }else{
                    options.title(place.getName().toString());
                    tvPlaceTitle.setText(place.getName().toString());
                }
                mMap.clear();
                setBottomSheetVisibility(false);
                inflatedStub.setVisibility(View.VISIBLE);
                options.position(place.getLatLng());
                mMap.addMarker(options);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), DEFAULT_ZOOM));
                break;

            case ROUTE_OVERVIEW:
                setBottomSheetVisibility(true);
                mMap.addMarker(options);
                mMap.addPolyline(routeOverview);
                tvPlace.setText(travelInfo);
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 16));
                break;

            case DEVICE_LOCATION:
                LatLng deviceLatLng = new LatLng(lastKnownLocation.getLatitude(), 
                        lastKnownLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(deviceLatLng, DEFAULT_ZOOM));
                break;
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        mapState = PLACE;
        this.place = place;
        updateMap(mapState);

        if(place.getId() != null){
            Log.d(TAG, place.getId());
            destination = "destination=place_id:" + place.getId();
        }else{
            destination = "destination=" + place.getLatLng().latitude + "," + place.getLatLng().longitude;
        }

        hasPlaceChanged = true;
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(getActivity(), "Unfortunately there was an error locating your place.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, status.getStatusMessage());
    }

    public void setBottomSheetVisibility(boolean isVisible){
        if(isVisible){
            placesBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            placesBottomSheetBehavior.setHideable(false);
            autocompleteClearButton.setVisibility(View.VISIBLE);
        }else{
            placesBottomSheetBehavior.setHideable(true);
            placesBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onPreExecute() {
        tvPlace.setVisibility(View.INVISIBLE);
        bStart.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        setBottomSheetVisibility(true);
    }

    @Override
    public void onSuccessPostExecute(LatLngBounds routeBounds, PolylineOptions routeOverview, String miles, String travelTime, long meters) {
        mapState = ROUTE_OVERVIEW;
        routeOverview.color(getResources().getColor(R.color.primaryColor));
        routeOverview.width(7f);
        this.routeBounds = routeBounds;
        this.routeOverview = routeOverview;
        travelInfo = travelTime + " : " + miles;
        updateMap(mapState);

        bottomSheetAdapter.setDistance(meters);
        recyclerView.setAdapter(bottomSheetAdapter);

        Garage viewModel = ViewModelProviders.of(this).get(Garage.class);
        viewModel.getCars().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                if(cars != null){
                    bottomSheetAdapter.setCars(cars);
                }
            }
        });

        tvPlace.setVisibility(View.VISIBLE);
        bStart.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onErrorPostExecute(String status, String errorMessage) {
        assert getView() != null;

        setBottomSheetVisibility(false);

        tvPlace.setVisibility(View.VISIBLE);
        bStart.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        switch (status){
            case "ZERO_RESULTS":
                Snackbar.make(getView(), "We're sorry, no directions were found. Please select another location.", Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(getView(), "We're sorry. Something went wrong. Please select another location.", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    private void resizeMap(@NonNull View mapView, int width, int height){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        mapView.setLayoutParams(layoutParams);
        mapView.invalidate();
        mapView.requestLayout();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        View mapView = mapFragment.getView();

        if(placesBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            resizeMap(mapView, LinearLayout.LayoutParams.MATCH_PARENT,
                    mapView.getMeasuredHeight() - 156);
        }else if(placesBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
            resizeMap(mapView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }

        Log.d(TAG, "Map Resized");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permissions Granted");
                    if(mMap != null){
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        getDeviceLocation(false);
                        myLocationFab.setVisibility(View.VISIBLE);
                    }
                }else{
                    Log.d(TAG, "Permissions Denied");
                }
        }

    }
}
