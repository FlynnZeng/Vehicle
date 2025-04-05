package com.me.vehicle.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.me.vehicle.MainActivity;
import com.me.vehicle.R;
import com.me.vehicle.api.AjaxResult;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentLoginBinding;
import com.me.vehicle.model.Users;
import com.me.vehicle.utils.PreferencesUtil;
import com.me.vehicle.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private Services services;
    private FragmentLoginBinding binding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        Retrofit client = RetrofitClient.getClient(requireActivity());
        services = client.create(Services.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnLogin.setOnClickListener(v->login());

        return root;
    }

    private void login() {
        String username = binding.editUsername.getText().toString().trim();
        String password = binding.editPassword.getText().toString().trim();

        // 检查是否为空
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showToast(requireActivity(), "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(requireActivity(), "请输入密码");
            return;
        }

        // 构造请求体
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);

        // 发起请求
        services.login(user).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    String token = body.getData();
                    // 保存 token
                    PreferencesUtil.putString(requireActivity(), "token", token);

                    getUserInfo();

                    // TODO: 获取用户信息，然后调整首页

                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "登录失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }

    public void getUserInfo(){
        services.getUserInfo().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<Users>> call, Response<AjaxResult<Users>> response) {
                AjaxResult<Users> body = response.body();
                if (body != null && body.getCode() == 200) {
                    Users user = body.getData();

                    // 保存 token
                    PreferencesUtil.putString(requireActivity(), "username", user.getUsername());
                    PreferencesUtil.putString(requireActivity(), "realName", user.getRealName());
                    PreferencesUtil.putInt(requireActivity(), "id", user.getId().intValue());
                    PreferencesUtil.putString(requireActivity(), "phone", user.getPhone());
                    PreferencesUtil.putString(requireActivity(), "role", user.getRole());

                    ToastUtil.showToast(requireActivity(), "登录成功");
                    toHome();
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "登录失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<Users>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }

    private void toHome(){
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}