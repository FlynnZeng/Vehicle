package com.me.vehicle.ui.carCost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.adapter.CostAdapter;
import com.me.vehicle.api.AjaxResult;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentCarCostBinding;
import com.me.vehicle.model.CostStatistics;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;
import com.me.vehicle.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarCostFragment extends Fragment {
    private Services services;
    private FragmentCarCostBinding binding;
    private Vehicle car;
    private CostAdapter adapter;
    private List<CostStatistics> costList;

    public static CarCostFragment newInstance() {
        return new CarCostFragment();
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
        binding = FragmentCarCostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        Intent intent = requireActivity().getIntent();
        car = (Vehicle) intent.getSerializableExtra("car");

        costList = new ArrayList<>();
        adapter = new CostAdapter(requireContext(), costList);
        binding.costList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.costList.setAdapter(adapter);

        initData();
        init();
        getCount();
        return root;
    }

    private void initData() {
        binding.mileage.setText(String.format("%d km", car.getMileage()));
    }

    private void init() {
        services.getStatistics(car.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CostStatistics>>> call, Response<ApiResponse<List<CostStatistics>>> response) {
                ApiResponse<List<CostStatistics>> body = response.body();
                if (body != null && body.getCode() == 200) {
                    List<CostStatistics> rows = body.getRows();

                    double fuelTotal = 0.0;
                    double oilTotal = 0.0;

                    for (CostStatistics item : rows) {
                        if ("加油费".equals(item.getCostType())) {
                            fuelTotal += item.getAmount().doubleValue();
                        } else if ("维修保养".equals(item.getCostType())) {
                            oilTotal += item.getAmount().doubleValue();
                        }
                    }
                    binding.fuel.setText(String.format(Locale.CHINA, "¥%.2f", fuelTotal));
                    binding.service.setText(String.format(Locale.CHINA, "¥%.2f", oilTotal));

                    costList.clear();
                    costList.addAll(rows);
                    adapter.notifyDataSetChanged();
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "获取列表失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CostStatistics>>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }

    private void getCount(){
        services.getCount(car.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    binding.count.setText(String.format("%s 次", body.getData()));
                }else{
                    binding.count.setText(String.format("%s 次", 0));
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }
}