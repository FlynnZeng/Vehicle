package com.me.vehicle.ui.notifications;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.me.vehicle.MainActivity;
import com.me.vehicle.R;
import com.me.vehicle.api.AjaxResult;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentNotificationsBinding;
import com.me.vehicle.model.Users;
import com.me.vehicle.ui.login.LoginActivity;
import com.me.vehicle.utils.PreferencesUtil;
import com.me.vehicle.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Services services;

    long id;


    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit client = RetrofitClient.getClient(requireActivity());
        services = client.create(Services.class);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.container.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        String username = PreferencesUtil.getString(requireActivity(), "username");
        String realName = PreferencesUtil.getString(requireActivity(), "realName");
        id = PreferencesUtil.getLong(requireActivity(), "id");
        String phone = PreferencesUtil.getString(requireActivity(), "phone");
        String role = PreferencesUtil.getString(requireActivity(), "role");

        binding.textUsername.setText(String.format("用户名: %s", username));
        binding.editRealName.setText(realName);
        binding.editPhone.setText(phone);
        binding.textRole.setText(String.format("角色: %s", role));

        binding.btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        binding.btnLogout.setOnClickListener(v->logout());
        binding.btnSave.setOnClickListener(v->savaInfo());

        return root;
    }

    private void showChangePasswordDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_password, null);

        EditText oldPwd = dialogView.findViewById(R.id.edit_old_pwd);
        EditText newPwd = dialogView.findViewById(R.id.edit_new_pwd);
        EditText confirmPwd = dialogView.findViewById(R.id.edit_confirm_pwd);

        new AlertDialog.Builder(getContext())
                .setTitle("修改密码")
                .setView(dialogView)
                .setPositiveButton("确认修改", (dialog, which) -> {
                    String old = oldPwd.getText().toString().trim();
                    String pwd = newPwd.getText().toString().trim();
                    String confirm = confirmPwd.getText().toString().trim();

                    if (!pwd.equals(confirm)) {
                        ToastUtil.showToast(getContext(), "两次密码不一致");
                        return;
                    }

                    checkPwd(old, pwd);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void changePassword(String newPwd) {
        Users users = new Users();
        users.setId(id);
        users.setPassword(newPwd);
        services.changeInfo(users).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    ToastUtil.showToast(getContext(), "密码修改成功");
                } else {
                    ToastUtil.showToast(getContext(), body != null ? body.getMsg() : "修改失败");
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(getContext(), "网络错误，修改失败");
            }
        });
    }

    private void checkPwd(String oldPwd, String newPwd) {
        Users users = new Users();
        users.setId(id);
        users.setPassword(oldPwd);
        services.checkPwd(users).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<Boolean>> call, Response<AjaxResult<Boolean>> response) {
                AjaxResult<Boolean> body = response.body();
                if (body == null || body.getCode() != 200) {
                    ToastUtil.showToast(getContext(), "旧密码错误");
                } else {
                    changePassword(newPwd);
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<Boolean>> call, Throwable t) {
                ToastUtil.showToast(getContext(), "网络错误，修改失败");
            }
        });
    }

    private void logout() {
        services.logout().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    ToastUtil.showToast(getContext(), "退出成功");
                    PreferencesUtil.clearPreferences(requireActivity());
                    requireActivity().startActivity(new Intent(requireActivity(), LoginActivity.class));
                    requireActivity().finish();
                } else {
                    ToastUtil.showToast(getContext(), "请稍后重试");
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(getContext(), "网络错误，修改失败");
            }
        });
    }

    private void savaInfo(){
        String real = binding.editRealName.getText().toString().trim();
        String phone = binding.editPhone.getText().toString().trim();

        // 判空处理
        if (real.isEmpty()) {
            ToastUtil.showToast(getContext(), "真实姓名不能为空");
            return;
        }

        if (phone.isEmpty()) {
            ToastUtil.showToast(getContext(), "手机号不能为空");
            return;
        }

        if (!phone.matches("^1[3-9]\\d{9}$")) {
            ToastUtil.showToast(getContext(), "手机号格式不正确");
            return;
        }

        Users users = new Users();
        users.setId(id);
        users.setRealName(real);
        users.setPhone(phone);

        services.changeInfo(users).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    ToastUtil.showToast(getContext(), "修改成功");
                } else {
                    ToastUtil.showToast(getContext(), body != null ? body.getMsg() : "修改失败");
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(getContext(), "网络错误，修改失败");
            }
        });

        services.getUserInfo().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<Users>> call, Response<AjaxResult<Users>> response) {
                AjaxResult<Users> body = response.body();
                if (body != null && body.getCode() == 200) {
                    Users user = body.getData();

                    // 保存 token
                    PreferencesUtil.putString(requireActivity(), "username", user.getUsername());
                    PreferencesUtil.putString(requireActivity(), "realName", user.getRealName());
                    PreferencesUtil.putLong(requireActivity(), "id", user.getId());
                    PreferencesUtil.putString(requireActivity(), "phone", user.getPhone());
                    PreferencesUtil.putString(requireActivity(), "role", user.getRole());
                    PreferencesUtil.putBoolean(requireActivity(), "login", true);

                    ToastUtil.showToast(requireActivity(), "登录成功");
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "请重新登录";
                    ToastUtil.showToast(requireActivity(), msg);
                    requireActivity().startActivity(new Intent(requireActivity(), LoginActivity.class));
                    requireActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<Users>> call, Throwable t) {
                ToastUtil.showToast(requireActivity(), "请重新登录");
                requireActivity().startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}