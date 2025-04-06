package com.me.vehicle.api;

import com.me.vehicle.model.Users;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Services {
    // 登录
    @POST("/vehicle/users/login")
    Call<AjaxResult<String>> login(@Body Users user);

    // 获取用户信息
    @GET("/vehicle/users/info")
    Call<AjaxResult<Users>> getUserInfo();

    // 获取派车记录
    @GET("/vehicle/vehicles/list")
    Call<ApiResponse<List<Vehicle>>> getCarList();

    // 根据车辆id获取用车记录
    @GET("/vehicle/applications/list/{id}")
    Call<ApiResponse<List<VehicleUse>>> getCarUseRecords(@Path("id") Long carId);

}
