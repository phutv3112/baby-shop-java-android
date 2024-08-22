package com.example.appbanbimsua.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.adapter.ProductNameAdapter;
import com.example.appbanbimsua.adapter.SanPhamHomeAdapter;
import com.example.appbanbimsua.enitities.Product;
import com.example.appbanbimsua.utils.GridSpacingItemDecoration;
import com.example.appbanbimsua.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductNameAdapter productNameAdapter;
    private List<Product> filteredProductList;
    private List<Product> displayedProductList;
    private Button btnShowMore;
    private boolean isShowingAll = false;
    private RecyclerView rcv_list_item;
    private SanPhamHomeAdapter sanPhamHomeAdapter;
    private TextView sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.rcv_name);
        rcv_list_item = findViewById(R.id.rcv_list_item);
        sp = findViewById(R.id.tv_sp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filteredProductList = new ArrayList<>();
        displayedProductList = new ArrayList<>();
        productNameAdapter = new ProductNameAdapter(this, displayedProductList);
        recyclerView.setAdapter(productNameAdapter);

        btnShowMore = findViewById(R.id.btn_show_more);
        btnShowMore.setOnClickListener(v -> showAllProducts());

        EditText searchEditText = findViewById(R.id.searchEditText);
        displayProducts(Utils.productList);
        toggleRecyclerViews(false);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProductList(s.toString());
                toggleRecyclerViews(!s.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private static String removeAccents(String input) {
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private void filterProductList(String query) {
        filteredProductList.clear();
        displayedProductList.clear();
        isShowingAll = false;

        if (query.trim().isEmpty()) {
            updateProductList();
            displayProducts(Utils.productList);
            return;
        }

//        String[] queryParts = query.toLowerCase().split("\\s+");
//        for (Product product : Utils.productList) {
//            boolean matches = true;
//            String productName = product.getName().toLowerCase();
//
//            for (String part : queryParts) {
//                if (!productName.contains(part)) {
//                    matches = false;
//                    break;
//                }
//            }
//
//            if (matches) {
//                filteredProductList.add(product);
//            }
//        }

        String queryLower = removeAccents(query.toLowerCase().trim());

        for (Product product : Utils.productList) {
            String productName = removeAccents(product.getName().toLowerCase());

            if (productName.contains(queryLower)) {
                filteredProductList.add(product);
            }
        }


        updateProductList();
        displayProducts(filteredProductList);
    }

    private void updateProductList() {
        displayedProductList.clear();
        if (filteredProductList.size() <= 4 || isShowingAll) {
            displayedProductList.addAll(filteredProductList);
            btnShowMore.setVisibility(View.GONE);
        } else {
            displayedProductList.addAll(filteredProductList.subList(0, 4));
            btnShowMore.setVisibility(View.VISIBLE);
        }
        productNameAdapter.notifyDataSetChanged();
    }

    private void showAllProducts() {
        isShowingAll = true;
        updateProductList();
    }

    private void displayProducts(List<Product> productList) {
        sanPhamHomeAdapter = new SanPhamHomeAdapter(this, productList);
        rcv_list_item.setAdapter(sanPhamHomeAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rcv_list_item.setLayoutManager(layoutManager);
        rcv_list_item.addItemDecoration(new GridSpacingItemDecoration(2, 0, true));
    }
    private void toggleRecyclerViews(boolean showNameList) {
        if (showNameList) {
            recyclerView.setVisibility(View.VISIBLE);
            btnShowMore.setVisibility(View.VISIBLE);
            sp.setVisibility(View.GONE);
            rcv_list_item.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            btnShowMore.setVisibility(View.GONE);
            sp.setVisibility(View.VISIBLE);
            rcv_list_item.setVisibility(View.VISIBLE);
        }
    }
}