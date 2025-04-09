package com.me.vehicle.model;

import java.util.Date;

public class Locations {

    /** 位置记录ID，自动递增的主键 */
    private Long id;

    /** 车辆ID，关联vehicles表 */
    private Long vehicleId;

    /** 位置地址描述 */
    private String address;

    /** 记录时间 */
    private String recordedAt;

    /** 当前使用者ID，关联users表 */
    private Long userId;

    public Locations(Long vehicleId, String address, String recordedAt, Long userId) {
        this.vehicleId = vehicleId;
        this.address = address;
        this.recordedAt = recordedAt;
        this.userId = userId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setVehicleId(Long vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId()
    {
        return vehicleId;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setRecordedAt(String recordedAt)
    {
        this.recordedAt = recordedAt;
    }

    public String getRecordedAt()
    {
        return recordedAt;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

}
