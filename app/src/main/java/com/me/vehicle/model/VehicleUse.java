package com.me.vehicle.model;

import java.io.Serializable;
import java.util.Date;

public class VehicleUse implements Serializable {

    /** 申请ID，自动递增的主键 */
    private Long id;

    /** 申请用户ID，关联users表 */
    private Long userId;

    private String username;

    /** 申请车辆ID，关联vehicles表 */
    private Long vehicleId;

    /** 申请日期 */
    private String applyDate;

    /** 用车开始时间 */
    private String startTime;

    /** 用车结束时间 */
    private String endTime;

    /** 同行人员信息 */
    private String companions;

    /** 用车理由 */
    private String reason;

    /** 出发地点 */
    private String startLocation;

    /** 目的地点 */
    private String endLocation;

    /** 申请状态：待审核、已批准或已拒绝 */
    private String status;

    /** 申请创建时间，默认为当前时间戳 */
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserID() {
        return userId;
    }

    public void setUserID(Long userId) {
        this.userId = userId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
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

    public String getCompanions() {
        return companions;
    }

    public void setCompanions(String companions) {
        this.companions = companions;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
