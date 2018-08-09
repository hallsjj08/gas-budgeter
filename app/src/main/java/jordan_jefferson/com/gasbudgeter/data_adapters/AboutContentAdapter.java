package jordan_jefferson.com.gasbudgeter.data_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jordan_jefferson.com.gasbudgeter.R;
import jordan_jefferson.com.gasbudgeter.data.AboutContent;

public class AboutContentAdapter extends RecyclerView.Adapter<AboutContentAdapter.AboutContentViewHolder> {

    private ArrayList<AboutContent> aboutContents;
    private LayoutInflater layoutInflater;

    public AboutContentAdapter(ArrayList<AboutContent> aboutContents, Context context) {

        this.aboutContents = aboutContents;
        layoutInflater = LayoutInflater.from(context);

    }

    public class AboutContentViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView dropDownArrow;
        TextView title;
        TextView description;

        public AboutContentViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cv_about);
            dropDownArrow = itemView.findViewById(R.id.drop_down_arrow);
            title = itemView.findViewById(R.id.about_title);
            description = itemView.findViewById(R.id.about_description);

        }
    }

    @NonNull
    @Override
    public AboutContentAdapter.AboutContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.about_template_row, parent, false);
        return new AboutContentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AboutContentAdapter.AboutContentViewHolder holder, int position) {
        holder.title.setText(aboutContents.get(position).getTitle());
        holder.description.setText(aboutContents.get(position).getDescription());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.dropDownArrow.setRotation(90f);
                holder.description.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(aboutContents != null){
            return aboutContents.size();
        }
        return 0;
    }

}
