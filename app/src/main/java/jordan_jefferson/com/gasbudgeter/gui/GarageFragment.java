package jordan_jefferson.com.gasbudgeter.gui;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import java.util.ArrayList;
import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.data_adapters.CarListAdapter;
import jordan_jefferson.com.gasbudgeter.interface_files.OnClickCarData;
import jordan_jefferson.com.gasbudgeter.view_model.Garage;

public class GarageFragment extends Fragment implements OnClickCarData {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "GARAGE_FRAGMENT";
    public static final String EDIT_CAR_EXTRA = "Edit Car Key";
    public static final String VIEW_CAR_EXTRA = "View Car Key";

    private CarListAdapter carListAdapter;
    private Garage viewModel;
    private ViewStub newCarStub;
    private View inflatedNewCarStub;

    private FragmentTransaction fragmentTransaction;

    public GarageFragment() {
        // Required empty public constructor
        Log.d(TAG, "Constructed");
    }

    public static GarageFragment newInstance() {
        GarageFragment fragment = new GarageFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           Log.d(TAG, "Has Saved State");
        }
        Log.d(TAG, "Created");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.garage_fragment, container, false);

        newCarStub = view.findViewById(R.id.new_car_stub);

        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add Car Button Clicked.");
                Intent intent = new Intent(getActivity(), NewCarActivity.class);
                startActivityForResult(intent, 9001);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.dataRecyclerView);
        carListAdapter = new CarListAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = ViewModelProviders.of(this).get(Garage.class);

        viewModel.getCars().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                assert cars != null;
                if(cars.isEmpty()){
                    Log.d(TAG, "Garage is empty.");
                    if(inflatedNewCarStub == null){
                        inflatedNewCarStub = newCarStub.inflate();
                    }
                    inflatedNewCarStub.setVisibility(View.VISIBLE);
                }else{
                    if(inflatedNewCarStub != null){
                        inflatedNewCarStub.setVisibility(View.INVISIBLE);
                    }
                }

                carListAdapter.setCars(cars);

            }
        });

        Log.d(TAG, "View Created");

        recyclerView.setAdapter(carListAdapter);

        return view;
    }

    @Override
    public void onClickEditCar(Car car) {
        Log.d(TAG, "Edit Car Button Clicked.");
        Intent intent = new Intent(getActivity(), EditCarActivity.class);
        ArrayList<String> carParams = new ArrayList<>();
        carParams.add(car.getYear() + "");
        carParams.add(car.getMake());
        carParams.add(car.getModel());
//        carParams.add(car.getType);
        Bundle args = new Bundle();
        args.putSerializable(EDIT_CAR_EXTRA, car);
        intent.putExtra(EDIT_CAR_EXTRA, args);
        startActivityForResult(intent, 9002);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 9001:
                if(resultCode == Activity.RESULT_OK){
                    Car car = (Car) data.getSerializableExtra(NewCarActivity.NEW_CAR_KEY);
                    Log.d(TAG, car.getMake());
                    viewModel.insert(car);
                }
                break;
            case 9002:
                switch (resultCode){
                    case EditCarFragment.RESULT_DELETE:
                        viewModel.delete((Car) data.getSerializableExtra(EDIT_CAR_EXTRA));
                        break;
                }
                break;
        }
    }

    @Override
    public void onClickViewCarSpecs(Car car) {
        Log.d(TAG, "View Specs Button Clicked");
    }
}
