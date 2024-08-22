package com.example.appbanbimsua.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.adapter.ListOrderAdapter;
import com.example.appbanbimsua.enitities.response.OrderList;
import com.example.appbanbimsua.utils.Utils;

import java.util.List;

public class InDeliveryFragment extends Fragment {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ListOrderAdapter orderAdapter;
    private View view;
    private LinearLayout tv_empty_cart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_waiting_pickup, container, false);
        initUI();
        fetchOrders();
        return view;
    }

    private void initUI() {
        recyclerView = view.findViewById(R.id.recyclerViewWaitingPickup);
        tv_empty_cart = view.findViewById(R.id.tv_empty_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext());
    }

    private void fetchOrders() {
        showProgress();
        Utils.fetchOrders(getContext(), Utils.getUserInfo(getContext()).getId(), 2, new Utils.OrderCallback() {
            @Override
            public void onSuccess(List<OrderList> orders) {
                progressDialog.dismiss();
                if (orders != null && !orders.isEmpty()) {
                    tv_empty_cart.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    orderAdapter = new ListOrderAdapter(getContext(), orders, InDeliveryFragment.this);
                    recyclerView.setAdapter(orderAdapter);
                } else {
                    tv_empty_cart.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(Throwable throwable) {
                progressDialog.dismiss();
                // Xử lý lỗi
            }
        });
    }

    private void showProgress() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    public void onResume() {
        fetchOrders();
        super.onResume();
    }
}
