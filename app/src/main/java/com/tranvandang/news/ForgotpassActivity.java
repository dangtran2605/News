package com.tranvandang.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotpassActivity extends AppCompatActivity
{
    private void ResetPassword()
    {
        progressBar.setVisibility(View.VISIBLE);
        fSubmit.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgotpassActivity.this,"Reset Password link has been sent!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgotpassActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Toast.makeText(ForgotpassActivity.this,"Error :" +e.getMessage(),Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            fSubmit.setVisibility(View.VISIBLE);
            }
        });
    }
    private TextView fSubmit;
    private EditText fEmail;
    private TextView fBack;
    String strEmail;

    private TextView textViewTitle;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        fBack = findViewById(R.id.textViewBack);
        fEmail = findViewById(R.id.editTextEmail);
        fSubmit = findViewById(R.id.textViewSubmit);
        textViewTitle = findViewById(R.id.textViewTitle);
        fSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = fEmail.getText().toString().trim();
                if (!TextUtils.isEmpty(strEmail))
                {
                    ResetPassword();
                }else {
                    fEmail.setError("Email cannot be empty!");
                }
            }
        });
    fBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    });

    }
}
