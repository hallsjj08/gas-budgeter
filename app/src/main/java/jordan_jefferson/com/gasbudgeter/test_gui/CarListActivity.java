package jordan_jefferson.com.gasbudgeter.test_gui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.network.ClientItem;
import jordan_jefferson.com.gasbudgeter.network.ClientListItems;
import jordan_jefferson.com.gasbudgeter.network.FuelEconomyClient;
import jordan_jefferson.com.gasbudgeter.view_model.Garage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class CarListActivity extends AppCompatActivity {

    private Garage viewModel;

    public static final int NEW_CAR_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CarListAdapter carListAdapter = new CarListAdapter(getApplicationContext());
        recyclerView.setAdapter(carListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(Garage.class);

        viewModel.getCars().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                carListAdapter.setCars(cars);
            }
        });

        fetchItems();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarListActivity.this, CarDBTestActivity.class);
                startActivityForResult(intent, NEW_CAR_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CAR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Car car = (Car) data.getSerializableExtra(CarDBTestActivity.EXTRA_REPLY);
            viewModel.insert(car);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Data Not Found.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void fetchItems(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.fueleconomy.gov/ws/rest/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FuelEconomyClient fuelEconomyClient = retrofit.create(FuelEconomyClient.class);

        Call<ClientListItems> years = fuelEconomyClient.getClientVehicleTypeId("2012", "Honda", "Civic");

        years.enqueue(new Callback<ClientListItems>() {
            @Override
            public void onResponse(Call<ClientListItems> call, Response<ClientListItems> response) {
                System.out.println("Success");
                List<ClientItem> myItems = response.body().clientItemList;

                for(int i = 0; i < myItems.size(); i++){
                    System.out.println(myItems.get(i).getText());
                }
            }

            @Override
            public void onFailure(Call<ClientListItems> call, Throwable t) {
                System.out.println("Failed");
            }
        });
    }

}
