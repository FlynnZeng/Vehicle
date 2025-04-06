package com.me.vehicle.model;

import java.util.Date;

public class Dispatch {

    /** 派车记录ID，自动递增的主键 */
    private Long id;

    /** 派出车辆ID，关联vehicles表 */
    private Long vehicleId;

    /** 用车人ID，关联users表 */
    private Long userId;

    /** 用车原因 */
    private String useReason;

    /** 开始用车时间 */
    private String startTime;

    /** 结束用车时间 */
    private String endTime;

    /** 出发地点 */
    private String startLocation;

    /** 目的地点 */
    private String endLocation;

    /** 本次行程里程数，单位：公里 */
    private Long mileage;

    /** 记录创建时间，默认为当前时间戳 */
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUseReason() {
        return useReason;
    }

    public void setUseReason(String useReason) {
        this.useReason = useReason;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public Long getMileage() {
        return mileage;
    }

    public void setMileage(Long mileage) {
        this.mileage = mileage;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
