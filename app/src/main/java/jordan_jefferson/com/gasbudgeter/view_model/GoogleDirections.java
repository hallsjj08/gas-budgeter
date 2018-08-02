package jordan_jefferson.com.gasbudgeter.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import jordan_jefferson.com.gasbudgeter.data.GoogleDirectionsRepository;

public class GoogleDirections extends AndroidViewModel {

    private GoogleDirectionsRepository googleDirectionsRepository;

    public GoogleDirections(@NonNull Application application) {
        super(application);

        this.googleDirectionsRepository = new GoogleDirectionsRepository();

    }

    public void fetchDirections(String directionsPath){
        googleDirectionsRepository.fetchDirections(directionsPath);
    }
}
