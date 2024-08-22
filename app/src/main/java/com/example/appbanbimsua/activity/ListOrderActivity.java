package com.example.appbanbimsua.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.appbanbimsua.R;
import com.example.appbanbimsua.fragment.CanceledFragment;
import com.example.appbanbimsua.fragment.DeliveredFragment;
import com.example.appbanbimsua.fragment.InDeliveryFragment;
import com.example.appbanbimsua.fragment.ReturnedFragment;
import com.example.appbanbimsua.fragment.WaitingPickupFragment;
import com.google.android.material.tabs.TabLayout;

public class ListOrderActivity extends AppCompatActivity {
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);

        // Thiết lập TabLayout và ViewPager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        img_back = findViewById(R.id.img_back);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // Thiết lập ViewPager Adapter
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new WaitingPickupFragment();
                    case 1:
                        return new InDeliveryFragment();
                    case 2:
                        return new DeliveredFragment();
                    case 3:
                        return new CanceledFragment();
                    case 4:
                        return new ReturnedFragment();
                    default:
                        return new WaitingPickupFragment();
                }
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Chờ lấy hàng";
                    case 1:
                        return "Đang giao hàng";
                    case 2:
                        return "Đã giao hàng";
                    case 3:
                        return "Đơn hàng đã hủy";
                    case 4:
                        return "Đơn hàng đã trả";
                }
                return null;
            }
        });

        // Liên kết TabLayout với ViewPager
        tabLayout.setupWithViewPager(viewPager);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
