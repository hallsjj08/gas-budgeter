package jordan_jefferson.com.gasbudgeter.gui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import jordan_jefferson.com.gasbudgeter.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";
    private Fragment garageFragment;
    private Fragment tripMapsFragment;
    private Fragment aboutFragment;
    private Fragment currentFragment;
    private BottomNavigationView navigation;

    private static final String MAP_FRAG_TAG = "Trip_Maps_Fragment";
    private static final String GARAGE_FRAG_TAG = "Garage_Fragment";
    private static final String ABOUT_FRAG_TAG = "About_Fragment";
    private static final String SELECTED_ITEM_KEY = "Selected_Item";

    private int mapContainer;
    private int garageContainer;
    private int aboutContainer;

    private String[] myPermissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Activity Created");

        mapContainer = R.id.map_container;
        garageContainer = R.id.garage_container;
        aboutContainer = R.id.about_container;

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(String permission : myPermissions){
                if (ContextCompat.checkSelfPermission(getApplicationContext(), permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.w(permission, "Requesting Permission");
                    ActivityCompat.requestPermissions(this,
                            myPermissions, 1);
                } else {
                    Log.w(permission, "Permission Already Granted");
                }
            }
        }else{
            int currentSelection;
            if(savedInstanceState == null){
                tripMapsFragment = TripMaps.newInstance();
                garageFragment = GarageFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .add(garageContainer, garageFragment, GARAGE_FRAG_TAG)
                        .add(mapContainer, tripMapsFragment, MAP_FRAG_TAG)
                        .commit();
                currentFragment = garageFragment;
                swapFragments(tripMapsFragment);
            }else{
                currentSelection = savedInstanceState.getInt(SELECTED_ITEM_KEY);
                tripMapsFragment = getSupportFragmentManager().findFragmentByTag(MAP_FRAG_TAG);
                garageFragment = getSupportFragmentManager().findFragmentByTag(GARAGE_FRAG_TAG);
                navigation.setSelectedItemId(currentSelection);
            }

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_destination:
                    if(tripMapsFragment == null){
                        tripMapsFragment = TripMaps.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(mapContainer, tripMapsFragment, MAP_FRAG_TAG)
                                .commit();
                        currentFragment = tripMapsFragment;
                    }else{
                        swapFragments(tripMapsFragment);
                    }
                    return true;
                case R.id.navigation_garage:
                    if(garageFragment == null){
                        garageFragment = GarageFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(garageContainer, garageFragment, GARAGE_FRAG_TAG)
                                .commit();
                        currentFragment = garageFragment;
                    }else{
                        swapFragments(garageFragment);
                    }
                    return true;
                case R.id.navigation_about:
                    if(aboutFragment == null){
                        aboutFragment = AboutFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(aboutContainer, aboutFragment, ABOUT_FRAG_TAG)
                                .commit();
                        currentFragment = aboutFragment;
                    }else{
                        swapFragments(aboutFragment);
                    }
                    return true;
            }
            return false;
        }
    };

    private void swapFragments(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .show(fragment)
                .commit();
        currentFragment = fragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_KEY, navigation.getSelectedItemId());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.d(TAG, "Landscape");
        }else {
            Log.d(TAG, "Portrait");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permissions Granted");
                        tripMapsFragment = TripMaps.newInstance();
                        garageFragment = GarageFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(garageContainer, garageFragment, GARAGE_FRAG_TAG)
                                .add(mapContainer, tripMapsFragment, MAP_FRAG_TAG)
                                .commit();
                        currentFragment = garageFragment;
                        swapFragments(tripMapsFragment);
                }else{
                    Log.d(TAG, "Permissions Denied");
                    finish();
                }
        }

    }
}
