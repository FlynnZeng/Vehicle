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
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.R;
import com.me.vehicle.adapter.CarUseAdapter;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentCarInfoBinding;
import com.me.vehicle.model.Users;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;
import com.me.vehicle.utils.DateUtils;
import com.me.vehicle.utils.Https;
import com.me.vehicle.utils.ImageLoader;
import com.me.vehicle.utils.PreferencesUtil;
import com.me.vehicle.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CarInfoFragment extends Fragment {

    private FragmentCarInfoBinding binding;
    private Services services;
    private Long carId;
    private List<VehicleUse> useList;

    private CarUseAdapter carUseAdapter;

    public static CarInfoFragment newInstance() {
        return new CarInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit client = RetrofitClient.getClient(requireActivity());
        services = client.create(Services.class);
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

        useList = new ArrayList<>();

        carUseAdapter = new CarUseAdapter(useList, null);
        binding.carUseList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.carUseList.setAdapter(carUseAdapter);
        getUseList();

        return root;
    }

    private void initData(){
        Intent intent = requireActivity().getIntent();
        Vehicle carInfo = (Vehicle) intent.getSerializableExtra("carInfo");
        if (carInfo != null) {
            carId = carInfo.getId();
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

    private void getUseList(){
        services.getCarUseRecords(carId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<List<VehicleUse>>> call, Response<ApiResponse<List<VehicleUse>>> response) {
                ApiResponse<List<VehicleUse>> body = response.body();
                if (body != null && body.getCode() == 200) {
                    List<VehicleUse> rows = body.getRows();
                    useList.clear();
                    useList.addAll(rows);
                    carUseAdapter.notifyDataSetChanged();
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "获取列表失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<VehicleUse>>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }
}