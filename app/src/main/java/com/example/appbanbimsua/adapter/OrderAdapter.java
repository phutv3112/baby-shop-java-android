package com.example.appbanbimsua.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanbimsua.R;
import com.example.appbanbimsua.activity.ProductDetailActivity;
import com.example.appbanbimsua.enitities.ProductCart;
import com.example.appbanbimsua.utils.Utils;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private Context context;
    private List<ProductCart> productList;
    public String total;

    public OrderAdapter(Context context, List<ProductCart> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new OrderAdapter.OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        ProductCart product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.format("%,d đ", product.getPrice()));
        holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
        String img = Utils.BASE_URL + product.getImages().get(0);

//        Glide.with(context)
//            .load(product.getImages().get(0))
//            .into(holder.imgProduct);
        Glide.with(holder.itemView.getContext())
                .load(img)
                .error(R.drawable.ic_home)
                .override(holder.imgProduct.getWidth(), holder.imgProduct.getHeight())
                .centerCrop() // scale to fit entire view
                .into(holder.imgProduct);
        holder.btnIncreaseQuantity.setVisibility(View.GONE);
        holder.btnDecreaseQuantity.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productSlug = product.getSlug();
                String productId = product.getId();
                //    getProductDetail(productSlug, productId);
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productSlug", productSlug);
                intent.putExtra("productId", productId);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class OrderHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice, tvQuantity;
        Button btnIncreaseQuantity, btnDecreaseQuantity;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnIncreaseQuantity = itemView.findViewById(R.id.btn_increase_quantity);
            btnDecreaseQuantity = itemView.findViewById(R.id.btn_decrease_quantity);
        }
    }
}

