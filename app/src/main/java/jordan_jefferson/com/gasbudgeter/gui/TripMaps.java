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
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
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
import jordan_jefferson.com.gasbudgeter.data_adapters.BottomSheetAdapter;
import jordan_jefferson.com.gasbudgeter.interface_files.AsyncResponse;
import jordan_jefferson.com.gasbudgeter.network.GoogleDirectionsRetrofitBuilder;
import jordan_jefferson.com.gasbudgeter.view_model.Garage;


public class TripMaps extends Fragment implements OnMapReadyCallback, PlaceSelectionListener,
        AsyncResponse {

    private static final String TAG = "TripMapsFragment";
    private static final float DEFAULT_ZOOM = 15f;

    private int fragmentHeight;

    private static final String[] TEST_URL = {"https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Decatur,IL&key=AIzaSyBztmrBqLEv5fO-NjmNXg66ztVK_Si99Qw"};
    private static final String API_KEY = "key=AIzaSyBztmrBqLEv5fO-NjmNXg66ztVK_Si99Qw";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    private String origin;
    private String destination;
    private String[] directionsUrl = new String[1];

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private Garage viewModel;
    private RecyclerView recyclerView;
    private BottomSheetAdapter bottomSheetAdapter;
    private Fragment mapFragment;

    private ConstraintLayout placeSelectedBottomSheet;
    private BottomSheetBehavior placesBottomSheetBehavior;

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
            Log.d(TAG, "Has saved state.");
        }
        Log.d(TAG, "Created");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trip_maps, container, false);

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

        placeSelectedBottomSheet = view.findViewById(R.id.place_selected_bottom_sheet);
        placesBottomSheetBehavior = BottomSheetBehavior.from(placeSelectedBottomSheet);
        placesBottomSheetBehavior.setHideable(true);
        placesBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.tripMap);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        final SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager()
                .findFragmentById(R.id.trip_place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(this);

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

        tvPlace = view.findViewById(R.id.place_name);
        Button bDirections = view.findViewById(R.id.directions);

        bDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin = "origin=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                directionsUrl[0] = BASE_URL + origin + "&" + destination + "&" + API_KEY;
                Log.d(TAG, directionsUrl[0]);
                GoogleDirectionsRetrofitBuilder googleDirectionsRetrofitBuilder = new GoogleDirectionsRetrofitBuilder();
                googleDirectionsRetrofitBuilder.delegate = TripMaps.this;
                googleDirectionsRetrofitBuilder.execute(directionsUrl);
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
        String placeName = place.getName().toString() + place.getAddress().toString();
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
        mapFragment = getChildFragmentManager().findFragmentById(R.id.tripMap);
        resizeMap(mapFragment, LinearLayout.LayoutParams.MATCH_PARENT, fragmentHeight - 156);
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(getActivity(), "Unfortunately there was an error locating your place.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, status.getStatusMessage());
    }

    @Override
    public void onDirectionResultsUpdate(LatLngBounds routeBounds, PolylineOptions routeOverview,
                                         String miles, String travelTime, long meters) {

        bottomSheetAdapter.setDistance(meters);
        recyclerView.setAdapter(bottomSheetAdapter);

        viewModel = ViewModelProviders.of(this).get(Garage.class);
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

    }

    private void resizeMap(Fragment fragment, int width, int height){
        if(fragment != null){
            View view = fragment.getView();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            view.setLayoutParams(layoutParams);
            view.invalidate();
            view.requestLayout();
        }
    }
}
