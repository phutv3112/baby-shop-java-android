package com.example.appbanbimsua.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.adapter.CartAdapter;
import com.example.appbanbimsua.adapter.OrderAdapter;
import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.ProductCart;
import com.example.appbanbimsua.enitities.ProductOrder;
import com.example.appbanbimsua.enitities.request.OrderRequest;
import com.example.appbanbimsua.enitities.response.CartResponse;
import com.example.appbanbimsua.fragment.ReceiverInfoFragment;
import com.example.appbanbimsua.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements ReceiverInfoFragment.OnReceiverInfoSavedListener {

    private TextView tvName, tvPhone, tvAddress, tvNoInfo;
    private LinearLayout layoutInfo;
    private Button btn_close;
    private Button add_to_cart_button;
    private RecyclerView rcv_cart;
    private OrderAdapter cartAdapter;
    private TextView txt_tongtien;
    private EditText edt_note;
    private List<ProductOrder> products = new ArrayList<>();
    private long totalAmount = 0;
    private ProgressDialog progressDialog ;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initUI();
        loadReceiverInfo();
        initListen();
        loadCart();
    }

    private void initUI(){
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvNoInfo = findViewById(R.id.tv_no_info);
        layoutInfo = findViewById(R.id.layout_info);
        btn_close = findViewById(R.id.btn_close);
        add_to_cart_button = findViewById(R.id.add_to_cart_button);
        rcv_cart = findViewById(R.id.rcv_cart);
        txt_tongtien = findViewById(R.id.txt_tongtien);
        edt_note = findViewById(R.id.edt_note);
        img_back = findViewById(R.id.img_back);
        progressDialog = new ProgressDialog(this);
    }
    private void loadCart() {
        cartAdapter = new OrderAdapter(OrderActivity.this, Utils.productCart);
        rcv_cart.setLayoutManager(new LinearLayoutManager(this));
        rcv_cart.setAdapter(cartAdapter);


        for (ProductCart product : Utils.productCart) {
            totalAmount += product.getTotalMoney();
        }
        txt_tongtien.setText(String.format("Tổng tiền: %,d đ", totalAmount));
    }
    private void initListen(){
        tvNoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReceiverInfoFragment();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        layoutInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openReceiverInfoFragment();
            }
        });
        add_to_cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvNoInfo.getVisibility() == View.VISIBLE) {
                    Toast.makeText(OrderActivity.this, "Vui lòng nhập thông tin nhận hàng", Toast.LENGTH_SHORT).show();
                } else {
                    // Thực hiện đặt hàng
                    removeCartItemsByUserId((long) Utils.getUserInfo(OrderActivity.this).getId());
                }
            }
        });

    }
    private void loadReceiverInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("ReceiverInfoPrefs", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        String phone = sharedPreferences.getString("phone", null);
        String address = sharedPreferences.getString("address", null);

        if (name == null || phone == null || address == null) {
            // No info available
            tvNoInfo.setVisibility(View.VISIBLE);
            layoutInfo.setVisibility(View.GONE);
        } else {
            // Display info
            tvNoInfo.setVisibility(View.GONE);
            layoutInfo.setVisibility(View.VISIBLE);
            tvName.setText(name);
            tvPhone.setText(phone);
            tvAddress.setText(address);
        }
    }

    private void openReceiverInfoFragment() {
        ReceiverInfoFragment receiverInfoFragment = new ReceiverInfoFragment();
        receiverInfoFragment.show(getSupportFragmentManager(), "ReceiverInfoFragment");
    }

    @Override
    public void onReceiverInfoSaved() {
        loadReceiverInfo();
    }
    private void createOrder() {
        showProgress();
        for (ProductCart productCart : Utils.productCart) {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setProductId(productCart.getId());
            productOrder.setSize(1);
            productOrder.setQuantity(String.valueOf(productCart.getQuantity()));

            products.add(productOrder);
        }
        OrderRequest orderRequest = new OrderRequest(products,tvName.getText().toString(),
                tvPhone.getText().toString(),tvAddress.getText().toString(),
                "XXX",(int) totalAmount,0,
                edt_note.getText().toString(),
                Utils.getUserInfo(this).getId());
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Integer>> call = apiService.createOrder(orderRequest);
        call.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                progressDialog.dismiss();
                showSuccessDialog();
            }
            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("OrderActivity", "API call failed: " + t.getMessage());
            }
        });
    }
    private void removeCartItemsByUserId(Long userId) {
        showProgress();
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<CartResponse> call = apiService.removeCartItemsByUserId(userId);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                progressDialog.dismiss();
                createOrder();
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thành công")
                .setMessage("Đặt hàng thành công")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                    Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                    startActivity(intent);
                })
                .show();
    }

    private void showProgress(){
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}
