package com.me.vehicle.api;

import com.me.vehicle.model.Users;
import com.me.vehicle.model.Vehicle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Services {
    @POST("/vehicle/users/login")
    Call<AjaxResult<String>> login(@Body Users user);

    @GET("/vehicle/users/info")
    Call<AjaxResult<Users>> getUserInfo();

    @GET("/vehicle/vehicles/list")
    Call<ApiResponse<List<Vehicle>>> getCarList();
}
