package com.example.appbanbimsua.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.adapter.SanPhamHomeAdapter;
import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.Product;
import com.example.appbanbimsua.enitities.response.ProductResponse;
import com.example.appbanbimsua.utils.GridSpacingItemDecoration;
import com.example.appbanbimsua.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewFlipper viewFlipper;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private RecyclerView rcv_list_item;
    private SanPhamHomeAdapter sanPhamHomeAdapter;
    private ImageView imgsearch;
    private FrameLayout framegiohang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        ActionViewFlipper();
        getProduct();
        initListen();
    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        viewFlipper = findViewById(R.id.viewlipper);
        navigationView = findViewById(R.id.navigation_view);
        rcv_list_item = findViewById(R.id.rcv_list_item);
        imgsearch = findViewById(R.id.imgsearch);
        framegiohang = findViewById(R.id.framegiohang);
        progressDialog = new ProgressDialog(this);
    }

    private void initListen() {
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        framegiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ActionViewFlipper() {
        List<Integer> mangquangcao = new ArrayList<>();
        mangquangcao.add(R.drawable.img_slide_1);
        mangquangcao.add(R.drawable.img_slide_2);
        mangquangcao.add(R.drawable.img_slide_3);

        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(mangquangcao.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_rigth);

        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_logo_out) {
            handleLogout();
        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(getApplicationContext(), ListOrderActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_post) {
            Intent intent = new Intent(getApplicationContext(), PostActitivy.class);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer((GravityCompat.START));
        } else {
            super.onBackPressed();
        }
    }

    private void handleLogout() {
        // Xóa trạng thái đăng nhập và thông tin người dùng
        clearUserInfo();
        saveLoginState(false);

        // Quay lại màn hình đăng nhập
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Phương thức để xóa thông tin người dùng
    private void clearUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Phương thức để cập nhật trạng thái đăng nhập
    private void saveLoginState(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private void getProduct() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ProductResponse> call = apiService.getProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //List<Product> productList = response.body().getProduct();
                    Utils.productList = response.body().getProduct();
                    displayProducts(Utils.productList);
                } else {
                    Toast.makeText(MainActivity.this, "Không thể lấy danh sách sản phẩm", Toast.LENGTH_SHORT).show();
                    // Log chi tiết lỗi nếu cần
                    Log.e("API_ERROR", "Response Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });
    }

    private void displayProducts(List<Product> productList) {
        sanPhamHomeAdapter = new SanPhamHomeAdapter(this, productList);
        rcv_list_item.setAdapter(sanPhamHomeAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rcv_list_item.setLayoutManager(layoutManager);
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        rcv_list_item.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));
    }
}
