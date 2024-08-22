package com.example.appbanbimsua.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.appbanbimsua.R;

public class ReceiverInfoFragment extends DialogFragment {

    private EditText edtName, edtPhone, edtAddress;
    private Button btnSubmit;

    public ReceiverInfoFragment() {
        // Required empty public constructor
    }
    public interface OnReceiverInfoSavedListener {
        void onReceiverInfoSaved();
    }
    private OnReceiverInfoSavedListener listener;
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReceiverInfoSavedListener) {
            listener = (OnReceiverInfoSavedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " phải implement OnReceiverInfoSavedListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receiver_info, container, false);

        edtName = view.findViewById(R.id.edt_name);
        edtPhone = view.findViewById(R.id.edt_phone);
        edtAddress = view.findViewById(R.id.edt_address);
        btnSubmit = view.findViewById(R.id.btn_submit);
        loadReceiverInfo();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {

                    Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 10) {

                    Toast.makeText(getActivity(), "Số điện thoại phải gồm 10 chữ số!", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ReceiverInfoPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("phone", phone);
                    editor.putString("address", address);
                    editor.apply();

                    if (listener != null) {
                        listener.onReceiverInfoSaved();
                    }
                    dismiss();
                }
            }
        });
        return view;
    }
    private void loadReceiverInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ReceiverInfoPrefs", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        String phone = sharedPreferences.getString("phone", null);
        String address = sharedPreferences.getString("address", null);

        if (name == null || phone == null || address == null) {
            return;
        } else {
            edtName.setText(name);
            edtPhone.setText(phone);
            edtAddress.setText(address);
        }
    }
}
