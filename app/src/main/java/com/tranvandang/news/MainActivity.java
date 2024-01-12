package com.tranvandang.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tranvandang.news.Adapter.TrendingAdapter;
import com.tranvandang.news.Model.Category;
import com.tranvandang.news.Model.Ditorial;
import com.tranvandang.news.Model.News;
import com.tranvandang.news.ViewModel.NewsDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    AtomicInteger tasksCounter = new AtomicInteger(0);
    RecyclerView recyclerTrending;
    TrendingAdapter trendingAdapter;
    List<NewsDetail> newsDetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        /*user = auth.getCurrentUser();
        if(user != null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

        }*/
        //anh xa
        recyclerTrending = findViewById(R.id.recyclerTrending);
        //set kieu layout hori hàng ngang veri hàng dọc
        recyclerTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //lay du lieu
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRefNews = data.child("news");
        final int[] dem = {0};
        dataRefNews.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Long count = (Long) dataSnapshot.child("countUser").getValue();
                    if (count >= 5000&& dem[0] <5) {
                        News news = dataSnapshot.getValue(News.class);
                        NewsDetail newsDetail = new NewsDetail();
                        newsDetail.setTitle(news.getTitle());
                        newsDetail.setDetail(news.getDetail());
                        newsDetail.setDescription(news.getDescription());
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
                        dem[0]++;
                    }
                }
                trendingAdapter = new TrendingAdapter(newsDetailList);

                /*trendingAdapter.setOnItemClickListener(new TrendingAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String key) {
                        //tạo intent chuyển qua activity mới Main->Detail
                        Intent intent = new Intent(MainActivity.this, DetailNewsActivity.class);
                        //chuyển thêm data đính kèm
                        intent.putExtra("news_id", key);
                        intent.putExtra("from","main");
                        //chạy intent
                        startActivity(intent);
                    }
                });*/
                recyclerTrending.setAdapter(trendingAdapter);
                trendingAdapter.setOnItemClickListener(new TrendingAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String key) {
                        //tạo intent chuyển qua activity là Main->Detail
                        Intent intent = new Intent(MainActivity.this, DetailNewsActivity.class);
                        //chuyển thể data định kèm
                        intent.putExtra("news_id", key);
                        intent.putExtra("from","main");
                        //chạy intent
                        startActivity(intent);
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }
    private void taskCompleted() {
        if (tasksCounter.incrementAndGet() == newsDetailList.size() * 2) {
            // Tất cả công việc đã hoàn thành, cập nhật Adapter và RecyclerView
            trendingAdapter = new TrendingAdapter(newsDetailList);
            recyclerTrending.setAdapter(trendingAdapter);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}