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
    private Date startTime;

    /** 结束用车时间 */
    private Date endTime;

    /** 出发地点 */
    private String startLocation;

    /** 目的地点 */
    private String endLocation;

    /** 本次行程里程数，单位：公里 */
    private Long mileage;

    /** 记录创建时间，默认为当前时间戳 */
    private Date createdAt;
}
