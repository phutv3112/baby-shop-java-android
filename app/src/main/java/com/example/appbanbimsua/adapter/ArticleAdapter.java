package com.example.appbanbimsua.adapter;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanbimsua.R;
import com.example.appbanbimsua.activity.PostDetailActivity;
import com.example.appbanbimsua.enitities.response.Article;
import com.example.appbanbimsua.utils.Utils;


import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> articles;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.contentTextView.setText(Html.fromHtml(article.getContent(), Html.FROM_HTML_MODE_LEGACY));
        holder.createdAtTextView.setText(article.getCreatedAt());

        String img = Utils.BASE_URL + article.getThumbnail();
        Glide.with(holder.itemView.getContext())
                .load(img)
                .into(holder.thumbnailImageView);

        // Set onClickListener for the itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), PostDetailActivity.class);
                intent.putExtra("ARTICLE_EXTRA", article); // Pass the Article object
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView createdAtTextView;
        ImageView thumbnailImageView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_title);
            contentTextView = itemView.findViewById(R.id.tv_content);
            createdAtTextView = itemView.findViewById(R.id.createdAt);
            thumbnailImageView = itemView.findViewById(R.id.img_thumbnail);
        }
    }
}
