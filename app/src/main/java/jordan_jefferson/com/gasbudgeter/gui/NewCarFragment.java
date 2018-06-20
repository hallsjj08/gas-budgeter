package jordan_jefferson.com.gasbudgeter.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import jordan_jefferson.com.gasbudgeter.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewCarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public NewCarFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_car, container, false);

        Button addCar = view.findViewById(R.id.bAddCar);

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NEW_CAR_FRAGMENT", "Add Car Button Clicked.");
            }
        });

        return view;
    }

}
