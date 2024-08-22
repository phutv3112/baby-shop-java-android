package com.example.appbanbimsua.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.request.ChangePasswordRequest;
import com.example.appbanbimsua.enitities.response.ResponseOK;
import com.example.appbanbimsua.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePwActivity extends AppCompatActivity {

    private TextInputLayout txtOldPassLayout, txtNewPassLayout, txtCfPassLayout;
    private TextInputEditText txtOldPassEdt, txtNewPassEdt, txtCfPassEdt;
    private Button btnChangePassword;
    private ProgressDialog progressDialog;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        initUi();
        initListener();
    }

    private void initUi() {
        txtOldPassLayout = findViewById(R.id.txt_old_passlayout);
        txtNewPassLayout = findViewById(R.id.txt_passlayout);
        txtCfPassLayout = findViewById(R.id.txt_cfpasslayout);
        txtOldPassEdt = findViewById(R.id.txt_old_passEdt);
        txtNewPassEdt = findViewById(R.id.txt_passEdt);
        txtCfPassEdt = findViewById(R.id.txt_cfpassEdt);
        btnChangePassword = findViewById(R.id.btn_change_password);
        img_back = findViewById(R.id.img_back);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        txtNewPassEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                if (isPasswordValid(password)) {
                    txtNewPassLayout.setHelperText("Mật khẩu mạnh");
                    txtNewPassLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.teal_700)));
                    txtNewPassLayout.setError("");
                } else {
                    txtNewPassLayout.setHelperText("Mật khẩu cần có ít nhất 8 ký tự và chứa chữ cái, số, ký tự đặc biệt");
                    txtNewPassLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.orange3)));
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
                String confirmPassword = s.toString();
                if (confirmPassword.equals(txtNewPassEdt.getText().toString())) {
                    txtCfPassLayout.setHelperText("Mật khẩu xác nhận hợp lệ");
                    txtCfPassLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.teal_700)));
                    txtCfPassLayout.setError("");
                } else {
                    txtCfPassLayout.setHelperText("Mật khẩu xác nhận không khớp");
                    txtCfPassLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.orange3)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnChangePassword.setOnClickListener(v -> onClickChangePassword());
        img_back.setOnClickListener(v -> finish());
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        Pattern pattern = Pattern.compile("[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    private void onClickChangePassword() {
        String oldPassword = txtOldPassEdt.getText().toString().trim();
        String newPassword = txtNewPassEdt.getText().toString().trim();
        String confirmPassword = txtCfPassEdt.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(ChangePwActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu có khớp không
        if (!newPassword.equals(confirmPassword)) {
            txtCfPassLayout.setError("Mật khẩu xác nhận không khớp.");
            return;
        }

        // Kiểm tra mật khẩu mới có hợp lệ không
        if (!isPasswordValid(newPassword)) {
            txtNewPassLayout.setError("Mật khẩu không hợp lệ. Mật khẩu phải có ít nhất 8 ký tự và chứa ít nhất một chữ cái, một số và một ký tự đặc biệt.");
            return;
        }

        progressDialog.setMessage("Đang đổi mật khẩu...");
        progressDialog.show();

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword, newPassword);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<ResponseOK> call = apiService.changePassword((long) Utils.getUserInfo(this).getId(), changePasswordRequest);

        call.enqueue(new Callback<ResponseOK>() {
            @Override
            public void onResponse(Call<ResponseOK> call, Response<ResponseOK> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    // Hiện AlertDialog thông báo thành công
                    new AlertDialog.Builder(ChangePwActivity.this)
                            .setTitle("Thành công")
                            .setMessage("Đổi mật khẩu thành công")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorJson);
                        String message = jsonObject.getString("message");
                        Toast.makeText(ChangePwActivity.this, message, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ChangePwActivity.this, "Đổi mật khẩu thất bại! Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOK> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChangePwActivity.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
