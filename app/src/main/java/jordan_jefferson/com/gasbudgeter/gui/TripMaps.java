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

    private static final String TAG = "TripMapsFragment";
    private static final float DEFAULT_ZOOM = 15f;

    private int fragmentHeight;

    //private static final String[] TEST_URL = {"https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Decatur,IL&key=AIzaSyBztmrBqLEv5fO-NjmNXg66ztVK_Si99Qw"};
    private static final String API_KEY = "key=AIzaSyBztmrBqLEv5fO-NjmNXg66ztVK_Si99Qw";
    private static final String FORMAT = "json?";
    private String origin;
    private String destination;
    private String directionsUrl;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private RecyclerView recyclerView;
    private BottomSheetAdapter bottomSheetAdapter;
    private SupportMapFragment mapFragment;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private GoogleDirections directionsViewModel;

    private BottomSheetBehavior placesBottomSheetBehavior;
    private TextView tvPlace;
    private Button bDirections;
    private ProgressBar progressBar;

    public TripMaps() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    public static TripMaps newInstance() {
//        TripMaps fragment = new TripMaps();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
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
        ViewTreeObserver vto = view.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(getView() != null){
                    fragmentHeight = getView().getMeasuredHeight();
                    Log.d(TAG, "Measure Height: " + fragmentHeight);
                }
            }
        });

        recyclerView = view.findViewById(R.id.bottom_sheet_recyclerView);
        bottomSheetAdapter = new BottomSheetAdapter(getContext());

        ConstraintLayout placeSelectedBottomSheet = view.findViewById(R.id.place_selected_bottom_sheet);
        placesBottomSheetBehavior = BottomSheetBehavior.from(placeSelectedBottomSheet);
        placesBottomSheetBehavior.setHideable(true);
        placesBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.tripMap);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager()
                .findFragmentById(R.id.trip_place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(this);

        assert autocompleteFragment.getView() != null;
        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autocompleteFragment.setText("");
                        if(mMap != null){
                            mMap.clear();
                        }
                        if(placesBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED
                                || placesBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                            placesBottomSheetBehavior.setHideable(true);
                            placesBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            resizeMap(mapFragment, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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
        getDeviceLocation();
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
    public void onPlaceSelected(Place place) {
        String placeName;

        if(place.getAddress() != null){
            placeName = place.getAddress().toString();
        }else {
            placeName = place.getName().toString();
        }

        tvPlace.setText(placeName);
        updateCamera(place.getLatLng(), DEFAULT_ZOOM, placeName);
        if(place.getId() != null){
            Log.d(TAG, place.getId());
            destination = "destination=place_id:" + place.getId();
        }else{
            destination = "destination=" + place.getLatLng().latitude + "," + place.getLatLng().longitude;
        }

        placesBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        placesBottomSheetBehavior.setHideable(false);
        resizeMap(mapFragment, LinearLayout.LayoutParams.MATCH_PARENT, fragmentHeight - 156);
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(getActivity(), "Unfortunately there was an error locating your place.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, status.getStatusMessage());
    }

    private void resizeMap(Fragment fragment, int width, int height){
        if(fragment != null){
            View view = fragment.getView();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            if(view != null){
                view.setLayoutParams(layoutParams);
                view.invalidate();
                view.requestLayout();
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

        mMap.addPolyline(routeOverview);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 16));

        String travelInfo = travelTime + " : " + miles;
        tvPlace.setText(travelInfo);

        tvPlace.setVisibility(View.VISIBLE);
        bDirections.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onErrorPostExecute(String status, String errorMessage) {
//        Toast.makeText(getActivity(), status + ", " + errorMessage, Toast.LENGTH_LONG).show();
        assert getView() != null;

        placesBottomSheetBehavior.setHideable(true);
        placesBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        resizeMap(mapFragment, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

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
}
