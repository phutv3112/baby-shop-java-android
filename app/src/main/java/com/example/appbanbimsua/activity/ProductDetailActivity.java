package com.example.appbanbimsua.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.example.appbanbimsua.R;
import com.example.appbanbimsua.adapter.CommentAdapter;
import com.example.appbanbimsua.adapter.ImagePagerAdapter;
import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.Comment;
import com.example.appbanbimsua.enitities.request.CommentRequest;
import com.example.appbanbimsua.enitities.response.ProductDetailResponse;
import com.example.appbanbimsua.enitities.response.ResponseOK;
import com.example.appbanbimsua.enitities.response.UserResponse;
import com.example.appbanbimsua.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ProductDetailResponse product = new ProductDetailResponse();
    private ViewPager viewPager;
    private TextView productNameTextView;
    private TextView productThuonghieu;
    private TextView productPriceTextView;
    private TextView productQuantityTextView;
    private Button addToCartButton;
    private Button buyNowButton;
    private WebView productDescriptionWebView;
    private RecyclerView recyclerViewComments;
    private EditText etUserComment;
    private Button btnSubmitComment;
    private CommentAdapter commentAdapter;
    private List<Comment> comments;
    private String productSlug;
    private String productId;
    private UserResponse userResponse = new UserResponse();
    private ImageView imgsearch;
    private FrameLayout framegiohang;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
//        product = (ProductDetailResponse) getIntent().getSerializableExtra("productResponse");
//        Log.d("productDetailcomment",product.toString());

        Intent intent = getIntent();
        productSlug = intent.getStringExtra("productSlug");
        productId = intent.getStringExtra("productId");

        if (productSlug == null || productId == null) {
            Toast.makeText(this, "Invalid product data", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc activity nếu dữ liệu không hợp lệ
            return;
        }

        initUI();
        getProductDetail(productSlug,productId);
        setUpListeners();

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Cập nhật Intent mới
        productSlug = intent.getStringExtra("productSlug");
        productId = intent.getStringExtra("productId");
        // Tải lại dữ liệu sản phẩm mới
        getProductDetail(productSlug, productId);
    }

    private void initUI() {
        viewPager = findViewById(R.id.viewpager);
        productNameTextView = findViewById(R.id.product_name);
        productThuonghieu = findViewById(R.id.product_thuonghieu);
        productPriceTextView = findViewById(R.id.product_price);
        productQuantityTextView = findViewById(R.id.tv_quantity);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        buyNowButton = findViewById(R.id.btn_buy);
        productDescriptionWebView = findViewById(R.id.product_description_webview);
        etUserComment = findViewById(R.id.et_user_comment);
        btnSubmitComment = findViewById(R.id.btn_submit_comment);
        imgsearch = findViewById(R.id.imgsearch);
        framegiohang = findViewById(R.id.framegiohang);
        img_back = findViewById(R.id.img_back);
    }

    private void populateData() {
        if (product != null) {
            productNameTextView.setText(product.getProduct().getName());
            productThuonghieu.setText(product.getProduct().getBrand().getName());
            productPriceTextView.setText(String.format("%d VND", product.getProduct().getPrice()));
            productQuantityTextView.setText(String.valueOf(product.getProduct().getQuantity()));
            productDescriptionWebView.loadData(product.getProduct().getDescription(), "text/html", "UTF-8");

            List<String> imageUrls = product.getProduct().getProductImages();
            if (imageUrls != null && !imageUrls.isEmpty()) {
                ImagePagerAdapter adapter = new ImagePagerAdapter(this, imageUrls);
                viewPager.setAdapter(adapter);
            }
        }
    }

    private void initCommentsRecyclerView() {
        recyclerViewComments = findViewById(R.id.rcv_comments);
        comments = new ArrayList<>(); // Fetch or add your comments here
        comments = product.getProduct().getComments();
        commentAdapter = new CommentAdapter(comments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComments.setAdapter(commentAdapter);
    }

    private void setUpListeners() {
        addToCartButton.setOnClickListener(v -> {
            if (productQuantityTextView.getText().equals("0")) {
                Toast.makeText(this, "Sản phẩm đã hết không thể thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                if (Utils.isLoggedIn(this)) {
                    userResponse = Utils.getUserInfo(this);
                    assert userResponse != null;
                    addOrUpdateCartItem((long) userResponse.getId(), productId);
                } else {
                    Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                }
            }
        });
        buyNowButton.setOnClickListener(v -> {
            if (productQuantityTextView.getText().equals("0")) {
                Toast.makeText(this, "Sản phẩm đã hết không thể thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                if (Utils.isLoggedIn(this)) {
                    userResponse = Utils.getUserInfo(this);
                    assert userResponse != null;
                    addOrUpdateCartItem((long) userResponse.getId(), productId);
                    Intent intent = new Intent(ProductDetailActivity.this, OrderActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Vui lòng đăng nhập để mua", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                }
            }
        });
        btnSubmitComment.setOnClickListener(v -> {
            String commentContent = etUserComment.getText().toString().trim();
            if (!commentContent.isEmpty()) {
                if (Utils.isLoggedIn(this)) {
                     userResponse = Utils.getUserInfo(this);
                     postComment(productId, commentContent, String.valueOf(userResponse.getId()));
                } else {
                    Toast.makeText(this, "Vui lòng đăng nhập để bình luận", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập bình luận", Toast.LENGTH_SHORT).show();
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        framegiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void getProductDetail(String tenSp, String id){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ProductDetailResponse> call = apiService.getProductDetails(tenSp, id);
        call.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                     product = response.body();
                     populateData();
                     initCommentsRecyclerView();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void postComment(String productId, String content, String userId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Tạo đối tượng bình luận để gửi đi
        CommentRequest commentRequest = new CommentRequest(productId, content, userId);

        Call<ResponseOK> call = apiService.postComment(commentRequest);
        call.enqueue(new Callback<ResponseOK>() {
            @Override
            public void onResponse(Call<ResponseOK> call, Response<ResponseOK> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    getProductDetail(productSlug, productId);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOK> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addOrUpdateCartItem(Long userId, String productId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang thêm vào giỏ hàng...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseOK> call = apiService.addOrUpdateCartItem(userId, productId);
        call.enqueue(new Callback<ResponseOK>() {
            @Override
            public void onResponse(Call<ResponseOK> call, Response<ResponseOK> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProductDetailActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không thể thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOK> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
