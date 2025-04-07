package com.me.vehicle.ui.carRecord;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.R;
import com.me.vehicle.adapter.DispatchAdapter;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentCarRecordBinding;
import com.me.vehicle.model.Dispatch;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;
import com.me.vehicle.utils.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarRecordFragment extends Fragment {
    private Services services;
    private FragmentCarRecordBinding binding;

    private DispatchAdapter adapter;

    private  RecyclerView recyclerView;

    public static CarRecordFragment newInstance() {
        return new CarRecordFragment();
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
        binding =  FragmentCarRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.record_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new DispatchAdapter();
        recyclerView.setAdapter(adapter);
        init();
        return root;
    }

    private void init(){
        services.getDispatchRecord().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<List<VehicleUse>>> call, Response<ApiResponse<List<VehicleUse>>> response) {
                ApiResponse<List<VehicleUse>> body = response.body();
                if (body != null && body.getCode() == 200) {
                    List<VehicleUse> rows = body.getRows();
                    adapter.setData(rows);
                    adapter.notifyDataSetChanged();
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "获取记录失败，请稍后重试";
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