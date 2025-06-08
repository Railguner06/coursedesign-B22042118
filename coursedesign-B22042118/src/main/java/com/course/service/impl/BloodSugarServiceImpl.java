package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.BloodSugarService;
import org.springframework.stereotype.Service;
/**
 * 记录血糖
 */
@Service
public class BloodSugarServiceImpl implements BloodSugarService {
    @Override
    public void bloodSugar() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        pointObject.setBloodSugarRecordCount(pointObject.getBloodSugarRecordCount() + 1);
        if (pointObject.getBloodSugarRecordCount() > 3) {
            pointObject.setGrowScore(pointObject.getGrowScore() + 1);
            pointObject.setScoreTotal(pointObject.getScoreTotal() + 1);
        }
        String content = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", content);
        System.out.println("+++++bloodSugar积分计算方法执行+++++");
        System.out.println(pointObject);
    }
}
