package jordan_jefferson.com.gasbudgeter.data_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;
import jordan_jefferson.com.gasbudgeter.gui.OnClickCarData;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<Car> cars;
    private OnClickCarData onClickCarData;

    public CarListAdapter(Context context, OnClickCarData onClickCarData){
        mLayoutInflater = LayoutInflater.from(context);
        this.onClickCarData = onClickCarData;
    }

    class CarViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        ImageView imageView;
        TextView carText;
        ImageButton editCarButton;
        Button viewSpecsButton;

        private CarViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.card_view);
            imageView = itemView.findViewById(R.id.thumbnail);
            carText = itemView.findViewById(R.id.myCarText);
            editCarButton = itemView.findViewById(R.id.edit_car_button);
            viewSpecsButton = itemView.findViewById(R.id.view_specs_button);

        }
    }

    @NonNull
    @Override
    public CarListAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.car_template_row, parent, false);
        return new CarViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CarListAdapter.CarViewHolder holder, int position) {

        if(cars != null){

            String userCarDescription = cars.get(position).getYear() + " "
                    + cars.get(position).getMake() + " "
                    + cars.get(position).getModel();

            holder.carText.setText(userCarDescription);
        }

        onClickEditCar(holder, position);

        onClickViewSpecs(holder, position);

    }

    private void onClickEditCar(CarListAdapter.CarViewHolder holder, final int position){
        holder.editCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCarData.onClickEditCar(cars.get(position));
            }
        });
    }

    private void onClickViewSpecs(CarListAdapter.CarViewHolder holder, final int position){
        holder.viewSpecsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCarData.onClickViewCarSpecs(cars.get(position));
            }
        });
    }

    public void setCars(List<Car> cars){
        this.cars = cars;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(cars != null){
            return cars.size();
        }
        return 0;
    }
}
