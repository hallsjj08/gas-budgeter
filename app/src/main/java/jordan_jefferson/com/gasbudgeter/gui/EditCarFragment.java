package jordan_jefferson.com.gasbudgeter.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.view_model.FuelEconomyApi;

public class EditCarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "EDIT_CAR_FRAG";
    public static final int RESULT_DELETE = 100;

    private ProgressFragment progressFragment;

    private ArrayList<String> params = new ArrayList<>();

    private FuelEconomyApi viewModel;

    private Car car;

    public EditCarFragment() {
        // Required empty public constructor
    }

    public static EditCarFragment newInstance(Car car) {
        EditCarFragment fragment = new EditCarFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, car);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.car = (Car) getArguments().getSerializable(ARG_PARAM1);
            params.add(car.getYear() + "");
            params.add(car.getMake());
            params.add(car.getModel());
            params.add(car.getVehicleType());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getActivity() != null;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_car, container, false);

        if(progressFragment == null){
            progressFragment = ProgressFragment.newInstance();
        }


        TextView buttonYear = view.findViewById(R.id.buttonYear);
        TextView buttonMake = view.findViewById(R.id.buttonMake);
        TextView buttonModel = view.findViewById(R.id.buttonModel);
        TextView buttonType = view.findViewById(R.id.buttonType);
        Button removeVehicle = view.findViewById(R.id.remove_car_button);

        buttonYear.setText(params.get(0));
        buttonMake.setText(params.get(1));
        buttonModel.setText(params.get(2));
        buttonType.setText(params.get(3));

        removeVehicle.setPaintFlags(removeVehicle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        removeVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(GarageFragment.EDIT_CAR_EXTRA, car);
                getActivity().setResult(RESULT_DELETE, intent);
                getActivity().finish();
            }
        });

        viewModel = ViewModelProviders.of(this).get(FuelEconomyApi.class);

        return view;
    }
}
