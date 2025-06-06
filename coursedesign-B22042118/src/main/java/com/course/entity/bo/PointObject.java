package com.course.entity.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lixuy
 * Created on 2019-04-10
 */
@Data
public class PointObject implements Serializable{

    private static final long serialVersionUID = 123456789L;

    private Integer id;
    //成长积分数
    private Integer growScore;
    //可兑换积分数
    private Integer exchangeScore;
    //总积分数
    private Integer scoreTotal;
}
