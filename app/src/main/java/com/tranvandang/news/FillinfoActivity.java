package com.tranvandang.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.lang.annotation.Native;

public class FillinfoActivity extends AppCompatActivity {

    private void showTin(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    private LinearLayout themAvt;
    private ImageView imgAvt;
    private EditText editTextUsername, editTextFullname, editTextEmail, editTextPhone;
    private TextView textViewNext;
    private ImageView imageViewBack;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        imgAvt = findViewById(R.id.avt);
        editTextUsername = findViewById(R.id.userName);
        editTextFullname = findViewById(R.id.fullName);
        editTextEmail = findViewById(R.id.email);
        editTextPhone = findViewById(R.id.phone);
        textViewNext = findViewById(R.id.button_labe);
        imageViewBack = findViewById(R.id.back_button);
        themAvt = findViewById(R.id.addAvt);
        themAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        textViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateFields()){
                    Intent intent = new Intent(FillinfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillinfoActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private boolean ValidateFields()
    {
        if (
                TextUtils.isEmpty(editTextUsername.getText().toString()) ||
                TextUtils.isEmpty(editTextFullname.getText().toString()) ||
                TextUtils.isEmpty(editTextEmail.getText().toString())   ||
                TextUtils.isEmpty(editTextPhone.getText().toString()))
        {
            showTin("Please fill in all fields!");
            return false;
        }
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null)
        {
            String imagePath = data.getData().toString();
            Glide.with(this).load(imagePath).into(imgAvt);
        }
    }
}
