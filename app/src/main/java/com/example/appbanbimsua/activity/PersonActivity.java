package com.example.appbanbimsua.activity;

import static com.example.appbanbimsua.utils.Utils.PREF_USER_INFO;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.example.appbanbimsua.enitities.request.UpdateProfileRequest;
import com.example.appbanbimsua.enitities.response.ResponseOK;
import com.example.appbanbimsua.enitities.response.UserResponse;
import com.example.appbanbimsua.utils.Utils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonActivity extends AppCompatActivity {
    private Button btnEdit;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvEmail;
    private TextView tvAddress;
    private ImageView img_back;
    private UserResponse userInfo = new  UserResponse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        userInfo = Utils.getUserInfo(this);
        initUI();
        initData();
        initListen();
    }

    private void initUI() {
        btnEdit = findViewById(R.id.btnEdit);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        img_back = findViewById(R.id.img_back);
    }

    private void initData() {
        tvName.setText("Họ và tên: " + userInfo.getFullName());
        tvPhone.setText("Số điện thoại: " + userInfo.getPhone());
        tvEmail.setText("Email: " + userInfo.getEmail());
        tvAddress.setText("Địa chỉ: " + userInfo.getAddress());
    }

    private void initListen() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
        img_back.setOnClickListener(v -> onBackPressed());
    }

    private void showEditDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_user_info, null);
        builder.setView(dialogView);

        final EditText etName = dialogView.findViewById(R.id.etName);
        final EditText etPhone = dialogView.findViewById(R.id.etPhone);
        final EditText etAddress = dialogView.findViewById(R.id.etAddress);

        etName.setText(userInfo.getFullName());
        etPhone.setText(userInfo.getPhone());
        etAddress.setText(userInfo.getAddress());

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Cập nhật dữ liệu trong `TextView`
            tvName.setText(etName.getText().toString());
            tvPhone.setText(etPhone.getText().toString());
            tvAddress.setText(etAddress.getText().toString());

            UpdateProfileRequest profileReq = new UpdateProfileRequest();
            profileReq.setFullName(etName.getText().toString());
            profileReq.setPhone(etPhone.getText().toString());
            profileReq.setAddress(etAddress.getText().toString());

            Long userId = (long) Utils.getUserInfo(PersonActivity.this).getId();
            updateUserProfile(userId, profileReq);
            dialog.dismiss();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void saveUserInfo(Context context, UserResponse userResponse) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(userResponse);
        editor.putString(Utils.KEY_USER_INFO, userJson);
        editor.apply(); // Hoặc editor.commit(); để lưu đồng bộ
    }

    private void updateUserProfile(Long userId, UpdateProfileRequest profileReq) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseOK> call = apiService.updateProfile(userId, profileReq);
        call.enqueue(new Callback<ResponseOK>() {
            @Override
            public void onResponse(Call<ResponseOK> call, Response<ResponseOK> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    // Cập nhật thông tin trong SharedPreferences
                    UserResponse userResponse = Utils.getUserInfo(PersonActivity.this);
                    userResponse.setFullName(profileReq.getFullName());
                    userResponse.setPhone(profileReq.getPhone());
                    userResponse.setAddress(profileReq.getAddress());
                    saveUserInfo(PersonActivity.this, userResponse);

                    tvName.setText(userResponse.getFullName());
                    tvPhone.setText(userResponse.getPhone());
                    tvAddress.setText(userResponse.getAddress());

                    Toast.makeText(PersonActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseOK> call, Throwable t) {
                // Xử lý lỗi gọi API
                progressDialog.dismiss();
            }
        });
    }
}