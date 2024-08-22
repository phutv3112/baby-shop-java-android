package com.example.appbanbimsua.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.adapter.ArticleAdapter;
import com.example.appbanbimsua.api.ApiService;
import com.example.appbanbimsua.api.RetrofitClient;
import com.example.appbanbimsua.enitities.response.Article;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActitivy extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private ImageView btn_back;
    private List<Article> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_actitivy);

        recyclerView = findViewById(R.id.recycler_view);
        btn_back = findViewById(R.id.btn_back);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleAdapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(articleAdapter);

        fetchArticles();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fetchArticles() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Article>> call = apiService.getPosts();

        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    articleList.clear();
                    articleList.addAll(response.body());
                    articleList.sort(new Comparator<Article>() {
                        @Override
                        public int compare(Article a1, Article a2) {
                            return a2.getCreatedAt().compareTo(a1.getCreatedAt());
                        }
                    });

                    articleAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PostActitivy.this, "Failed to get articles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Toast.makeText(PostActitivy.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
