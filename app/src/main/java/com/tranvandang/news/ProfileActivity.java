package com.tranvandang.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tranvandang.news.Adapter.TrendingProfileAdapter;
import com.tranvandang.news.Model.Bookmark;
import com.tranvandang.news.Model.Category;
import com.tranvandang.news.Model.Ditorial;
import com.tranvandang.news.Model.News;
import com.tranvandang.news.Model.User;
import com.tranvandang.news.ViewModel.NewsDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfileActivity extends AppCompatActivity implements TrendingProfileAdapter.OnItemProfileTClickListener {
    FirebaseAuth auth;
    FirebaseUser user;

    RecyclerView recyclerTrending;
    TrendingProfileAdapter trendingAdapter;
    List<NewsDetail> newsDetailList = new ArrayList<>();
    TextView bookMarkCount,nameUser,emailUser;
    ImageView imgAvt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null)
        {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);

        }
        else{
            bookMarkCount = findViewById(R.id.bookmark);
            imgAvt = findViewById(R.id.avt);
            nameUser = findViewById(R.id.fullName);
            emailUser = findViewById(R.id.email);
            recyclerTrending = findViewById(R.id.recTrending);
            recyclerTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            DatabaseReference data = FirebaseDatabase.getInstance().getReference();
            DatabaseReference dataBookmark = data.child("bookmark");
            dataBookmark.addListenerForSingleValueEvent(new ValueEventListener() {
                final int[] countBookmark = {0};
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("keyUser").getValue().equals(user.getUid())){
                            countBookmark[0]++;
                        }

                    }
                    if(bookMarkCount!=null){
                        bookMarkCount.setText(countBookmark[0] + "");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            DatabaseReference dataAccount = data.child("users");
            dataAccount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals(user.getUid())) {
                            User ac = dataSnapshot.getValue(User.class);
                            Glide.with(imgAvt.getContext())
                                    .load(ac.getImgUrl())
                                    .into(imgAvt);
                            if(nameUser!=null){
                                nameUser.setText(ac.getName());
                            }
                            if(emailUser!=null){
                                emailUser.setText(ac.getEmail());
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void onItemProfileTClick(String newsKey) {

    }
    public void fillTrending(AtomicInteger tasksCounter){
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
                                taskCompleted(tasksCounter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                taskCompleted(tasksCounter);
                            }


                        });
                        data.child("categories").child(news.getKeyCategory()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Category cate = snapshot.getValue(Category.class);
                                newsDetail.setNameCategory(cate.getName());
                                taskCompleted(tasksCounter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                taskCompleted(tasksCounter);
                            }

                        });
                        newsDetailList.add(newsDetail);
                    }
                }
                trendingAdapter = new TrendingProfileAdapter(newsDetailList,ProfileActivity.this);

                recyclerTrending.setAdapter(trendingAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    private void taskCompleted(AtomicInteger tasksCounter) {
        if (tasksCounter.incrementAndGet() == newsDetailList.size() * 2) {
            // Tất cả công việc đã hoàn thành, cập nhật Adapter và RecyclerView
            trendingAdapter = new TrendingProfileAdapter(newsDetailList,this);

            recyclerTrending.setAdapter(trendingAdapter);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        fillTrending( new AtomicInteger(0));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

