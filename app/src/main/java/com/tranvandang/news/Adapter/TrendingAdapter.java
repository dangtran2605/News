package com.tranvandang.news.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import com.tranvandang.news.R;
import com.tranvandang.news.ViewModel.NewsDetail;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.myViewHolder> {

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String key);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private List<NewsDetail> newsList;

    public TrendingAdapter(List<NewsDetail> newsList) {
        this.newsList = newsList;
    }
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bind(newsList.get(position));
        // Set click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && mListener != null) {
                    // Lấy key từ NewsDetail
                    NewsDetail clickedNews = newsList.get(adapterPosition);
                    String key = clickedNews.getKey();

                    // Gọi phương thức onItemClick của bộ lắng nghe
                    mListener.onItemClick(key);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_trending, parent, false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        //anh xa
        TextView titleNews, category,nameDito;
        ImageView imageNews,imgDitorial;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            titleNews = itemView.findViewById(R.id.textTitle);
            category = itemView.findViewById(R.id.textCate);
            nameDito = itemView.findViewById(R.id.textNameDito);
            imageNews = itemView.findViewById(R.id.imgViewNews);
            imgDitorial = itemView.findViewById(R.id.imgDito);
        }
        //fill du lieu
        public void bind(NewsDetail model){
            titleNews.setText(model.getTitle());
            category.setText(model.getNameCategory());
            nameDito.setText(model.getNameDitorial());
            Glide.with(imageNews.getContext())
                    .load(model.getImgUrl1())
                    .into(imageNews);
            Glide.with(imgDitorial.getContext())
                    .load(model.getLogoDitoUrl())
                    .into(imgDitorial);
        }
    }
}
