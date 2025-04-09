package com.me.vehicle.ui.complete;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.R;
import com.me.vehicle.api.AjaxResult;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentCompleteBinding;
import com.me.vehicle.model.CarReturnInfo;
import com.me.vehicle.model.CostStatistics;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;
import com.me.vehicle.utils.PreferencesUtil;
import com.me.vehicle.utils.ToastUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CompleteFragment extends Fragment {

    private FragmentCompleteBinding binding;

    private Long carId, useId;

    private List<CostStatistics> costList;


    private Services services;

    public static CompleteFragment newInstance() {
        return new CompleteFragment();
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
        binding = FragmentCompleteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        Intent intent = requireActivity().getIntent();
        VehicleUse item = (VehicleUse) intent.getSerializableExtra("item");
        assert item != null;
        carId = item.getVehicleId();
        useId = item.getId();

        binding.checkboxFuel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.editFuelAmount.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });
        binding.checkboxMaintenance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.editMaintenanceAmount.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        binding.btnSubmit.setOnClickListener(v->submitReturn());
        return root;
    }

    public CarReturnInfo collageData()  {
        double mileage = Double.parseDouble(binding.editMileage.getText().toString().trim());
        double fuelCost = binding.checkboxFuel.isChecked() ?
                Double.parseDouble(binding.editFuelAmount.getText().toString().trim()) : 0.0;
        double maintenanceCost = binding.checkboxMaintenance.isChecked() ?
                Double.parseDouble(binding.editMaintenanceAmount.getText().toString().trim()) : 0.0;

        costList = new ArrayList<>();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());

        if (fuelCost > 0) {
            CostStatistics fuel = new CostStatistics();
            fuel.setVehicleId(carId);
            fuel.setCostDate(dateStr);
            fuel.setCostType("加油费");
            fuel.setDescription("归还车辆录入");
            fuel.setAmount(BigDecimal.valueOf(fuelCost));
            costList.add(fuel);
        }

        if (maintenanceCost > 0) {
            CostStatistics maintenance = new CostStatistics();
            maintenance.setVehicleId(carId);
            maintenance.setCostDate(dateStr);
            maintenance.setCostType("维修保养");
            maintenance.setDescription("归还车辆录入");
            maintenance.setAmount(BigDecimal.valueOf(maintenanceCost));
            costList.add(maintenance);
        }

        return new CarReturnInfo(carId, useId, mileage);
    }

    public void submitReturn(){
        CarReturnInfo info = collageData();

        addMileage(info);
        services.addExpenses(costList).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body == null || body.getCode() != 200) {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "还车失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }else{
                    ToastUtil.showToast(requireActivity(), "还车成功");
                    requireActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(requireContext(), "网络错误，请稍后重试");
            }
        });
    }

    private void addMileage(CarReturnInfo info){
        services.addMileage(info).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body == null || body.getCode() != 200) {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "还车失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(requireContext(), "网络错误，请稍后重试");
            }
        });
    }
}