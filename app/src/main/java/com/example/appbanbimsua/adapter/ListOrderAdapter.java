package com.example.appbanbimsua.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.activity.OrderDetailActivity;
import com.example.appbanbimsua.enitities.response.OrderList;
import com.example.appbanbimsua.fragment.CanceledFragment;
import com.example.appbanbimsua.fragment.DeliveredFragment;
import com.example.appbanbimsua.fragment.InDeliveryFragment;
import com.example.appbanbimsua.fragment.ReturnedFragment;
import com.example.appbanbimsua.fragment.WaitingPickupFragment;

import java.util.List;

public class ListOrderAdapter extends RecyclerView.Adapter<ListOrderAdapter.OrderListViewHolder> {
    private Context context;
    private Fragment fragment;
    private List<OrderList> orders;

    public ListOrderAdapter(Context context, List<OrderList> orders, Fragment fragment) {
        this.context = context;
        this.orders = orders;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListViewHolder holder, int position) {
        OrderList order = orders.get(position);
        holder.billCodeTextView.setText("Mã đơn hàng: "+order.getBillCode());
        holder.quantityTextView.setText("Số lượng: "+String.valueOf(order.getQuantity()));
        holder.totalPriceTextView.setText(String.format("Tổng tiền: %,d đ", order.getTotalPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                // Truyền giá trị key
                if (fragment instanceof WaitingPickupFragment) {
                    intent.putExtra("key", 1);
                } else if (fragment instanceof InDeliveryFragment){
                    intent.putExtra("key", 2);
                }else if (fragment instanceof DeliveredFragment) {
                    intent.putExtra("key", 3);
                } else if (fragment instanceof ReturnedFragment) {
                    intent.putExtra("key", 4);
                }else if (fragment instanceof CanceledFragment) {
                    intent.putExtra("key", 5);
                }
                intent.putExtra("billCode", order.getBillCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {
        TextView billCodeTextView;
        TextView quantityTextView;
        TextView totalPriceTextView;

        public OrderListViewHolder(@NonNull View itemView) {
            super(itemView);
            billCodeTextView = itemView.findViewById(R.id.tv_bill_name);
            quantityTextView = itemView.findViewById(R.id.tv_quantity);
            totalPriceTextView = itemView.findViewById(R.id.tv_tong_tien);
        }
    }
}
