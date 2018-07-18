package jordan_jefferson.com.gasbudgeter.gui;


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

        FloatingActionButton fab = view.findViewById(R.id.fab_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getActivity() != null;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, NewCarFragment.newInstance(GarageFragment.this))
                        .addToBackStack(null)
                        .commit();
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
                    assert getActivity() != null;
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, NewCarFragment.newInstance(GarageFragment.this));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else{
                    carListAdapter.setCars(cars);
                }

            }
        });

        Log.d(TAG, "View Created");

        recyclerView.setAdapter(carListAdapter);

        return view;
    }

    @Override
    public void onCarResultOk(Car car) {
        viewModel.insert(car);
        Log.d(TAG, "Car added to garage");
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
        args.putStringArrayList(EDIT_CAR_EXTRA, carParams);
        intent.putExtra(EDIT_CAR_EXTRA, args);
        startActivityForResult(intent, 9002);
    }

    @Override
    public void onClickViewCarSpecs(Car car) {
        Log.d(TAG, "View Specs Button Clicked");
    }
}
