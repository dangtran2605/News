package com.tranvandang.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tranvandang.news.Adapter.TrendingAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tranvandang.news.Model.Category;
import com.tranvandang.news.Model.Ditorial;
import com.tranvandang.news.Model.News;
import com.tranvandang.news.ViewModel.NewsDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SeeallTrendingActivity extends AppCompatActivity implements TrendingAdapter.OnItemClickListener {
    AtomicInteger tasksCounter = new AtomicInteger(0);
    RecyclerView recyclerTrendingseeall;
    TrendingAdapter SeeallTrendingAdapter;
    ImageView back;
    List<NewsDetail> newsDetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeall_trending_activity);
        back = findViewById(R.id.backBtn);
        back.setOnClickListener(v -> onBack());
        //anh xa
        recyclerTrendingseeall = findViewById(R.id.recyclerTrendingseeall);
        //set kieu layout hori hàng ngang veri hàng dọc
        recyclerTrendingseeall.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //lay du lieu
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRefNews = data.child("news");
        dataRefNews.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Long count = (Long) dataSnapshot.child("countUser").getValue();
                    if (count >= 5000) {
                        News news = dataSnapshot.getValue(News.class);
                        NewsDetail newsDetail = new NewsDetail();
                        newsDetail.setKey(dataSnapshot.getKey());
                        newsDetail.setTitle(news.getTitle());
                        newsDetail.setDetail(news.getDetail());
                        newsDetail.setDescription(news.getDesctiption());
                        newsDetail.setUrl(news.getUrl());
                        newsDetail.setImgUrl1(news.getImgUrl1());
                        newsDetail.setStatus(news.getStatus());
                        data.child("ditorial").child(news.getKeyDitorial()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Ditorial dito=  snapshot.getValue(Ditorial.class);
                                newsDetail.setLogoDitoUrl(dito.getLogoUrl());
                                newsDetail.setNameDitorial(dito.getName());
                                taskCompleted();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                taskCompleted();
                            }


                        });
                        data.child("categories").child(news.getKeyCategory()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Category cate = snapshot.getValue(Category.class);
                                newsDetail.setNameCategory(cate.getName());
                                taskCompleted();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                taskCompleted();
                            }

                        });
                        newsDetailList.add(newsDetail);
                    }
                }
                SeeallTrendingAdapter = new TrendingAdapter(newsDetailList,SeeallTrendingActivity.this);
                recyclerTrendingseeall.setAdapter(SeeallTrendingAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }
    private void taskCompleted() {
        if (tasksCounter.incrementAndGet() == newsDetailList.size() * 2) {
            // Tất cả công việc đã hoàn thành, cập nhật Adapter và RecyclerView
            SeeallTrendingAdapter = new TrendingAdapter(newsDetailList,this);
            recyclerTrendingseeall.setAdapter(SeeallTrendingAdapter);
        }
    }
    public void onBack(){
        if(getIntent().getExtras() != null){
            Bundle extras = getIntent().getExtras();

        }
        Intent intent= new Intent(SeeallTrendingActivity.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onItemClick(String newsKey) {
// Xử lý sự kiện khi một mục được nhấp, ví dụ: mở hoạt động chi tiết với khóa được nhấp
        Intent intent = new Intent(SeeallTrendingActivity.this, DetailNewsActivity.class);
        intent.putExtra("NEWS_KEY", newsKey);
        intent.putExtra("from", "trending");
        startActivity(intent);
    }
}
