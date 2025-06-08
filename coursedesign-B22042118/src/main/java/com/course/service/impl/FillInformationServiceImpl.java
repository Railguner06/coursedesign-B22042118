package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.FillInformationService;
import org.springframework.stereotype.Service;
/**
 * 填写个人资料
 */
@Service
public class FillInformationServiceImpl implements FillInformationService {
    @Override
    public void fillInformation() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        if (!pointObject.isFirstFillInfo()) {
            pointObject.setGrowScore(pointObject.getGrowScore() + 2);
            pointObject.setScoreTotal(pointObject.getScoreTotal() + 2);
            pointObject.setFirstFillInfo(true);
            String content = JsonUtils.objectToJson(pointObject);
            FileUtils.writeFile("score", content);
            System.out.println("+++++fillInformation积分计算方法执行+++++");
            System.out.println(pointObject);
        }
    }
}