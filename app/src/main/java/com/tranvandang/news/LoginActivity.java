package com.tranvandang.news;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ImageView imageViewGoogle;
    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private TextView loginButton;
    int RC_SIGN_IN = 10;
    private void googleSignIn()
    {
        Intent intent = mGog.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            }catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    FirebaseUser user = auth.getCurrentUser();
                    Map<String, String> map = new HashMap<>();

// Kiểm tra và thêm 'id' vào map
                    if (user.getUid() != null) {
                        map.put("id", user.getUid());
                    }

// Kiểm tra và thêm 'name' vào map
                    if (user.getDisplayName() != null) {
                        map.put("name", user.getDisplayName());
                    }

// Kiểm tra và thêm 'profile' vào map
                    if (user.getPhotoUrl() != null) {
                        map.put("profile", user.getPhotoUrl().toString());
                    }
                    database.getReference().child("user").child(user.getUid()).setValue(map);
                    Intent intent = new Intent(LoginActivity.this,FillinfoActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Something went off!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    GoogleSignInClient mGog;
    FirebaseDatabase database;
    private TextView textViewForgot;
    /*protected void  onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }*/
   @SuppressLint({"WrongViewCast", "MissingInflatedId"})
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewForgot = findViewById(R.id.forgot_the_);
        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.userName);
        loginPassword = findViewById(R.id.passWord);
        loginButton = findViewById(R.id.button_login);
        signupRedirectText = findViewById(R.id.signup_button);
        imageViewGoogle = findViewById(R.id.button_gog);

       GoogleSignInOptions gso = new GoogleSignInOptions.
               Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
               requestIdToken(getString(R.string.default_web_client_id))
                       .requestEmail().build();
       mGog = GoogleSignIn.getClient(this,gso);

       imageViewGoogle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            googleSignIn();
           }
       });
        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotpassActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if (!pass.isEmpty()){
                        auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Login Succesful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,FillinfoActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        loginPassword.setError("Password cannot be empty!");
                    }
                } else if (email.isEmpty()) {
                    loginEmail.setError("Email cannot be empty!");
                }else {
                    loginEmail.setError("Please enter valid Email!");
                }
            }
        });
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
    }
}
