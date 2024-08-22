package com.example.appbanbimsua.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.Product;
import com.example.appbanbimsua.enitities.ProductCart;
import com.example.appbanbimsua.enitities.response.OrderList;
import com.example.appbanbimsua.enitities.response.UserResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {
    public static final String BASE_URL = "http://172.16.82.112:9999";  //  192.168.0.102 192.168.1.33
    public static final String PREF_USER_INFO = "UserInfo";
    public static final String KEY_USER_INFO = "userInfo";
    public static final String KEY_USER_ID = "id";
    public static final String PREF_LOGIN = "LoginPrefs";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static  List<Product> productList ;
    public static  List<ProductCart> productCart;

    public static UserResponse getUserInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_USER_INFO, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(KEY_USER_INFO, null);
        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, UserResponse.class);
        }
        return null; // Hoặc trả về một đối tượng mặc định nếu không tìm thấy
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static int getCurrentUserId(Context context) {
        if (context == null) {
            return -1;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_USER_INFO, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_USER_ID, -1);
    }
    public static void fetchOrders(Context context, long userId, int status, final OrderCallback callback) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<OrderList>> call = apiService.getOrders(userId, status);
        call.enqueue(new Callback<List<OrderList>>() {
            @Override
            public void onResponse(Call<List<OrderList>> call, Response<List<OrderList>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response not successful"));
                }
            }

            @Override
            public void onFailure(Call<List<OrderList>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    public interface OrderCallback {
        void onSuccess(List<OrderList> orders);
        void onError(Throwable throwable);
    }

}