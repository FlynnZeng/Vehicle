package com.me.vehicle.ui.carReq;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.me.vehicle.adapter.VehicleSpinnerAdapter;
import com.me.vehicle.api.AjaxResult;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentCarReqBinding;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;
import com.me.vehicle.utils.PreferencesUtil;
import com.me.vehicle.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarReqFragment extends Fragment {

    private FragmentCarReqBinding binding;
    private Services services;
    private List<Vehicle> carList = new ArrayList<>();
    private ArrayAdapter<Vehicle> carAdapter;

    public static CarReqFragment newInstance() {
        return new CarReqFragment();
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
        binding = FragmentCarReqBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        initSpinner();

        binding.editDate.setOnClickListener(v -> showMaterialDatePicker());

        binding.editStartTime.setOnClickListener(v -> showMaterialTimePicker(binding.editStartTime));
        binding.editEndTime.setOnClickListener(v -> showMaterialTimePicker(binding.editEndTime));

        binding.btnSubmit.setOnClickListener(v -> collectData());

        return root;
    }

    private void initSpinner() {
        carAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, carList);

        carAdapter = new VehicleSpinnerAdapter(requireContext(), carList);
        binding.spinnerCar.setAdapter(carAdapter);

        getCarList();

        binding.spinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vehicle selectedVehicle = carList.get(position);
                // 你可以记录选中车的 ID 或其他字段
                ToastUtil.showToast(requireActivity(), selectedVehicle.getPlateNumber());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 用户没有选中任何项
                ToastUtil.showToast(requireActivity(), "no");
            }
        });
    }

    private void getCarList() {
        services.getCarList().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Vehicle>>> call, @NonNull Response<ApiResponse<List<Vehicle>>> response) {
                services.getCarList().enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<List<Vehicle>>> call, @NonNull Response<ApiResponse<List<Vehicle>>> response) {
                        ApiResponse<List<Vehicle>> body = response.body();
                        if (body != null && body.getCode() == 200) {
                            List<Vehicle> rows = body.getRows();
                            carList.clear();
                            carList.addAll(rows);
                            carAdapter.notifyDataSetChanged();
                        } else {
                            String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "获取列表失败，请稍后重试";
                            ToastUtil.showToast(requireActivity(), msg);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<List<Vehicle>>> call, @NonNull Throwable t) {
                        ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Vehicle>>> call, @NonNull Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }

    private void showMaterialDatePicker() {
        // 获取明天的时间戳（毫秒）
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        long tomorrowMillis = calendar.getTimeInMillis();

        // 设置最小选择日期（明天）
        CalendarConstraints.DateValidator validator = DateValidatorPointForward.from(tomorrowMillis);

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(validator)
                .setStart(tomorrowMillis)
                .build();

        // 创建 MaterialDatePicker
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("请选择日期")
                .setSelection(tomorrowMillis) // 默认选中明天
                .setCalendarConstraints(constraints)
                .build();

        // 监听选择结果
        picker.addOnPositiveButtonClickListener(selection -> {
            // selection 是时间戳（毫秒）
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.setTimeInMillis(selection);
            String formatted = String.format(Locale.CHINA, "%04d-%02d-%02d",
                    selectedCal.get(Calendar.YEAR),
                    selectedCal.get(Calendar.MONTH) + 1,
                    selectedCal.get(Calendar.DAY_OF_MONTH));

            binding.editDate.setText(formatted);
        });

        picker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
    }

    private void showMaterialTimePicker(EditText targetEditText) {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H) // 24 小时制
                .setHour(9) // 默认小时
                .setMinute(0) // 默认分钟
                .setTitleText("选择时间")
                .build();

        picker.addOnPositiveButtonClickListener(v -> {
            String timeStr = String.format(Locale.CHINA, "%02d:%02d", picker.getHour(), picker.getMinute());
            targetEditText.setText(timeStr);
        });

        picker.show(getParentFragmentManager(), "MATERIAL_TIME_PICKER");
    }

    private void collectData() {
        // 获取输入内容
        Vehicle selectedVehicle = (Vehicle) binding.spinnerCar.getSelectedItem();
        String date = binding.editDate.getText().toString().trim();
        String startTime = binding.editStartTime.getText().toString().trim();
        String endTime = binding.editEndTime.getText().toString().trim();
        String user = binding.editUser.getText().toString().trim();
        String companions = binding.editFollow.getText().toString().trim();
        String reason = binding.editReason.getText().toString().trim();
        String from = binding.editFrom.getText().toString().trim();
        String to = binding.editTo.getText().toString().trim();

        // 判空校验
        if (selectedVehicle == null) {
            ToastUtil.showToast(requireActivity(), "请选择车辆");
            return;
        }

        if (TextUtils.isEmpty(date)) {
            ToastUtil.showToast(requireActivity(), "请选择用车日期");
            return;
        }

        if (TextUtils.isEmpty(startTime)) {
            ToastUtil.showToast(requireActivity(), "请选择开始时间");
            return;
        }

        if (TextUtils.isEmpty(endTime)) {
            ToastUtil.showToast(requireActivity(), "请选择结束时间");
            return;
        }

        if (TextUtils.isEmpty(user)) {
            ToastUtil.showToast(requireActivity(), "请填写用车人");
            return;
        }

        if (TextUtils.isEmpty(reason)) {
            ToastUtil.showToast(requireActivity(), "请填写用车事由");
            return;
        }

        if (TextUtils.isEmpty(from)) {
            ToastUtil.showToast(requireActivity(), "请填写出发地点");
            return;
        }

        if (TextUtils.isEmpty(to)) {
            ToastUtil.showToast(requireActivity(), "请填写目的地");
            return;
        }

        // 数据封装（可用于提交接口）
        VehicleUse request = new VehicleUse();
        request.setVehicleId(selectedVehicle.getId());
        request.setUserID(PreferencesUtil.getLong(requireActivity(), "id"));
        request.setApplyDate(date);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setUsername(user);
        request.setCompanions(companions);
        request.setReason(reason);
        request.setStartLocation(from);
        request.setEndLocation(to);

        useCar(request);
    }

    private void useCar(VehicleUse use) {
        services.requestUse(use).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AjaxResult<String>> call, @NonNull Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    ToastUtil.showToast(requireActivity(), "申请成功，待审核");
                    requireActivity().finish();
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "获取列表失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AjaxResult<String>> call, @NonNull Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }
}