package com.me.vehicle.model;

public class CarReturnInfo {
    private Long id;
    private Long useId;

    /** 本次行驶里程（单位：公里） */
    private double mileage;


    // 构造方法
    public CarReturnInfo(Long id, Long useId, double mileage) {
        this.id = id;
        this.useId = useId;
        this.mileage = mileage;
    }

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getUseId() {
        return useId;
    }

    public void setUseId(Long useId) {
        this.useId = useId;
    }
    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }
}

