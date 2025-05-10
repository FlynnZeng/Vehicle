package com.me.vehicle.ui.carShootRes;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.MainActivity;
import com.me.vehicle.databinding.FragmentCarShootResBinding;
import com.me.vehicle.ui.carRecord.CarRecordActivity;

public class CarShootResFragment extends Fragment {

    private FragmentCarShootResBinding binding;

    public static CarShootResFragment newInstance() {
        return new CarShootResFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCarShootResBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        init();

        binding.btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        binding.btnViewRecords.setOnClickListener(v->{
            Intent intent = new Intent(requireActivity(), CarRecordActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return root;
    }

    private void init() {
        Intent intent = requireActivity().getIntent();
        String useInfo = intent.getStringExtra("use_info");

        binding.useInfo.setText(useInfo);
    }

}