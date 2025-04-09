package com.me.vehicle.ui.carLocale;

import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.adapter.TraceListAdapter;
import com.me.vehicle.api.AjaxResult;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentCarLocaleBinding;
import com.me.vehicle.model.Locations;
import com.me.vehicle.model.Trace;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;
import com.me.vehicle.utils.LocationManagerHelper;
import com.me.vehicle.utils.PreferencesUtil;
import com.me.vehicle.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarLocaleFragment extends Fragment {
    private FragmentCarLocaleBinding binding;
    private RecyclerView rvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
    private String full;
    private Services services;
    private Vehicle car;

    public static CarLocaleFragment newInstance() {
        return new CarLocaleFragment();
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
        binding = FragmentCarLocaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        Intent intent = requireActivity().getIntent();
        car = (Vehicle) intent.getSerializableExtra("item");

        binding.btnRefresh.setOnClickListener(v -> {
            getLocation();
            initData();
        });
        getLocation();

        traceList = new ArrayList<>();
        adapter = new TraceListAdapter(requireActivity(), traceList);
        rvTrace = binding.rvTrace;
        rvTrace.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvTrace.setAdapter(adapter);

        binding.submit.setOnClickListener(v->submitLocale());

        initData();
        return root;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1001);
            return;
        }

        LocationManagerHelper.getInstance().build(requireContext(), addressList -> {
            if (addressList != null && !addressList.isEmpty()) {
                String country = addressList.get(0).getCountryName();       // 国家
                String province = addressList.get(0).getAdminArea();        // 省
                String city = addressList.get(0).getSubAdminArea();         // 市（有时会是 null）
                if (city == null) city = addressList.get(0).getLocality();  // 兼容部分系统
                String district = addressList.get(0).getSubLocality();      // 区/街道
                String thoroughfare = addressList.get(0).getThoroughfare();      // 区/街道

                full = country + province + city + district + thoroughfare;

                binding.textAddress.setText(full);
            } else {
                binding.textAddress.setText("无法获取地址");
            }
        });
    }

    private void initData() {
        services.getLocations(car.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<List<Locations>>> call, Response<AjaxResult<List<Locations>>> response) {
                AjaxResult<List<Locations>> body = response.body();
                if (body != null && body.getCode() == 200) {
                    List<Locations> locationList = body.getData();
                    List<Trace> list = convertToTraceList(locationList);

                    traceList.clear();
                    traceList.addAll(list);

                    adapter.notifyDataSetChanged();
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "获取列表失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<List<Locations>>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }

    private void submitLocale() {
        Long userId = PreferencesUtil.getLong(requireActivity(), "id");
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());

        Locations locale = new Locations(car.getId(), full, dateStr, userId);

        services.addLocations(locale).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    ToastUtil.showToast(requireActivity(), "提交成功");
                    initData();
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "获取列表失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }

    public static List<Trace> convertToTraceList(List<Locations> locationsList) {
        List<Trace> traceList = new ArrayList<>();
        if (locationsList != null) {
            for (Locations loc : locationsList) {
                traceList.add(new Trace(loc.getRecordedAt(), loc.getAddress()));
            }
        }
        return traceList;
    }


}