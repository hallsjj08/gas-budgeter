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

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.view_model.Garage;

public class CarListActivity extends AppCompatActivity {

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

        Garage viewModel = ViewModelProviders.of(this).get(Garage.class);

        viewModel.getCars().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                carListAdapter.setCars(cars);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarListActivity.this, CarDBTestActivity.class);
                startActivityForResult(intent, NEW_CAR_ACTIVITY_REQUEST_CODE);
            }
        });
    }

}
