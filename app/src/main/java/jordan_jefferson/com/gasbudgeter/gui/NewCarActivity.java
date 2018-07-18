package jordan_jefferson.com.gasbudgeter.gui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.data.FuelEconomyRepository;
import jordan_jefferson.com.gasbudgeter.network.ClientItem;
import jordan_jefferson.com.gasbudgeter.view_model.FuelEconomyApi;

public class NewCarActivity extends AppCompatActivity implements FuelEconomyRepository.AsyncResponseCallbacks {

    private Fragment progressFragment;
    private Fragment dataItemFragment;
    private FuelEconomyApi viewModel;
    private String dataType = "YEARS";
    private static final String DATA_TYPE_TAG = "Data Type";
    public static final String NEW_CAR_KEY = "New Car";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);
        Toolbar toolbar = findViewById(R.id.toolbar);

        FuelEconomyRepository.callbacks = this;

        if(progressFragment == null){
            progressFragment = ProgressFragment.newInstance();
        }

        if(savedInstanceState != null){
            dataType = savedInstanceState.getString(DATA_TYPE_TAG);
            dataItemFragment = getSupportFragmentManager().findFragmentByTag(dataType);
        }

        if(dataItemFragment == null){
            viewModel = ViewModelProviders.of(this).get(FuelEconomyApi.class);
            viewModel.fetchNewApiData("");
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.new_car_items_container, dataItemFragment, dataType)
                    .addToBackStack(dataType)
                    .commit();
        }

        toolbar.setTitle(dataType);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DATA_TYPE_TAG, dataType);
    }

    @Override
    public void onPreExecute() {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_car_items_container, progressFragment)
                .commit();
    }

    @Override
    public void onPostDataExecute(List<ClientItem> clientItems) {
        getSupportFragmentManager().beginTransaction()
                .remove(progressFragment)
                .commitNow();

        dataItemFragment = DataItemFragment.newInstance("New Car", clientItems);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.new_car_items_container, dataItemFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPostCarExecute(Car newCar) {
        Intent intent = new Intent();
        intent.putExtra(NEW_CAR_KEY, newCar);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        viewModel.cancelAllApiTransactions();
        if(getSupportFragmentManager().getBackStackEntryCount() == 1){
            setResult(Activity.RESULT_CANCELED);
            finish();
        }else{
            getSupportFragmentManager().popBackStack();
        }

    }
}
