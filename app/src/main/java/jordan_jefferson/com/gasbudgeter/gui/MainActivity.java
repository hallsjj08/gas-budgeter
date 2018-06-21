package jordan_jefferson.com.gasbudgeter.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import jordan_jefferson.com.gasbudgeter.R;

public class MainActivity extends AppCompatActivity {

    private Fragment garageFragment;
    private Fragment tripMapsFragment;
    private Fragment aboutFragment;
    private BottomNavigationView navigation;

    private int currentSelection;

    private static final String MAP_FRAG_TAG = "Trip_Maps_Fragment";
    private static final String GARAGE_FRAG_TAG = "Garage_Fragment";
    private static final String ABOUT_FRAG_TAG = "About_Fragment";
    private static final String SELECTED_ITEM_KEY = "Selected_Item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState == null){
            currentSelection = R.id.navigation_destination;
            tripMapsFragment = TripMaps.newInstance();
            swapFragments(tripMapsFragment, MAP_FRAG_TAG);

        }else{
            currentSelection = savedInstanceState.getInt(SELECTED_ITEM_KEY);
            tripMapsFragment = getSupportFragmentManager().findFragmentByTag(MAP_FRAG_TAG);
            garageFragment = getSupportFragmentManager().findFragmentByTag(GARAGE_FRAG_TAG);
            navigation.setSelectedItemId(currentSelection);
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
                    }
                    swapFragments(tripMapsFragment, MAP_FRAG_TAG);
                    tripMapsFragment = getSupportFragmentManager().findFragmentByTag(MAP_FRAG_TAG);
                    return true;
                case R.id.navigation_garage:
                    if(garageFragment == null){
                        garageFragment = GarageFragment.newInstance();
                    }
                    swapFragments(garageFragment, GARAGE_FRAG_TAG);
                    garageFragment = getSupportFragmentManager().findFragmentByTag(GARAGE_FRAG_TAG);
                    return true;
                case R.id.navigation_about:

                    return true;
            }
            return false;
        }
    };

    private void swapFragments(Fragment fragment, String tag){
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_KEY, navigation.getSelectedItemId());
    }
}
