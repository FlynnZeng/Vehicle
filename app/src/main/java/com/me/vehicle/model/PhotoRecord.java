package com.me.vehicle.model;

import java.util.Date;

public class PhotoRecord {

    /** 照片ID，自动递增的主键 */
    private Long id;

    /** 相关车辆ID，关联vehicles表 */
    private Long vehicleId;

    /** 拍摄用户ID，关联users表 */
    private Long userId;

    /** 照片存储路径 */
    private String photoPath;

    /** 拍摄时间 */
    private String shootTime;

    /** 拍摄类型：出车前或返回时 */
    private String shootType;

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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getShootTime() {
        return shootTime;
    }

    public void setShootTime(String shootTime) {
        this.shootTime = shootTime;
    }

    public String getShootType() {
        return shootType;
    }

    public void setShootType(String shootType) {
        this.shootType = shootType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
