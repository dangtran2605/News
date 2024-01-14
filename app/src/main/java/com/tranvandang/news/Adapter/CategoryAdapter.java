package com.tranvandang.news.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tranvandang.news.Model.Category;
import com.tranvandang.news.R;


import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.myViewHolder> {

    public interface OnItemCateClickListener {
        void onItemCateClick(String newsKey);
    }
    private final List<Category> cateList;
    private final OnItemCateClickListener listener;

    public CategoryAdapter(List<Category> cateList,OnItemCateClickListener listener) {

        this.cateList = cateList;
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bind(cateList.get(position));
        // Set click listener for the item



    }
    @Override
    public int getItemCount() {
        return cateList.size();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        //anh xa
        TextView cateText;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cateText = itemView.findViewById(R.id.cateText);
        }
        //fill du lieu
        public void bind(Category model){
            cateText.setText(model.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String key = cateList.get(position).getKey();
                        listener.onItemCateClick(key);
                    }
                }
            });
        }
    }
}
