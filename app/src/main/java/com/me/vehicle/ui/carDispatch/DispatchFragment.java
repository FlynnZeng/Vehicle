package com.me.vehicle.ui.carDispatch;

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

import com.me.vehicle.adapter.CarUseAdapter;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentDispatchBinding;
import com.me.vehicle.model.VehicleUse;
import com.me.vehicle.ui.carShoot.CarShootActivity;
import com.me.vehicle.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DispatchFragment extends Fragment {

    private FragmentDispatchBinding binding;
    private Services services;
    private List<VehicleUse> useList;
    private CarUseAdapter carUseAdapter;

    public static DispatchFragment newInstance() {
        return new DispatchFragment();
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
        binding = FragmentDispatchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        useList = new ArrayList<>();

        carUseAdapter = new CarUseAdapter(useList, item->{
            Intent intent = new Intent(requireActivity(), CarShootActivity.class);
            intent.putExtra("carInfo", item);
            intent.putExtra("apply_id", item.getId());
            startActivity(intent);
        });
        binding.carWaitUse.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.carWaitUse.setAdapter(carUseAdapter);

        getUseList();

        return root;
    }

    private void getUseList(){
        services.getWaitUseList().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<List<VehicleUse>>> call, Response<ApiResponse<List<VehicleUse>>> response) {
                ApiResponse<List<VehicleUse>> body = response.body();
                if (body != null && body.getCode() == 200) {
                    List<VehicleUse> rows = body.getRows();

                    if (rows.isEmpty()) {
                        binding.carWaitUse.setVisibility(View.GONE);
                        binding.textEmpty.setVisibility(View.VISIBLE);
                    } else {
                        binding.carWaitUse.setVisibility(View.VISIBLE);
                        binding.textEmpty.setVisibility(View.GONE);
                    }


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