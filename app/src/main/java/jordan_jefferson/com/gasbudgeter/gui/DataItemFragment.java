package jordan_jefferson.com.gasbudgeter.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data_adapters.FuelEconomyDataListAdapter;
import jordan_jefferson.com.gasbudgeter.interface_files.RecyclerViewItemClickListener;
import jordan_jefferson.com.gasbudgeter.network.ClientItem;
import jordan_jefferson.com.gasbudgeter.view_model.FuelEconomyApi;

public class DataItemFragment extends Fragment implements RecyclerViewItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "DATA FRAGMENT";
    private String fragmentTag;
    private List<ClientItem> clientItems;

    FuelEconomyDataListAdapter fuelEconomyDataListAdapter;

    private FuelEconomyApi viewModel;

    public DataItemFragment() {
        // Required empty public constructor
    }

    public static DataItemFragment newInstance(String fragmentTag, List<ClientItem> clientItems) {
        DataItemFragment fragment = new DataItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) clientItems);
        args.putString(ARG_PARAM2, fragmentTag);
        fragment.setArguments(args);
        Log.d(TAG, "New Instance");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //noinspection unchecked
            this.clientItems = (List<ClientItem>) getArguments().getSerializable(ARG_PARAM1);
            this.fragmentTag = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        RecyclerView recyclerView = view.findViewById(R.id.dataRecyclerView);
        
        assert getContext() != null;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        fuelEconomyDataListAdapter = new FuelEconomyDataListAdapter(clientItems, getContext(), this);
        recyclerView.setAdapter(fuelEconomyDataListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        assert getActivity() != null;
        viewModel = ViewModelProviders.of(getActivity()).get(FuelEconomyApi.class);

    }

    @Override
    public void recyclerViewItemClicked(View v, int position) {

        assert getActivity() != null;
        switch (fragmentTag){
            case "New Car":
                Log.d("STACK_COUNT", "" + getActivity().getSupportFragmentManager().getBackStackEntryCount());
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() == 4){
                    String vehicleType = clientItems.get(position).getText();
                    String vehicleId = clientItems.get(position).getValue();
                    viewModel.fetchNewApiCarData(vehicleId, vehicleType);
                }else{
                    String selectedItem = clientItems.get(position).getText();
                    viewModel.fetchNewApiData(selectedItem);
                }
                break;
            case "Edit Car":
                getActivity().getSupportFragmentManager().popBackStack();
                Log.d(TAG, "Stack Popped");
                break;
        }

    }


}
