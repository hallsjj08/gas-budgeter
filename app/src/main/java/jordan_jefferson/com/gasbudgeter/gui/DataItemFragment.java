package jordan_jefferson.com.gasbudgeter.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.network.ClientItem;

public class DataItemFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private List<ClientItem> clientItems;


    public DataItemFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DataItemFragment newInstance(List<ClientItem> clientItems) {
        DataItemFragment fragment = new DataItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) clientItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.clientItems = (List<ClientItem>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.dataRecyclerView);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        FuelEconomyDataListAdapter fuelEconomyDataListAdapter = new FuelEconomyDataListAdapter(clientItems, getContext());
        recyclerView.setAdapter(fuelEconomyDataListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
