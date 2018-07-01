package jordan_jefferson.com.gasbudgeter.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import jordan_jefferson.com.gasbudgeter.data.FuelEconomyRepository;

public class FuelEconomyApi extends AndroidViewModel {

    private FuelEconomyRepository fuelEconomyRepository;

    public FuelEconomyApi(@NonNull Application application) {
        super(application);

        fuelEconomyRepository = new FuelEconomyRepository();

    }

    public void fetchNewApiData(String selectedItem) { fuelEconomyRepository.fetchApiListData(selectedItem); }

    public void fetchNewApiCarData(String vehicleId) { fuelEconomyRepository.fetchApiCarData(vehicleId); }

    public void cancelAllApiTransactions() { fuelEconomyRepository.cancelDataFetch(); }

}
