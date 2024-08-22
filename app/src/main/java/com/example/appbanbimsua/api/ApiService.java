package com.example.appbanbimsua.api;// ApiService.java
import com.example.appbanbimsua.enitities.request.ChangePasswordRequest;
import com.example.appbanbimsua.enitities.request.CommentRequest;
import com.example.appbanbimsua.enitities.request.LoginRequest;
import com.example.appbanbimsua.enitities.request.OrderRequest;
import com.example.appbanbimsua.enitities.request.SignUpRequest;
import com.example.appbanbimsua.enitities.request.UpdateProfileRequest;
import com.example.appbanbimsua.enitities.response.Article;
import com.example.appbanbimsua.enitities.response.CartResponse;
import com.example.appbanbimsua.enitities.response.OrderDetailResponse;
import com.example.appbanbimsua.enitities.response.OrderList;
import com.example.appbanbimsua.enitities.response.ProductDetailResponse;
import com.example.appbanbimsua.enitities.response.ProductResponse;
import com.example.appbanbimsua.enitities.response.Product_quantity;
import com.example.appbanbimsua.enitities.response.ResponseOK;
import com.example.appbanbimsua.enitities.response.SignUpResponse;
import com.example.appbanbimsua.enitities.response.UserResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/login")
    Call<UserResponse> login(@Body LoginRequest loginRequest);
    @POST("/api/register")
    Call<SignUpResponse> signUp(@Body SignUpRequest signUpRequest);
    @GET("/products")
    Call<ProductResponse> getProducts();
    @GET("/chitiet/{slug}/{id}")
    Call<ProductDetailResponse> getProductDetails(
            @Path("slug") String slug,
            @Path("id") String id
    );
    @POST("/api/comments/product")
    Call<ResponseOK> postComment(@Body CommentRequest commentRequest);
    @POST("/api/cart/addOrUpdateCart")
    Call<ResponseOK> addOrUpdateCartItem(
            @Query("userId") Long userId,
            @Query("productId") String productId
    );
    @PUT("/api/cart/decrease")
    Call<ResponseOK> decreaseCartItem(
            @Query("userId") Long userId,
            @Query("productId") String productId
    );
    @DELETE("/api/cart/remove")
    Call<CartResponse> removeCartItem(
            @Query("userId") Long userId,
            @Query("productId") String productId
    );
    @DELETE("/api/cart/removeCartItemsByUserId")
    Call<CartResponse> removeCartItemsByUserId(
            @Query("userId") Long userId
    );
    @GET("/api/cart/getCartByUserId")
    Call<CartResponse> getCartByUserId(
            @Query("userId") Long userId
    );
    @POST("/api/orders/v2")
    Call<List<Integer>> createOrder(@Body OrderRequest orderRequest);
    @GET("api/orders")
    Call<List<OrderList>> getOrders(
            @Query("buyer") long buyer,
            @Query("status") int status
    );
    @GET("api/orders/detail")
    Call<List<OrderDetailResponse>> getOrderDetail(
            @Query("billCode") String billCode
    );
    @POST("/api/update/status")
    Call<ResponseBody> updateStatus(
            @Query("billCode") String billCode,
            @Query("status") int status
    );
    @PUT("/api/update-profile/{userId}")
    Call<ResponseOK> updateProfile(
            @Path("userId") Long userId,
            @Body UpdateProfileRequest profileReq
    );
    @POST("/api/change-password/{userId}")
    Call<ResponseOK> changePassword(
            @Path("userId") Long userId,
            @Body ChangePasswordRequest passwordReq
    );
    @GET("/posts")
    Call<List<Article>> getPosts();
    @GET("/api/product-size")
    Call<List<Product_quantity>> getProductSizes(@Query("productId") String productId);
}
