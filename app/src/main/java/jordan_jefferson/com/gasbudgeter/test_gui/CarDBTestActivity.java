package jordan_jefferson.com.gasbudgeter.test_gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.network.ClientItem;
import jordan_jefferson.com.gasbudgeter.network.ClientListItems;
import jordan_jefferson.com.gasbudgeter.network.FuelEconomyClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class CarDBTestActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "CarDBTestActivity_Reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_dbtest);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final Intent replyIntent = new Intent();

                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.fueleconomy.gov/ws/rest/")
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .build();

                FuelEconomyClient fuelEconomyClient = retrofit.create(FuelEconomyClient.class);

                Call<Car> car = fuelEconomyClient.getClientVehicleData("31873");

                car.enqueue(new Callback<Car>() {
                    @Override
                    public void onResponse(Call<Car> call, Response<Car> response) {

                        Log.w("Response", "Success");
                        Car myCar = response.body();
                        replyIntent.putExtra(EXTRA_REPLY, myCar);
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Car> call, Throwable t) {
                        Log.w("Response", "Failed");
                        System.out.println(t.getMessage());
                        setResult(RESULT_CANCELED, replyIntent);
                        finish();
                    }
                });
            }
        });

    }
}