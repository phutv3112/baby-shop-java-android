package com.example.appbanbimsua.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.request.LoginRequest;
import com.example.appbanbimsua.enitities.response.UserResponse;
import com.example.appbanbimsua.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout layoutSignUp;
    TextInputEditText txtEmail,  txtPassEdt;
    private Button btnSignIn;
    private ProgressDialog progressDialog;
    private TextView tv_forgot_pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        initListener();
    }
    private void initUI() {
        layoutSignUp = findViewById(R.id.layout_sign_up);
        txtEmail = findViewById(R.id.txt_emailedt);
        txtPassEdt = findViewById(R.id.txt_passEdt);
        btnSignIn = findViewById(R.id.btn_sign_in);
        progressDialog = new ProgressDialog(this);
        tv_forgot_pw = findViewById(R.id.tv_forgot_pass);
        tv_forgot_pw.setVisibility(View.GONE);
    }
    private void initListener() {
        layoutSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignIn();
            }
        });
//        tv_forgot_pw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignInActivity.this, ForgotPassActivity.class);
//                startActivity(intent);
//            }
//        });
    }
    private void onClickSignIn() {
        String strEmail = txtEmail.getText().toString().trim();
        String strPassword = txtPassEdt.getText().toString().trim();

        if (strEmail.isEmpty() || strPassword.isEmpty()) {
            // Hiển thị thông báo yêu cầu nhập đầy đủ thông tin
            Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.show();

        // Gọi API đăng nhập
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(strEmail, strPassword);

        Call<UserResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    // Lưu trạng thái đăng nhập và thông tin người dùng
                    saveLoginState(true);
                    saveUserInfo(userResponse);
                    // Chuyển sang MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Login", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void saveLoginState(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREF_LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Utils.KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

//    private void saveUserInfo(UserResponse userResponse) {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("id", userResponse.getId());
//        editor.putString("fullName", userResponse.getFullName());
//        editor.putString("email", userResponse.getEmail());
//        editor.putString("phone", userResponse.getPhone());
//        editor.putStringSet("roles", new HashSet<>(userResponse.getRoles()));
//        editor.apply();
//    }
    private void saveUserInfo(UserResponse userResponse) {
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREF_USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(userResponse);
        editor.putString(Utils.KEY_USER_INFO, userJson);
        editor.apply();
    }
}