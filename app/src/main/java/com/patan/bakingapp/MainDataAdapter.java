package com.patan.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.patan.bakingapp.databinding.MainCardRowBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainDataAdapter extends RecyclerView.Adapter<MainDataAdapter.ViewHolder> {
    public interface RecipeItemClickListener {
        void onRecipeItemClick(int clickedItemIndex);
    }

    final private RecipeItemClickListener mOnClickListener;
    private List<Recipe> recipes;

    public MainDataAdapter(List<Recipe> recipes, RecipeItemClickListener clickListener) {
        this.recipes = recipes;
        this.mOnClickListener = clickListener;
    }

    @NonNull
    @Override
    public MainDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MainCardRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_card_row, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MainDataAdapter.ViewHolder holder, int position) {
        holder.binding.tvName.setText(recipes.get(position).getName());
        holder.setImage(recipes.get(position).getImage());
    }

    public Recipe getRecipeAtIndex(int index) {
        return recipes.get(index);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MainCardRowBinding binding;
        private Context context;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            context = itemView.getContext();

            itemView.setOnClickListener(this);
        }

        void setImage(String image) {
            if (image.trim().equals("")){
                binding.rowIvMainThumb.setImageResource(R.drawable.recipe);
            }else {
                Picasso.get()
                        .load(image)
                        .into(binding.rowIvMainThumb);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRecipeItemClick(clickedPosition);
        }
    }
}
