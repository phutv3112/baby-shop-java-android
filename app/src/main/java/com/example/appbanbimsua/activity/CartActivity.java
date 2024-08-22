package com.example.appbanbimsua.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.adapter.CartAdapter;
import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.ProductCart;
import com.example.appbanbimsua.enitities.response.CartResponse;
import com.example.appbanbimsua.enitities.response.UserResponse;
import com.example.appbanbimsua.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rcvCart;
    private CartAdapter cartAdapter;
    private ImageView img_back;
    private TextView txtTongTien, tvEmptyCart;
    private Button btnAddToCart, btnClose;
    private final BroadcastReceiver cartUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utils.isLoggedIn(CartActivity.this)) {
                UserResponse userResponse = Utils.getUserInfo(CartActivity.this);
                getCartByUserId((long) userResponse.getId());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupRecyclerView();

        if (Utils.isLoggedIn(this)) {
            UserResponse userResponse = Utils.getUserInfo(this);
            getCartByUserId((long) userResponse.getId());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(cartUpdateReceiver, new IntentFilter("UPDATE_CART"));
        setupListeners();
    }

    private void initViews() {
        rcvCart = findViewById(R.id.rcv_cart);
        txtTongTien = findViewById(R.id.txt_tongtien);
        tvEmptyCart = findViewById(R.id.tv_empty_cart);
        btnAddToCart = findViewById(R.id.add_to_cart_button);
        btnClose = findViewById(R.id.btn_close);
        img_back = findViewById(R.id.img_back);
    }

    private void setupRecyclerView() {
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ProductCart product = cartAdapter.getProductList().get(position);
                removeCartItem((long) Utils.getUserInfo(CartActivity.this).getId(), product.getId());
                cartAdapter.removeItem(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvCart);
    }

    private void setupListeners() {
        btnAddToCart.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        btnClose.setOnClickListener(v -> onBackPressed());
        img_back.setOnClickListener(v -> onBackPressed());
    }

    private void getCartByUserId(Long userId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<CartResponse> call = apiService.getCartByUserId(userId);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductCart> productList = response.body().getProducts();
                    updateUI(productList);
                } else {
                    Log.e("API_RESPONSE", "Response không thành công");
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void updateUI(List<ProductCart> productList) {
        if (productList.isEmpty()) {
            rcvCart.setVisibility(View.GONE);
            txtTongTien.setVisibility(View.GONE);
            btnAddToCart.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
        } else {
            cartAdapter = new CartAdapter(this, productList);
            rcvCart.setAdapter(cartAdapter);
            Utils.productCart = productList;
            long totalAmount = 0;
            for (ProductCart product : productList) {
                totalAmount += product.getTotalMoney();
            }
            txtTongTien.setText(String.format("Tổng tiền: %,d đ", totalAmount));
            rcvCart.setVisibility(View.VISIBLE);
            txtTongTien.setVisibility(View.VISIBLE);
            btnAddToCart.setVisibility(View.VISIBLE);
            btnClose.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);
        }
    }

    private void removeCartItem(Long userId, String productId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<CartResponse> call = apiService.removeCartItem(userId, productId);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                progressDialog.dismiss();
                getCartByUserId(userId);
                Toast.makeText(CartActivity.this, "Sản phẩm đã được xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CartActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(cartUpdateReceiver);
    }
}
