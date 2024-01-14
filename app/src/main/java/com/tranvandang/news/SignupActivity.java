package com.tranvandang.news;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tranvandang.news.Model.User;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword,dayText,monthText,yearText,phone,fullName,confPass;
    private TextView signupButton;
    private TextView loginRedirectText;
    private ImageView imgDateOfBirth,IMGgoogle,imgAvt;
    private LinearLayout themAvt;
    private Calendar calendar;
    private Uri selectedImageUri;

   /* @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }*/


    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.userName);
        signupPassword = findViewById(R.id.passWord);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.login_again);
        dayText = findViewById(R.id.day);
        monthText = findViewById(R.id.month);
        yearText = findViewById(R.id.year);
        phone = findViewById(R.id.phone);
        fullName = findViewById(R.id.fullName);
        confPass = findViewById(R.id.passWordConfirm);
        imgDateOfBirth = findViewById(R.id.calendar);
        calendar = Calendar.getInstance();
        themAvt = findViewById(R.id.addAvt);
        imgAvt = findViewById(R.id.avt);
        themAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        imgDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String passConfirm = confPass.getText().toString().trim();
                String name = fullName.getText().toString().trim();
                String phoneNumber = phone.getText().toString().trim();
                Integer dayInt = Integer.parseInt(dayText.getText().toString());
                Integer monthInt = Integer.parseInt(monthText.getText().toString());
                Integer yearInt = Integer.parseInt(yearText.getText().toString());
                if (user.isEmpty())
                {
                    signupEmail.setError("Email is required or empty!");
                }
                else if(pass.isEmpty()){
                    signupPassword.setError("Password is required or empty!");
                }
                else if(phoneNumber.isEmpty()){
                    phone.setError("Phone number is required or empty!");
                }
                else if(dayInt==null||monthInt==null||yearInt==null){
                    Toast.makeText(SignupActivity.this, "Date of birth is required!", Toast.LENGTH_SHORT).show();
                }
                else if (pass.equals(passConfirm)!=true){
                    confPass.setError("Password Confirmation does not match");
                }else {
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful())
                          {

                              saveUserDetailsToDatabase( dayInt, monthInt, yearInt, name,user, phoneNumber,pass);
                              Toast.makeText(SignupActivity.this, "Sign up succesful!", Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(SignupActivity.this, LoginActivity.class ));
                          }else {
                              Toast.makeText(SignupActivity.this, "Sign up failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                          }
                        }
                    });
                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
    }

    private void saveUserDetailsToDatabase(Integer day, Integer month, Integer year, String fullName, String email, String phone, String pass) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        String userId = auth.getCurrentUser().getUid();

        // Lưu thông tin người dùng vào Realtime Database
        User user = new User(fullName, email, pass, phone, day, month, year, "url");
        usersRef.child(userId).setValue(user);

        // Nếu người dùng đã chọn ảnh, lưu ảnh vào Storage và URL vào Realtime Database
        if (selectedImageUri != null) {
            saveImageToStorage(userId, selectedImageUri);
        } else {
            Toast.makeText(SignupActivity.this, "Please select an image!", Toast.LENGTH_SHORT).show();
        }
    }
    public void showDatePickerDialog(View v) {
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Toast.makeText(SignupActivity.this, "Ngày Sinh: " + selectedDate, Toast.LENGTH_SHORT).show();
                        dayText.setText(String.valueOf(dayOfMonth));
                        monthText.setText(String.valueOf(month + 1));
                        yearText.setText(String.valueOf(year));
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null) {
            Uri imagePath = data.getData();
            Glide.with(this).load(imagePath).into(imgAvt);
            // Lưu URI của ảnh vào một biến toàn cục để sử dụng khi nhấn nút Sign Up
            selectedImageUri = imagePath;
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void saveImageToStorage(String userId, Uri imagePath) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("avatars");

        // Tạo tên file duy nhất dựa trên ID của người dùng và định dạng ảnh
        String fileExtension = getFileExtension(imagePath);
        String imageName = userId + "." + fileExtension;

        // Tạo tham chiếu đến file trên Storage
        StorageReference imageRef = storageRef.child(imageName);

        // Tải ảnh lên Storage
        imageRef.putFile(imagePath)
                .addOnSuccessListener(taskSnapshot -> {
                    // Lấy URL của ảnh sau khi tải lên
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Lưu URL vào Realtime Database
                        DatabaseReference imageUrlRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("imgUrl");
                        imageUrlRef.setValue(uri.toString());
                        Toast.makeText(SignupActivity.this, "Avatar uploaded successfully!", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(SignupActivity.this, "Avatar upload failed!", Toast.LENGTH_SHORT).show());

    }
}


