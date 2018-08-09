package jordan_jefferson.com.gasbudgeter.gui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.AboutContent;
import jordan_jefferson.com.gasbudgeter.data_adapters.AboutContentAdapter;


public class AboutFragment extends Fragment {

    private ArrayList<AboutContent> aboutContents;

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about, container, false);

        this.aboutContents = new ArrayList<>();

        for(int i = 0; i<10; i++){
            aboutContents.add(new AboutContent("title " + i, "description " + i));
        }

        RecyclerView recyclerView = view.findViewById(R.id.about_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AboutContentAdapter aboutContentAdapter = new AboutContentAdapter(aboutContents, getContext());
        recyclerView.setAdapter(aboutContentAdapter);

        return view;
    }

}
