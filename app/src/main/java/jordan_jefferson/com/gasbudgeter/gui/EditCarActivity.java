package jordan_jefferson.com.gasbudgeter.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import jordan_jefferson.com.gasbudgeter.R;

public class EditCarActivity extends AppCompatActivity {

    private ArrayList<String> carParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        Bundle bundle = getIntent().getBundleExtra(GarageFragment.EDIT_CAR_EXTRA);
        carParams = bundle.getStringArrayList(GarageFragment.EDIT_CAR_EXTRA);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.edit_car_fragment_container, EditCarFragment.newInstance(carParams))
                .commit();
    }
}
