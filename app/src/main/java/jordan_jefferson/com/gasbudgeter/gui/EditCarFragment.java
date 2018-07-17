package jordan_jefferson.com.gasbudgeter.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.data.FuelEconomyRepository;
import jordan_jefferson.com.gasbudgeter.network.ClientItem;
import jordan_jefferson.com.gasbudgeter.view_model.FuelEconomyApi;

public class EditCarFragment extends Fragment implements FuelEconomyRepository.AsyncResponseCallbacks {

    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "EDIT_CAR_FRAG";

    private ArrayList<String> params;
    private Button buttonYear;
    private Button buttonMake;
    private Button buttonModel;
    private Button buttonType;

    private FuelEconomyApi viewModel;

    public EditCarFragment() {
        // Required empty public constructor
    }

    public static EditCarFragment newInstance(ArrayList<String> params) {
        EditCarFragment fragment = new EditCarFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.params = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_car, container, false);

        buttonYear = view.findViewById(R.id.buttonYear);
        buttonMake = view.findViewById(R.id.buttonMake);
        buttonModel = view.findViewById(R.id.buttonModel);
        buttonType = view.findViewById(R.id.buttonType);

        buttonYear.setText(params.get(0));
        buttonMake.setText(params.get(1));
        buttonModel.setText(params.get(2));

        setClickListener(buttonYear, 0);
        setClickListener(buttonMake, 1);
        setClickListener(buttonModel, 2);
        setClickListener(buttonType, 3);

        viewModel = ViewModelProviders.of(this).get(FuelEconomyApi.class);

        return view;
    }

    private void setClickListener(Button button, final int paramType){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Parameter Type: " + paramType + " clicked.");
            }
        });
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostDataExecute(List<ClientItem> newClientItems) {

    }

    @Override
    public void onPostCarExecute(Car newCar) {

    }
}
