package jordan_jefferson.com.gasbudgeter.gui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import jordan_jefferson.com.gasbudgeter.R;


public class AboutFragment extends Fragment {

    private View bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private Button bAbout;
    private Button bFeatures;
    private Button bLicense;
    private Button bEpaNotices;
    private Button bPrivacy;

    private TextView title;
    private TextView description;

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

        bottomSheet = view.findViewById(R.id.about_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        ImageButton bottomSheetButton = view.findViewById(R.id.bottom_sheet_button);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);

        bAbout = view.findViewById(R.id.about);
        bFeatures = view.findViewById(R.id.features);
        bLicense = view.findViewById(R.id.licenses);
        bEpaNotices = view.findViewById(R.id.notices);
        bPrivacy = view.findViewById(R.id.privacy);

        onButtonClick(bAbout);
        onButtonClick(bFeatures);
        onButtonClick(bLicense);
        onButtonClick(bEpaNotices);
        onButtonClick(bPrivacy);

        bottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        return view;
    }

    private void onButtonClick(Button button){
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

                if(v.getId() == bAbout.getId()){
                    title.setText(bAbout.getText());
                    description.setText(getResources().getString(R.string.about_the_app));
                }else if(v.getId() == bFeatures.getId()) {
                    title.setText(bFeatures.getText());
                    description.setText(getResources().getString(R.string.upcoming_features));
                }else if(v.getId() == bLicense.getId()) {
                    title.setText(bLicense.getText());
                    description.setText(getResources().getString(R.string.licenses_agreements));
                }else if(v.getId() == bEpaNotices.getId()) {
                    title.setText(bEpaNotices.getText());
                    description.setText(getResources().getString(R.string.epa_notices));
                }else if(v.getId() == bPrivacy.getId()) {
                    title.setText(bPrivacy.getText());
                    description.setText(getResources().getString(R.string.privacy));
                }

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

}
