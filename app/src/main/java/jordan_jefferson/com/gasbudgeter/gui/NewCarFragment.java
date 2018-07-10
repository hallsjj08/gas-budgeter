package jordan_jefferson.com.gasbudgeter.gui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewCarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCarFragment extends Fragment {

    private static final String TAG = "NEW_CAR_FRAG";


    public interface CarResult{
        void onCarResultOk(Car car);
    }

    public static CarResult carResult = null;

    public NewCarFragment() {
        // Required empty public constructor
    }

    public static NewCarFragment newInstance() {
        NewCarFragment fragment = new NewCarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d(TAG, "Has Saved State");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_car, container, false);

        Button addCar = view.findViewById(R.id.bAddCar);

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NEW_CAR_FRAGMENT", "Add Car Button Clicked.");
                Intent intent = new Intent(getActivity(), NewCarActivity.class);
                startActivityForResult(intent, 9001);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 9001:
                if(resultCode == Activity.RESULT_OK){
                    Car car = (Car) data.getSerializableExtra(NewCarActivity.NEW_CAR_KEY);
                    Log.d(TAG, car.getMake());
                    carResult.onCarResultOk(car);
                    assert getActivity() != null;
                    getActivity().getSupportFragmentManager().popBackStack();
                }
        }
    }
}
