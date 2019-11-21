package com.example.fitness_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitness_app.R;

import java.util.List;

public class TrackCategoriesAdapter extends RecyclerView.Adapter<TrackCategoriesAdapter.CategoriesViewHolder> {
    private List<String> categories;
    private LayoutInflater mInflater;
    private ItemClickListener mItemClickListener;

    public TrackCategoriesAdapter(Context context, List<String> categories) {
        this.mInflater = LayoutInflater.from(context);
        this.categories = categories;
    }

    public void setItemClickListener(ItemClickListener clickListener){
        this.mItemClickListener = clickListener;
    }

    public List<String> getCategories(){return this.categories;}

    /**
     * Bind adapter item to a layout file
     * @param parent
     * @param viewType
     * @return CategoriesViewHolder
     */
    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.track_category_item, parent, false);
        return new CategoriesViewHolder(view);
    }

    /**
     * Populate each adapter item to some data
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        holder.categoryTitleTextView.setText(categories.get(position));
    }

    /**
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryTitleTextView;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitleTextView = itemView.findViewById(R.id.tack_category_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null){
                mItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
