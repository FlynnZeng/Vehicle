package com.me.vehicle.api;

import com.google.android.gms.common.api.Api;
import com.me.vehicle.model.CarReturnInfo;
import com.me.vehicle.model.CostStatistics;
import com.me.vehicle.model.Dispatch;
import com.me.vehicle.model.Locations;
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

    // 更新用车状态
    @POST("/vehicle/applications/update")
    Call<AjaxResult<String>> updateApplyState(@Body VehicleUse vehicleUse);

    // 获取出车记录
    @GET("/vehicle/applications/list")
    Call<ApiResponse<List<VehicleUse>>> getDispatchRecord();

    // 添加车辆行驶里程
    @POST("/vehicle/applications/mileage")
    Call<AjaxResult<String>> addMileage(@Body CarReturnInfo info);

    // 添加统计费用
    @POST("/vehicle/statistics/add-more")
    Call<AjaxResult<String>> addExpenses(@Body List<CostStatistics> list);

    // 获取当前车辆定位记录
    @GET("/vehicle/locations/car/{id}")
    Call<AjaxResult<List<Locations>>> getLocations(@Path("id") Long id);

    // 添加定位
    @POST("/vehicle/locations")
    Call<AjaxResult<String>> addLocations(@Body Locations locations);

    // 修改用户信息
    @POST("/vehicle/users/edit")
    Call<AjaxResult<String>> changeInfo(@Body Users users);

    // 检查用户原秘密是否正确
    @POST("/vehicle/users/checkPassword")
    Call<AjaxResult<Boolean>> checkPwd(@Body Users users);

    // 退出登录
    @GET("/vehicle/users/logout")
    Call<AjaxResult<String>> logout();

    // 注册用户
    @POST("/vehicle/users")
    Call<AjaxResult<String>> register(@Body Users user);

    // 用户车辆的费用信息
    @GET("/vehicle/statistics/list/{carId}")
    Call<ApiResponse<List<CostStatistics>>> getStatistics(@Path("carId") Long id);
}
