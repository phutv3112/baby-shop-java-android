package com.example.appbanbimsua.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.adapter.CartAdapter;
import com.example.appbanbimsua.adapter.OrderDetailAdapter;
import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.Product;
import com.example.appbanbimsua.enitities.ProductCart;
import com.example.appbanbimsua.enitities.response.CartResponse;
import com.example.appbanbimsua.enitities.response.OrderDetailResponse;
import com.example.appbanbimsua.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerViewOrderDetails;
    private OrderDetailAdapter orderDetailAdapter;
    private List<ProductCart> productCartList;
    private List<ProductCart> matchingProducts = new ArrayList<>();
    private ProductCart productCart = new ProductCart();
    private String billCode = "";
    private int key = 0;
    private TextView tv_name, tv_phone, tv_address, txt_tongtien, tv_billCode, tv_gach;
    private TextView tv_note1, tv_order_status;
    private Button btn_close;
    private EditText tv_note;
    private Button add_to_cart_button;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);
        key = getIntent().getIntExtra("key", -1);
        billCode = getIntent().getStringExtra("billCode");
        initView();
        getOrderDetail(billCode);
        if (key == 3 || key == 4 || key == 5) {
            add_to_cart_button.setVisibility(View.GONE);
        }
        tv_note.setEnabled(false);
        initStatus();
        initListen();
    }

    private void initView() {
        recyclerViewOrderDetails = findViewById(R.id.recyclerViewOrderDetails);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_gach = findViewById(R.id.tv_gach);
        tv_address = findViewById(R.id.tv_address);
        txt_tongtien = findViewById(R.id.txt_tongtien);
        btn_close = findViewById(R.id.btn_close);
        add_to_cart_button = findViewById(R.id.add_to_cart_button);
        tv_billCode = findViewById(R.id.tv_billCode);
        tv_note = findViewById(R.id.tv_note);
        tv_note1 = findViewById(R.id.tv_note1);
        img_back = findViewById(R.id.img_back);
        tv_order_status = findViewById(R.id.tv_order_status);
    }

    private void initListen() {
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
        add_to_cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(billCode,5);
            }
        });
    }

    private void initStatus() {
        if (key == 1) {
            tv_order_status.setText("Chờ lấy hàng");
        } else if (key == 2) {
            tv_order_status.setText("Đang giao hàng");
        } else if (key == 3) {
            tv_order_status.setText("Giao hàng thành công");
        } else if (key == 4) {
            tv_order_status.setText("Đã trả lại hàng");
        } else if (key == 5) {
            tv_order_status.setText("Đã hủy");
        }
    }

    private void getOrderDetail(String billCode) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<OrderDetailResponse>> call = apiService.getOrderDetail(billCode);
        call.enqueue(new Callback<List<OrderDetailResponse>>() {
            @Override
            public void onResponse(Call<List<OrderDetailResponse>> call, Response<List<OrderDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderDetailResponse> orderDetailResponses = response.body();
                    Map<String, ProductCart> productMap = new HashMap<>();
                    for (OrderDetailResponse detailResponse : orderDetailResponses) {
                        for (Product product : Utils.productList) {
                            if (product.getId().equals(detailResponse.getProductId())) {
                                if (productMap.containsKey(product.getId())) {
                                    ProductCart existingProductCart = productMap.get(product.getId());
                                    existingProductCart.setQuantity(existingProductCart.getQuantity() + detailResponse.getQuantity());
                                } else {
                                    ProductCart newProductCart = new ProductCart();
                                    newProductCart.setName(product.getName());
                                    newProductCart.setSlug(product.getSlug());
                                    newProductCart.setId(product.getId());
                                    newProductCart.setPrice((int) product.getPrice());
                                    newProductCart.setImages(Collections.singletonList(product.getImages()));
                                    newProductCart.setQuantity(detailResponse.getQuantity());
                                    productMap.put(product.getId(), newProductCart);
                                }
                            }
                        }
                    }
                    matchingProducts = new ArrayList<>(productMap.values());
                    tv_billCode.setText(String.format("Mã đơn hàng: %s", billCode));
                    if (!orderDetailResponses.isEmpty()) {
                        if (orderDetailResponses.get(0).getReceiverName() == null || orderDetailResponses.get(0).getReceiverName().isEmpty()
                                || orderDetailResponses.get(0).getReceiverPhone() == null
                                || orderDetailResponses.get(0).getReceiverPhone().isEmpty()) {
                            tv_gach.setVisibility(View.GONE);
                        }
                        tv_name.setText(orderDetailResponses.get(0).getReceiverName());
                        tv_phone.setText(orderDetailResponses.get(0).getReceiverPhone());
                        tv_address.setText(orderDetailResponses.get(0).getReceiverAddress());
                        txt_tongtien.setText(String.format("Tổng tiền: %,d đ", orderDetailResponses.get(0).getPrices()));
                        if (orderDetailResponses.get(0).getNote() != null && !orderDetailResponses.get(0).getNote().isEmpty()) {
                            tv_note.setVisibility(View.VISIBLE);
                            tv_note1.setVisibility(View.VISIBLE);
                            tv_note.setText(orderDetailResponses.get(0).getNote());
                        }
                    }
                    recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
                    orderDetailAdapter = new OrderDetailAdapter(OrderDetailActivity.this, matchingProducts);
                    recyclerViewOrderDetails.setAdapter(orderDetailAdapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<OrderDetailResponse>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
    private void updateStatus(String billCode, int status) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateStatus(billCode, status);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseBody = response.body().string();
                        Log.d("API_RESPONSE", responseBody);
                        showDialog("Thông báo", "Hủy đơn hàng thành công");
                    } else {
                        showDialog("Thông báo", "Thất bại");
                    }
                } catch (IOException e) {
                    showDialog("Thông báo", "Lỗi: " + e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("API_RESPONSE", t.getMessage());
                showDialog("Thông báo", "Lỗi: " + t.getMessage());
            }

        });
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
