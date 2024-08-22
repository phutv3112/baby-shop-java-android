package com.example.appbanbimsua.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanbimsua.R;

public class SettingActivity extends AppCompatActivity {

    private TextView tvAccountInfo, tvChangePassword;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Ánh xạ các TextView
        tvAccountInfo = findViewById(R.id.tvAccountInfo);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        img_back = findViewById(R.id.img_back);

        tvAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, PersonActivity.class);
                startActivity(intent);
            }
        });

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePwActivity.class);
                startActivity(intent);
            }
        });
        img_back.setOnClickListener(v -> onBackPressed());
    }
}
