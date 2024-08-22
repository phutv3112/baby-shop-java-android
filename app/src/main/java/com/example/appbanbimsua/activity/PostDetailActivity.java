package com.example.appbanbimsua.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appbanbimsua.R;
import com.example.appbanbimsua.enitities.response.Article;
import com.example.appbanbimsua.utils.Utils;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView imgThumbnail;
    private TextView tvTitle;
    private WebView webviewContent;
    private TextView tvCreatedAt;
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        imgThumbnail = findViewById(R.id.img_thumbnail);
        tvTitle = findViewById(R.id.tv_title);
        webviewContent = findViewById(R.id.webview_content);
        tvCreatedAt = findViewById(R.id.tv_createdAt);
        btn_back = findViewById(R.id.btn_back);

        // Retrieve the Article object from the Intent
        Article article = (Article) getIntent().getSerializableExtra("ARTICLE_EXTRA");

        if (article != null) {
            // Populate the views with the article data
            tvTitle.setText(article.getTitle());
            webviewContent.loadData(article.getDescription(), "text/html", "UTF-8");
            tvCreatedAt.setText("Ngày tạo: " + article.getCreatedAt());

            String imgUrl = Utils.BASE_URL + article.getThumbnail();
            Glide.with(this)
                    .load(imgUrl)
                    .into(imgThumbnail);
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
