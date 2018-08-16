package jordan_jefferson.com.gasbudgeter.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import jordan_jefferson.com.gasbudgeter.data.FuelEconomyRepository;

public class FuelEconomyApi extends AndroidViewModel {

    private static final String TAG = "FUEL_ECO_VIEWMODEL";
    private FuelEconomyRepository fuelEconomyRepository;

    public FuelEconomyApi(@NonNull Application application) {
        super(application);

        fuelEconomyRepository = new FuelEconomyRepository();

    }

    public void fetchNewApiData(String selectedItem) { 
//        Log.d(TAG, "Fetching new data");
        fuelEconomyRepository.fetchApiListData(selectedItem); }

    public void fetchNewApiCarData(String vehicleId, String vehicleType) { fuelEconomyRepository.fetchApiCarData(vehicleId, vehicleType); }

    public void cancelAllApiTransactions() { fuelEconomyRepository.cancelDataFetch(); }

    public void setDataType(ArrayList<String> params, int dataType) {
        fuelEconomyRepository.setDataType(params, dataType);
    }
}
