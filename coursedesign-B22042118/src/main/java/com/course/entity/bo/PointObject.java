package com.course.entity.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PointObject implements Serializable {

    private static final long serialVersionUID = 123456789L;

    private Integer id;
    // 成长积分数
    private Integer growScore;
    // 可兑换积分数
    private Integer exchangeScore;
    // 总积分数
    private Integer scoreTotal;
    // 每日首次登录日期
    private Date lastLoginDate;
    // 是否首次填写个人资料
    private boolean isFirstFillInfo;
    // 血糖记录数
    private Integer bloodSugarRecordCount;
    // 并发症记录填写年份
    private Integer lastBfzNoteYear;
    // 评估报告填写时的血糖记录数
    private Integer evaluateReportBloodSugarCount;
    // 胰岛功能监测上次积分日期
    private Date lastYdgnNoteDate;
    // 可兑换积分有效期
    private Date exchangeScoreExpiryDate;
    // 每月成长积分起始日期
    private Date monthlyGrowScoreStartDate;
}