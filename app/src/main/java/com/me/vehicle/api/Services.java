package com.me.vehicle.api;

import com.me.vehicle.model.Dispatch;
import com.me.vehicle.model.PhotoRecord;
import com.me.vehicle.model.Users;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Services {
    // 登录
    @POST("/vehicle/users/login")
    Call<AjaxResult<String>> login(@Body Users user);

    // 获取用户信息
    @GET("/vehicle/users/info")
    Call<AjaxResult<Users>> getUserInfo();

    // 获取车辆列表
    @GET("/vehicle/vehicles/list")
    Call<ApiResponse<List<Vehicle>>> getCarList();

    // 根据车辆id获取用车记录
    @GET("/vehicle/applications/list/{id}")
    Call<ApiResponse<List<VehicleUse>>> getCarUseRecords(@Path("id") Long carId);

    // 申请用车
    @POST("/vehicle/applications")
    Call<AjaxResult<String>> requestUse(@Body VehicleUse use);

    // 获取待出车的列表
    @GET("/vehicle/applications/approved")
    Call<ApiResponse<List<VehicleUse>>> getWaitUseList();

    // 获取车辆信息
    @GET("/vehicle/vehicles/{id}")
    Call<AjaxResult<Vehicle>> getCarInfo(@Path("id") Long carId);

    // 上传文件
    @Multipart
    @POST("/vehicle/uploads")
    Call<AjaxResult<String>> uploadFile(@Part MultipartBody.Part file);

    // 添加出车拍照记录
    @POST("/vehicle/photos")
    Call<AjaxResult<String>> addRecord(@Body PhotoRecord record);

    // 添加派车记录
    @POST("/vehicle/records")
    Call<AjaxResult<String>> addDispatchCar(@Body Dispatch dispatch);
}
