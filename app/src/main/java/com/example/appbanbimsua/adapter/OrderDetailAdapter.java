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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanbimsua.R;
import com.example.appbanbimsua.activity.OrderDetailActivity;
import com.example.appbanbimsua.activity.ProductDetailActivity;
import com.example.appbanbimsua.enitities.ProductCart;
import com.example.appbanbimsua.enitities.response.OrderList;
import com.example.appbanbimsua.fragment.CanceledFragment;
import com.example.appbanbimsua.fragment.DeliveredFragment;
import com.example.appbanbimsua.fragment.InDeliveryFragment;
import com.example.appbanbimsua.fragment.WaitingPickupFragment;
import com.example.appbanbimsua.utils.Utils;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private Context context;
    private List<ProductCart> productList;
    public Context getContext() {
        return context;
    }
    public List<ProductCart> getProductList() {
        return productList;
    }

    public OrderDetailAdapter(Context context, List<ProductCart> productList) {
        this.context = context;
        this.productList = productList;
    }
    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        ProductCart product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.format("%,d đ", product.getPrice()));
        holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
        String img = Utils.BASE_URL + product.getImages().get(0);

        Glide.with(holder.itemView.getContext())
                .load(img)
                .error(R.drawable.ic_home)
                .override(holder.imgProduct.getWidth(), holder.imgProduct.getHeight())
                .centerCrop()
                .into(holder.imgProduct);

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

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice, tvQuantity;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
}
