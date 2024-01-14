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

public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.myViewHolder> {

    public interface OnItemLatestClickListener {
        void onItemLatestClick(String newsKey);  // Sửa đổi tham số dựa trên kiểu khóa của bạn
    }
    private List<NewsDetail> newsList;
    private OnItemLatestClickListener listener;

    public LatestAdapter(List<NewsDetail> newsList,OnItemLatestClickListener listener) {

        this.newsList = newsList;
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bind(newsList.get(position));
        // Set click listener for the item



    }
    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_latest, parent, false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        //anh xa
        TextView titleNews, category,nameDito;
        ImageView imageNews,imgDitorial;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            titleNews = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.categories);
            nameDito = itemView.findViewById(R.id.nameD);
            imageNews = itemView.findViewById(R.id.img);
            imgDitorial = itemView.findViewById(R.id.imgD);
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Lấy khóa hoặc bất kỳ dữ liệu nào bạn muốn chuyển sang hoạt động chi tiết
                        String newsKey = newsList.get(position).getKey(); // Giả sử bạn có một phương thức getKey() trong lớp NewsDetail của bạn
                        listener.onItemLatestClick(newsKey);
                    }
                }
            });
        }
    }
}

