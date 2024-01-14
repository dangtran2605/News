package com.tranvandang.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tranvandang.news.Adapter.CategoryAdapter;
import com.tranvandang.news.Adapter.LatestAdapter;
import com.tranvandang.news.Adapter.SeeallTrendingAdapter;
import com.tranvandang.news.Adapter.TrendingAdapter;
import com.tranvandang.news.Model.Category;
import com.tranvandang.news.Model.Ditorial;
import com.tranvandang.news.Model.News;
import com.tranvandang.news.ViewModel.NewsDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements TrendingAdapter.OnItemClickListener, CategoryAdapter.OnItemCateClickListener, LatestAdapter.OnItemLatestClickListener {
    FirebaseAuth auth;
    Button button;
    TextView seeAllTrending,seeAllLatest;
    FirebaseUser user;
    AtomicInteger tasksCounter = new AtomicInteger(0);


    RecyclerView recyclerTrending, recycCate, recLatest;
    CategoryAdapter categoryAdapter;
    TrendingAdapter trendingAdapter;
    LatestAdapter latestAdapter;
    List<NewsDetail> newsDetailList = new ArrayList<>();
    List<Category> categoryList = new ArrayList<>();
    List<NewsDetail> latestList = new ArrayList<>();
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
        recycCate = findViewById(R.id.recCate);
        recLatest = findViewById(R.id.recNews);
        seeAllTrending = findViewById(R.id.seeAllTrend);
        seeAllLatest = findViewById(R.id.seeAllLatest);
        seeAllTrending.setOnClickListener(view -> onBack() );
        //set kieu layout hori hàng ngang veri hàng dọc
        recyclerTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycCate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recLatest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
                        dem[0]++;
                    }
                }
                trendingAdapter = new TrendingAdapter(newsDetailList,MainActivity.this);

                recyclerTrending.setAdapter(trendingAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        DatabaseReference dataCate = data.child("categories");
        dataCate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    category.setKey(dataSnapshot.getKey());
                    taskCompletedCate();
                    categoryList.add(category);
                }
                categoryAdapter = new CategoryAdapter(categoryList,MainActivity.this);
                recycCate.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void onBack() {
        Intent intent = new Intent(MainActivity.this, SeeallTrendingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemCateClick(String cateKey) {
        AtomicInteger tasksCounter2 = new AtomicInteger(0);
        latestList.clear();
        fillLatest(cateKey, tasksCounter2);
    }
    public void fillLatest(String keyCate,AtomicInteger tasksCounter2){
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRefNews = data.child("news");
        final int[] dem = {0};
        dataRefNews.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Long count = (Long) dataSnapshot.child("countUser").getValue();
                    if (dem[0] <5&& dataSnapshot.child("keyCategory").getValue().toString().equals(keyCate)) {
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
                                taskCompletedLatest(tasksCounter2);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                taskCompletedLatest(tasksCounter2);
                            }


                        });
                        data.child("categories").child(news.getKeyCategory()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Category cate = snapshot.getValue(Category.class);
                                newsDetail.setNameCategory(cate.getName());
                                taskCompletedLatest(tasksCounter2);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                taskCompletedLatest(tasksCounter2);
                            }

                        });
                        latestList.add(newsDetail);
                        dem[0]++;
                    }
                }
                latestAdapter = new LatestAdapter(latestList,MainActivity.this);

                recLatest.setAdapter(latestAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void onItemClick(String newsKey) {
        // Xử lý sự kiện khi một mục được nhấp, ví dụ: mở hoạt động chi tiết với khóa được nhấp
        Intent intent = new Intent(MainActivity.this, DetailNewsActivity.class);
        intent.putExtra("NEWS_KEY", newsKey);
        intent.putExtra("from", "main");
        startActivity(intent);
    }
    private void taskCompletedCate(){
        if (tasksCounter.incrementAndGet() == newsDetailList.size() * 2) {
            // Tất cả công việc đã hoàn thành, cập nhật Adapter và RecyclerView
            categoryAdapter = new CategoryAdapter(categoryList,this);
            recycCate.setAdapter(categoryAdapter);

        }
    }
    private void taskCompletedLatest(AtomicInteger tasksCounter2) {
        if (tasksCounter2.incrementAndGet() == latestList.size() * 2) {
            // Tất cả công việc đã hoàn thành, cập nhật Adapter và RecyclerView
            latestAdapter = new LatestAdapter(latestList,this);
            recLatest.setAdapter(latestAdapter);
        }
    }
    private void taskCompleted() {
        if (tasksCounter.incrementAndGet() == newsDetailList.size() * 2) {
            // Tất cả công việc đã hoàn thành, cập nhật Adapter và RecyclerView
            trendingAdapter = new TrendingAdapter(newsDetailList,this);

            recyclerTrending.setAdapter(trendingAdapter);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        fillLatest("-idC1",new AtomicInteger(0));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onItemLatestClick(String newsKey) {
        // Xử lý sự kiện khi một mục được nhấp, ví dụ: mở hoạt động chi tiết với khóa được nhấp
        Intent intent = new Intent(MainActivity.this, DetailNewsActivity.class);
        intent.putExtra("NEWS_KEY", newsKey);
        intent.putExtra("from", "main");
        startActivity(intent);
    }
}