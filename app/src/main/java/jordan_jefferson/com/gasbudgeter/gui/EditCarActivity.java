package jordan_jefferson.com.gasbudgeter.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;

public class EditCarActivity extends AppCompatActivity {

    private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        Bundle bundle = getIntent().getBundleExtra(GarageFragment.EDIT_CAR_EXTRA);
        car = (Car) bundle.getSerializable(GarageFragment.EDIT_CAR_EXTRA);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.edit_car_fragment_container, EditCarFragment.newInstance(car))
                .commit();
    }
}
