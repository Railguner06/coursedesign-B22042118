package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.ExtendedActivityService;
import org.springframework.stereotype.Service;
/**
 * 参加扩展活动
 */
@Service
public class ExtendedActivityServiceImpl implements ExtendedActivityService {
    @Override
    public void extendedActivity() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        pointObject.setExchangeScore(pointObject.getExchangeScore() + 5);
        pointObject.setScoreTotal(pointObject.getScoreTotal() + 5);
        String content = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", content);
        System.out.println("+++++extendedActivity积分计算方法执行+++++");
        System.out.println(pointObject);

    }
}
