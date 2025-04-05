package com.me.vehicle.ui.carInfo;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.R;
import com.me.vehicle.databinding.FragmentCarInfoBinding;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.utils.DateUtils;
import com.me.vehicle.utils.Https;
import com.me.vehicle.utils.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class CarInfoFragment extends Fragment {

    private CarInfoViewModel mViewModel;
    private FragmentCarInfoBinding binding;

    public static CarInfoFragment newInstance() {
        return new CarInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CarInfoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCarInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        initData();


        return root;
    }

    private void initData(){
        Intent intent = requireActivity().getIntent();
        Vehicle carInfo = (Vehicle) intent.getSerializableExtra("carInfo");
        if (carInfo != null) {
            ImageLoader.loadImage(binding.carCover, Https.BASEURL+carInfo.getCover());
            binding.carNum.setText(carInfo.getPlateNumber());
            binding.carType.setText(String.format("%s | %s", carInfo.getBrand(),carInfo.getColor()));
            binding.carClass.setText(carInfo.getCarType());
            binding.mileage.setText(String.format("%d km", carInfo.getMileage()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

            binding.purchaseDate.setText(sdf.format(carInfo.getPurchaseDate()));
            binding.last.setText(sdf.format(carInfo.getLastMaintenance()));
            binding.expiry.setText(sdf.format(carInfo.getInsuranceExpiry()));
        }
    }

}