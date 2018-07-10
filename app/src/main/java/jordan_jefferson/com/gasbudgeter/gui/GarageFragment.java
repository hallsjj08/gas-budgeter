package jordan_jefferson.com.gasbudgeter.gui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.data_adapters.CarListAdapter;
import jordan_jefferson.com.gasbudgeter.view_model.Garage;

public class GarageFragment extends Fragment implements NewCarFragment.CarResult {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "GARAGE_FRAGMENT";

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

        NewCarFragment.carResult = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.dataRecyclerView);
        carListAdapter = new CarListAdapter(getContext());
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
                    fragmentTransaction.replace(R.id.fragment_container, NewCarFragment.newInstance());
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
}
