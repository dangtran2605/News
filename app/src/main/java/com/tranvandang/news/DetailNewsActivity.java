package com.tranvandang.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.tranvandang.news.Model.Category;
import com.tranvandang.news.Model.Ditorial;
import com.tranvandang.news.Model.News;


public class DetailNewsActivity extends AppCompatActivity {
    TextView tvTitle, tvDescription, tvCate,nameDito ;
    ImageView btnBack,imgDito,imgNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        tvTitle = findViewById(R.id.textTitle);
        tvDescription = findViewById(R.id.textDescription);
        tvCate = findViewById(R.id.textCate);
        btnBack = findViewById(R.id.imgBack);
        imgDito = findViewById(R.id.imgLogoDito);
        imgNews = findViewById(R.id.imgNews);
        btnBack.setOnClickListener(v -> onBack());
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            String keyNews = extras.getString("NEWS_KEY");
            if(keyNews == null){
                Toast.makeText(DetailNewsActivity.this, "No data", Toast.LENGTH_SHORT).show();
            }
            else {
                DatabaseReference data = FirebaseDatabase.getInstance().getReference();
                DatabaseReference dataNews = FirebaseDatabase.getInstance().getReference("news").child(keyNews);
                dataNews.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            News news = dataSnapshot.getValue(News.class);
                            if (news != null) {
                                if(tvTitle != null){
                                    tvTitle.setText(news.getTitle());
                                }
                                if(tvDescription != null){
                                    tvDescription.setText(news.getDesctiption());
                                }

                                //Lênh fill ảnh từ firebase ra imageView
                                Glide.with(imgNews.getContext())
                                        .load(news.getImgUrl1())
                                        .into(imgNews);

                                data.child("ditorial").child(news.getKeyDitorial()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Ditorial dito=  snapshot.getValue(Ditorial.class);
                                        Glide.with(imgDito.getContext())
                                                .load(dito.getLogoUrl())
                                                .into(imgDito);
                                        if(nameDito != null){
                                            nameDito.setText(dito.getName());
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }


                                });
                                data.child("categories").child(news.getKeyCategory()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Category cate = snapshot.getValue(Category.class);
                                        if(tvCate != null){
                                            tvCate.setText(cate.getName());
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }

                                });

                            }

                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
            }
        }

    }
    public void onBack(){
        if(getIntent().getExtras() != null){
            Bundle extras = getIntent().getExtras();
            if(extras.getString("from").equals("main")){
                Intent intent= new Intent(DetailNewsActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
               /* Intent intent= new Intent(DetailNewsActivity.this, TrendingActivity.class);
                startActivity(intent);*/
            }
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
