package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.FollowUpService;
import org.springframework.stereotype.Service;
/**
 * 完成门诊随访
 */
@Service
public class FollowUpServiceImpl implements FollowUpService {
    @Override
    public void followUp() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        pointObject.setExchangeScore(pointObject.getExchangeScore() + 3);
        pointObject.setScoreTotal(pointObject.getScoreTotal() + 3);
        String content = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", content);
        System.out.println("+++++followUp积分计算方法执行+++++");
    }
}

