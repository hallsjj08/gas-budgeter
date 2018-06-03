package jordan_jefferson.com.gasbudgeter.test_gui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.Car;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {

    private final LayoutInflater mLayoutInflator;
    private List<Car> cars;

    public CarListAdapter(Context context){ mLayoutInflator = LayoutInflater.from(context); }

    class CarViewHolder extends RecyclerView.ViewHolder{

        TextView year;
        TextView make;
        TextView model;

        public CarViewHolder(View itemView) {
            super(itemView);

            year = itemView.findViewById(R.id.year);
            make = itemView.findViewById(R.id.make);
            model = itemView.findViewById(R.id.model);
        }
    }

    @NonNull
    @Override
    public CarListAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflator.inflate(R.layout.car_template_row, parent, false);
        return new CarViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CarListAdapter.CarViewHolder holder, int position) {

        if(cars != null){
            holder.year.setText(cars.get(position).getYear() + "");
            holder.make.setText(cars.get(position).getMake());
            holder.model.setText(cars.get(position).getModel());
        }

    }

    void setCars(List<Car> cars){
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
