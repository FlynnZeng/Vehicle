package com.me.vehicle.model;

import java.math.BigDecimal;
import java.util.Date;

public class CostStatistics
{
    private static final long serialVersionUID = 1L;

    /** 费用记录ID，自动递增的主键 */
    private Long id;

    /** 相关车辆ID，关联vehicles表 */
    private Long vehicleId;

    /** 费用产生日期 */
    private String costDate;

    /** 费用类型：加油费、维修保养或其他 */
    private String costType;

    /** 费用描述 */
    private String description;

    /** 费用金额，保留两位小数 */
    private BigDecimal amount;

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

    public void setCostDate(String costDate)
    {
        this.costDate = costDate;
    }

    public String getCostDate()
    {
        return costDate;
    }

    public void setCostType(String costType)
    {
        this.costType = costType;
    }

    public String getCostType()
    {
        return costType;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

}

