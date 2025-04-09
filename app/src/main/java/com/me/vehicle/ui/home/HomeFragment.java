package com.me.vehicle.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.me.vehicle.databinding.FragmentHomeBinding;
import com.me.vehicle.ui.carDispatch.DispatchActivity;
import com.me.vehicle.ui.carList.CarListActivity;
import com.me.vehicle.ui.carLocale.CarLocaleActivity;
import com.me.vehicle.ui.carRecord.CarRecordActivity;
import com.me.vehicle.ui.carReq.CarReqActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        binding.carReq.setOnClickListener(v->{
            Intent intent = new Intent(requireActivity(), CarReqActivity.class);
            startActivity(intent);
        });

        binding.carInfo.setOnClickListener(v->{
            Intent intent = new Intent(requireActivity(), CarListActivity.class);
            startActivity(intent);
        });

        binding.dispatch.setOnClickListener(v-> {
            Intent intent = new Intent(requireActivity(), DispatchActivity.class);
            startActivity(intent);
        });

        binding.record.setOnClickListener(v-> {
            Intent intent = new Intent(requireActivity(), CarRecordActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}