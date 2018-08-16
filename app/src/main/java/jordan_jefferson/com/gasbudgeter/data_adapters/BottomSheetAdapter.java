package jordan_jefferson.com.gasbudgeter.data_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder> {

    private long distanceMeters;
    private List<Car> cars;
    private LayoutInflater layoutInflater;

    public BottomSheetAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    class BottomSheetViewHolder extends RecyclerView.ViewHolder {

        ImageView userPic;
        TextView tvYearMake;
        TextView tvBottomSheetCarModel;
        TextView tvFuelCost;

        BottomSheetViewHolder(View itemView) {
            super(itemView);
            userPic = itemView.findViewById(R.id.imageView1);
            tvYearMake = itemView.findViewById(R.id.year_make);
            tvBottomSheetCarModel = itemView.findViewById(R.id.bottom_sheet_car_model);
            tvFuelCost = itemView.findViewById(R.id.fuel_cost);
        }
    }

    @NonNull
    @Override
    public BottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.cars_bottomsheet_template_row, parent, false);
        return new BottomSheetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetViewHolder holder, int position) {
        if(cars != null){
            String year_make = cars.get(position).getYear() + " " + cars.get(position).getMake();
            String model = cars.get(position).getModel();
            String fuelCost = "Fuel Cost: $" + cars.get(position).costOfGas(2.84, distanceMeters);

            holder.tvYearMake.setText(year_make);
            holder.tvBottomSheetCarModel.setText(model);
            holder.tvFuelCost.setText(fuelCost);
        }
    }

    public void setCars(List<Car> cars){
        this.cars = cars;
        notifyDataSetChanged();
    }
    
    public void setDistance(long distanceMeters){
        this.distanceMeters = distanceMeters;
    }

    @Override
    public int getItemCount() {
        if(cars != null){
            return cars.size();
        }else{
            return 0;
        }
    }
}
