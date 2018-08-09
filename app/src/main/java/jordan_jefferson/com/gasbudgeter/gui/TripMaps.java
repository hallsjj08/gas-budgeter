package jordan_jefferson.com.gasbudgeter.gui;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
        GoogleDirectionsRepository.DirectionsAsyncResponseCallbacks, ViewTreeObserver.OnGlobalLayoutListener {

    private static final float DEFAULT_ZOOM = 15f;
    private int fragmentHeight;

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
    private ViewTreeObserver vto;

    private BottomSheetBehavior placesBottomSheetBehavior;
    private TextView tvPlace;
    private Button bDirections;
    private ProgressBar progressBar;
    private ImageButton autocompleteClearButton;

    private Place place;
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


        tvPlace = view.findViewById(R.id.place_name);
        bDirections = view.findViewById(R.id.directions);
        progressBar = view.findViewById(R.id.pbDirectionsLoading);

        vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);

        recyclerView = view.findViewById(R.id.bottom_sheet_recyclerView);
        bottomSheetAdapter = new BottomSheetAdapter(getContext());

        ConstraintLayout placeSelectedBottomSheet = view.findViewById(R.id.place_selected_bottom_sheet);
        placesBottomSheetBehavior = BottomSheetBehavior.from(placeSelectedBottomSheet);
        if(mapState == null){
            setBottomSheetVisibility(false);
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.tripMap);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

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
                            clearMap();
                            Log.d(TAG, "Map Cleared, Place Cleared.");
                        }
                        if(placesBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED
                                || placesBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                            setBottomSheetVisibility(false);
                        }
                    }
                });

        bDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin = "origin=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                directionsUrl = FORMAT + origin + "&" + destination + "&" + API_KEY;
                Log.d(TAG, directionsUrl);
                directionsViewModel = ViewModelProviders.of(TripMaps.this).get(GoogleDirections.class);
                directionsViewModel.fetchDirections(directionsUrl);
            }
        });

        FloatingActionButton myLocationFab = view.findViewById(R.id.my_location_fab);
        myLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
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
        mMap.setIndoorEnabled(false);

        if(mapState != null){
            updateMap(mapState);
        }else{
            getDeviceLocation();
        }


        Log.d(TAG, "Map Ready");
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation(){
        assert getActivity() != null;
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    lastKnownLocation = location;
                    mapState = DEVICE_LOCATION;
                    updateMap(mapState);
                    Log.d(TAG, "Device Found.");
                }
            }
        });
    }

    private void updateMap(String cameraSetting) {

        if(options == null){
            options = new MarkerOptions();
        }

        switch(cameraSetting){

            case PLACE:
                setBottomSheetVisibility(true);
                if(place.getAddress() != null){
                    options.title(place.getAddress().toString());
                    tvPlace.setText(place.getAddress().toString());
                }else{
                    options.title(place.getName().toString());
                    tvPlace.setText(place.getName().toString());
                }
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
            resizeMap(mapFragment, LinearLayout.LayoutParams.MATCH_PARENT, fragmentHeight - 156);
        }else{
            placesBottomSheetBehavior.setHideable(true);
            placesBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            resizeMap(mapFragment, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
    }

    private void resizeMap(Fragment fragment, int width, int height){
        if(fragment != null){
            View view = fragment.getView();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            if(view != null){
                view.setLayoutParams(layoutParams);
                view.invalidate();
                view.requestLayout();
                Log.d(TAG, "Map resized");
            }else{
                Log.d(TAG, "View is null");
            }
        }
    }

    @Override
    public void onPreExecute() {
        tvPlace.setVisibility(View.INVISIBLE);
        bDirections.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
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
        bDirections.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void clearMap(){
        mMap.clear();
        this.options = null;
        this.routeOverview = null;
        this.routeBounds = null;
    }

    @Override
    public void onErrorPostExecute(String status, String errorMessage) {
        assert getView() != null;

        setBottomSheetVisibility(false);

        tvPlace.setVisibility(View.VISIBLE);
        bDirections.setVisibility(View.VISIBLE);
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

    /**
     * Callback method to be invoked when the global layout state or the visibility of views
     * within the view tree changes
     */
    @Override
    public void onGlobalLayout() {
        if(getView() != null){
            fragmentHeight = getView().getMeasuredHeight();
            Log.d(TAG, "Measure Height: " + fragmentHeight);
        }
    }
}
