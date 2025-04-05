package com.me.vehicle.model;

import java.io.Serializable;
import java.util.Date;

public class Vehicle implements Serializable {
    /** 车辆ID，自动递增的主键 */
    private Long id;

    /** 车牌号，唯一标识符 */
    private String plateNumber;

    /** 车辆图片 */
    private String cover;

    /** 车辆品牌 */
    private String brand;

    /** 车辆类型 */
    private String carType;

    /** 车辆颜色 */
    private String color;

    /** 车辆里程数，单位：公里 */
    private Long mileage;

    /** 车辆购买日期 */
    private Date purchaseDate;

    /** 最近一次维护保养日期 */
    private Date lastMaintenance;

    /** 保险到期日期 */
    private Date insuranceExpiry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getMileage() {
        return mileage;
    }

    public void setMileage(Long mileage) {
        this.mileage = mileage;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(Date lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }

    public Date getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(Date insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
    }

    public Vehicle() {

    }

    public Vehicle(Long id, String plateNumber, String cover, String brand, String carType, String color, Long mileage, Date purchaseDate, Date lastMaintenance, Date insuranceExpiry) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.cover = cover;
        this.brand = brand;
        this.carType = carType;
        this.color = color;
        this.mileage = mileage;
        this.purchaseDate = purchaseDate;
        this.lastMaintenance = lastMaintenance;
        this.insuranceExpiry = insuranceExpiry;
    }
}
