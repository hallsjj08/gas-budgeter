package jordan_jefferson.com.gasbudgeter.gui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.network.ClientItem;

public class FuelEconomyDataListAdapter extends
        RecyclerView.Adapter<FuelEconomyDataListAdapter.FuelEconomyDataViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<ClientItem> clientItems;

    public FuelEconomyDataListAdapter(List<ClientItem> clientListItems, Context context){
        this.mLayoutInflater = LayoutInflater.from(context);
        this.clientItems = clientListItems;
    }

    public class FuelEconomyDataViewHolder extends RecyclerView.ViewHolder {

        TextView dataView;

        public FuelEconomyDataViewHolder(View itemView) {
            super(itemView);
            dataView = itemView.findViewById(R.id.fuelEconomyItem);
        }
    }

    @NonNull
    @Override
    public FuelEconomyDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.fueleconomy_data_template_row, parent, false);
        return new FuelEconomyDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FuelEconomyDataViewHolder holder, int position) {

        if(clientItems != null){
            holder.dataView.setText(clientItems.get(position).getText());
        }

    }

    @Override
    public int getItemCount() {
        if(clientItems != null){
            return clientItems.size();
        }
        return 0;
    }
}
