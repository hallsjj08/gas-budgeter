package jordan_jefferson.com.gasbudgeter.gui;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.network.ClientItem;
import jordan_jefferson.com.gasbudgeter.network.FuelEconomyRetrofitBuilder;

public class NewCar extends AppCompatActivity {

    private FuelEconomyRetrofitBuilder fuelEconomyRetrofitBuilder;
    private Fragment progressFragment;
    private Fragment dataItemYears;
    private String itemSelected = "";
    private List<ClientItem> clientItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Year");
        setSupportActionBar(toolbar);

        if(progressFragment == null){
            progressFragment = ProgressFragment.newInstance();
        }

        fuelEconomyRetrofitBuilder = new FuelEconomyRetrofitBuilder();
        AsyncData newCarData = new AsyncData();
        newCarData.execute(itemSelected);

    }

    private class AsyncData extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.new_car_items_container, progressFragment)
                    .commit();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            clientItems = fuelEconomyRetrofitBuilder.fetchData(itemSelected);
            Log.d("CLIENT_ITEMS", clientItems.toString());

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Log.d("CLIENT_ITEMS", "Success");
                dataItemYears = DataItemFragment.newInstance(clientItems);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.new_car_items_container, dataItemYears, "Years")
                        .commit();
            }else{
                fuelEconomyRetrofitBuilder.cancelTransactions();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            fuelEconomyRetrofitBuilder.cancelTransactions();
        }
    }

}
