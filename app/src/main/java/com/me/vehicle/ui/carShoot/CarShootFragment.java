package com.me.vehicle.ui.carShoot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.vehicle.R;
import com.me.vehicle.api.AjaxResult;
import com.me.vehicle.api.ApiResponse;
import com.me.vehicle.api.RetrofitClient;
import com.me.vehicle.api.Services;
import com.me.vehicle.databinding.FragmentCarShootBinding;
import com.me.vehicle.model.Dispatch;
import com.me.vehicle.model.PhotoRecord;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;
import com.me.vehicle.ui.carShootRes.CarShootResActivity;
import com.me.vehicle.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarShootFragment extends Fragment {

    private FragmentCarShootBinding binding;

    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private Uri imageUri;
    private Services services;
    private VehicleUse use;
    private Vehicle car;
    private File photoFile; // 保存拍照时的实际文件
    private PhotoRecord record;
    private Dispatch dispatch;

    public static CarShootFragment newInstance() {
        return new CarShootFragment();
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

        binding = FragmentCarShootBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        init();

        initData();

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && imageUri != null) {
                        assert use != null;
                        previewImage(use.getUsername(), car.getPlateNumber());
                        binding.btnShoot.setText("重拍");
                    } else {
                        ToastUtil.showToast(requireContext(), "拍照失败");
                    }
                });

        binding.btnShoot.setOnClickListener(v -> checkCameraPermissionAndOpen());

        binding.btnUpload.setOnClickListener(v -> uploadImage(photoFile));

        return root;
    }

    private void init(){
        record = new PhotoRecord();
        dispatch = new Dispatch();

        Intent intent = requireActivity().getIntent();
        use = (VehicleUse) intent.getSerializableExtra("carInfo");
        assert use != null;
        binding.editUser.setText(use.getUsername());

        record.setUserId(use.getUserID());
        record.setVehicleId(use.getVehicleId());
        record.setShootType("出车前");

        dispatch.setUserId(use.getUserID());
        dispatch.setVehicleId(use.getVehicleId());
        dispatch.setUseReason(use.getCompanions());
        dispatch.setStartTime(use.getStartTime());
        dispatch.setEndTime(use.getEndTime());
        dispatch.setStartLocation(use.getStartLocation());
        dispatch.setEndLocation(use.getEndLocation());
        dispatch.setMileage(0L);
    }

    private void initData() {
        services.getCarInfo(use.getVehicleId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AjaxResult<Vehicle>> call, @NonNull Response<AjaxResult<Vehicle>> response) {
                AjaxResult<Vehicle> body = response.body();
                if (body != null && body.getCode() == 200) {
                    car = body.getData();
                    binding.editCar.setText(car.getPlateNumber());
                } else {
                    String msg = (body != null && body.getMsg() != null) ? body.getMsg() : "获取列表失败，请稍后重试";
                    ToastUtil.showToast(requireActivity(), msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AjaxResult<Vehicle>> call, @NonNull Throwable t) {
                ToastUtil.showToast(requireActivity(), "网络错误，请稍后重试");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                ToastUtil.showToast(requireContext(), "请授予相机权限");
            }
        }
    }

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 权限未授予，请求权限
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 2001);
        } else {
            // 权限已授予，打开相机
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            photoFile = createImageFile();
            File photoFile = createImageFile();

            imageUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".fileprovider",
                    photoFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraLauncher.launch(intent);
        }
    }

    private File createImageFile() {
        String fileName = "dispatch_" + System.currentTimeMillis();
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return new File(storageDir, fileName + ".jpg");
    }

    private void previewImage(String username, String plateNumber) {
        if (imageUri == null) return;

        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri)) {
            Bitmap original = BitmapFactory.decodeStream(inputStream);

            // 添加水印
            String watermark = generateWatermarkText(username, plateNumber);
            Bitmap watermarked = addWatermark(original, watermark);

            // 显示预览图
            binding.imagePreview.setImageBitmap(watermarked);
            binding.imagePreview.setVisibility(View.VISIBLE);
            binding.textCameraHint.setVisibility(View.GONE);

            saveBitmapToFile(watermarked, photoFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap addWatermark(Bitmap original, String watermarkText) {
        int width = original.getWidth();
        int height = original.getHeight();

        // 创建可编辑的副本
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(original, 0, 0, null);

        // 设置文字样式
        Paint paint = new Paint();
        paint.setColor(Color.WHITE); // 文字颜色
        paint.setTextSize(width / 40f); // 动态文字大小
        paint.setAntiAlias(true);
        paint.setShadowLayer(3f, 2f, 2f, Color.BLACK); // 阴影更清晰

        // 设置水印位置（底部左下角）
        float x = width * 0.05f;
        float y = height * 0.95f;

        canvas.drawText(watermarkText, x, y, paint);

        return newBitmap;
    }

    private String generateWatermarkText(String username, String plateNumber) {
        String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date());
        return "用车人：" + username + "  车牌：" + plateNumber + "  时间：" + timeStr;
    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showToast(requireContext(), "保存图片失败");
        }
    }

    private void uploadImage(File imageFile) {
        // 构建请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);

        services.uploadFile(part).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    String url = body.getData(); // 返回的图片 URL
                    record.setPhotoPath(url);
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
                    record.setShootTime(dateStr);
                    addRecord(record);
                } else {
                    ToastUtil.showToast(requireContext(), "上传失败: " + (body != null ? body.getMsg() : "未知错误"));
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(requireContext(), "网络错误，上传失败");
            }
        });
    }

    private void addRecord(PhotoRecord record) {
        services.addRecord(record).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    ToastUtil.showToast(requireActivity(), "上传成功");
                    addDispatchCar(dispatch);
                } else {
                    ToastUtil.showToast(requireContext(), "上传失败: " + (body != null ? body.getMsg() : "未知错误"));
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(requireContext(), "网络错误，上传失败");
            }
        });
    }

    private void addDispatchCar(Dispatch dispatch) {
        services.addDispatchCar(dispatch).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                AjaxResult<String> body = response.body();
                if (body != null && body.getCode() == 200) {
                    Intent intent = new Intent(requireActivity(), CarShootResActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    ToastUtil.showToast(requireContext(), "请求失败: " + (body != null ? body.getMsg() : "未知错误"));
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                ToastUtil.showToast(requireContext(), "网络错误，请稍后重试");
            }
        });
    }
}