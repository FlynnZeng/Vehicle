package com.me.vehicle.ui.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.api.AjaxResult;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentRegisterBinding;
import com.me.vehicle.model.Users;
import com.me.vehicle.utils.PreferencesUtil;
import com.me.vehicle.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private Services services;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
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
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 返回登录
        binding.textGoLogin.setOnClickListener(v -> requireActivity().finish());

        // 注册按钮点击事件
        binding.btnRegister.setOnClickListener(v -> register());

        return root;
    }

    private void register() {
        String username = binding.editUsername.getText().toString().trim();
        String password = binding.editPassword.getText().toString().trim();
        String confirmPwd = binding.editConfirmPassword.getText().toString().trim();
        String realName = binding.editRealName.getText().toString().trim();
        String phone = binding.editPhone.getText().toString().trim();

        // 校验空字段
        if (username.isEmpty()) {
            ToastUtil.showToast(requireActivity(), "用户名不能为空");
            return;
        }
        if (password.isEmpty()) {
            ToastUtil.showToast(requireActivity(), "密码不能为空");
            return;
        }
        if (confirmPwd.isEmpty()) {
            ToastUtil.showToast(requireActivity(), "请确认密码");
            return;
        }
        if (!password.equals(confirmPwd)) {
            ToastUtil.showToast(requireActivity(), "两次密码输入不一致");
            return;
        }
        if (realName.isEmpty()) {
            ToastUtil.showToast(requireActivity(), "真实姓名不能为空");
            return;
        }
        if (phone.isEmpty()) {
            ToastUtil.showToast(requireActivity(), "手机号不能为空");
            return;
        }

        // 构造用户对象
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setPhone(phone);
        user.setRole("user");

        // 发起注册请求
        services.register(user).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    ToastUtil.showToast(requireActivity(), "注册成功，请登录");
                    requireActivity().finish();
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "注册失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }
}