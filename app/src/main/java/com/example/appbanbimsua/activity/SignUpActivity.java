package com.example.appbanbimsua.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.request.SignUpRequest;
import com.example.appbanbimsua.enitities.response.SignUpResponse;
import com.example.appbanbimsua.enitities.response.UserResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    TextInputLayout txtPassLayout, txtCfPassLayout;
    TextInputEditText txtEmail, txtUsername, txtPassEdt, txtCfPassEdt, txtPhone;
    private Button btnSignUp;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUi();
        initListener();
    }

    private void initUi() {
        txtEmail = findViewById(R.id.txt_emailedt);
        txtUsername = findViewById(R.id.txt_usernameedt);
        txtPassLayout = findViewById(R.id.txt_passlayout);
        txtPassEdt = findViewById(R.id.txt_passEdt);
        txtCfPassLayout = findViewById(R.id.txt_cfpasslayout);
        txtCfPassEdt = findViewById(R.id.txt_cfpassEdt);
        txtPhone = findViewById(R.id.txt_phoneedt);
        btnSignUp = findViewById(R.id.btn_sign_up);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        txtPassEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String password = s.toString();
                if (isPasswordValid(password)) {
                    txtPassLayout.setHelperText("Your Password are Strong");
                    txtPassLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.teal_700)));
                    txtPassLayout.setError("");
                } else {
                    txtPassLayout.setHelperText("mix of letters(upper and lower case), number and symbols");
                    txtPassLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.orange3)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtCfPassEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String password = s.toString();
                if (password.equals(txtPassEdt.getText().toString())) {
                    txtCfPassLayout.setHelperText("Mật khẩu hợp lệ");
                    txtCfPassLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.teal_700)));
                    txtCfPassLayout.setError("");
                } else {
                    txtCfPassLayout.setHelperText("Mật khẩu không khớp");
                    txtCfPassLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.orange3)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        Pattern pattern = Pattern.compile("[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }
    private void onClickSignUp() {

        String strEmail = txtEmail.getText().toString().trim();
        String strUsername = txtUsername.getText().toString().trim();
        String strPassword = txtPassEdt.getText().toString().trim();
        String strConfirmPassword = txtCfPassEdt.getText().toString().trim();
        String strPhone = txtPhone.getText().toString().trim();

        if (strEmail.isEmpty() || strUsername.isEmpty() || strPassword.isEmpty() || strConfirmPassword.isEmpty() || strPhone.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem mật khẩu đã nhập và đáp ứng yêu cầu không
        if (!isPasswordValid(strPassword)) {
            txtPassLayout.setError("Mật khẩu không hợp lệ. Mật khẩu phải có ít nhất 8 ký tự và chứa ít nhất một chữ cái, một số và một ký tự đặc biệt.");
            return;
        }

        // Kiểm tra xem mật khẩu và mật khẩu xác nhận có khớp nhau không
        if (!strPassword.equals(strConfirmPassword)) {
            txtCfPassLayout.setError("Mật khẩu xác nhận không khớp.");
            return;
        }
        progressDialog.setMessage("Đang đăng ký...");
        progressDialog.show();

        SignUpRequest signUpRequest = new SignUpRequest(strUsername, strEmail, strPhone, strPassword);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<SignUpResponse> call = apiService.signUp(signUpRequest);

        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse =  response.body();
                    if (!(userResponse == null)) {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Log.d("UserResponse", userResponse.toString());
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // Lấy thông báo lỗi từ phản hồi
                        String errorJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorJson);
                        String message = jsonObject.getString("message");

                        // Hiển thị thông báo lỗi cụ thể từ API
                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại! Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}