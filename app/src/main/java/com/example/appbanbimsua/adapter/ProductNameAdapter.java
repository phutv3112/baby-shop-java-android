package com.example.appbanbimsua.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.activity.ProductDetailActivity;
import com.example.appbanbimsua.activity.SearchActivity;
import com.example.appbanbimsua.enitities.Product;


import java.util.List;

public class ProductNameAdapter extends RecyclerView.Adapter<ProductNameAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductNameAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_name_sreach, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productSlug = product.getSlug();
                String productId = product.getId();


                //    getProductDetail(productSlug, productId);

                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productSlug", productSlug);
                intent.putExtra("productId", productId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Sử dụng FLAG_ACTIVITY_CLEAR_TOP cùng FLAG_ACTIVITY_SINGLE_TOP
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tv_product_name);
            productName.setPaintFlags(productName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        }
    }
}
