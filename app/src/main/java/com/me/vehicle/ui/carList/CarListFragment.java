package com.me.vehicle.ui.carList;

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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.R;
import com.me.vehicle.adapter.CarListAdapter;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentCarListBinding;
import com.me.vehicle.model.Users;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.ui.CarInfoActivity;
import com.me.vehicle.utils.PreferencesUtil;
import com.me.vehicle.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarListFragment extends Fragment {

    private CarListViewModel mViewModel;
    private FragmentCarListBinding binding;
    private Services services;
    private List<Vehicle> carList;
    private CarListAdapter adapter;

    public static CarListFragment newInstance() {
        return new CarListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CarListViewModel.class);

        Retrofit client = RetrofitClient.getClient(requireActivity());
        services = client.create(Services.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCarListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        RecyclerView recyclerView = binding.carList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        carList = new ArrayList<>();
        adapter = new CarListAdapter(getContext(), carList, item -> {
            Intent intent = new Intent(requireActivity(), CarInfoActivity.class);
            intent.putExtra("carInfo", item);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        getList();
        return root;
    }

    private void getList(){
        services.getCarList().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Vehicle>>> call, Response<ApiResponse<List<Vehicle>>> response) {
                ApiResponse<List<Vehicle>> body = response.body();
                if (body != null && body.getCode() == 200) {
                    List<Vehicle> rows = body.getRows();

                    carList.clear();
                    carList.addAll(rows);
                    adapter.notifyDataSetChanged();
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "获取列表失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Vehicle>>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }
}