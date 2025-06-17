package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.EvaluateReportService;
import org.springframework.stereotype.Service;
/**
 * 生成评估报告
 */
@Service
public class EvaluateReportServiceImpl implements EvaluateReportService {
    @Override
    public void evaluateReport() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        if (pointObject.isFirstFillInfo() && pointObject.getBloodSugarRecordCount() >= 10) {
            pointObject.setGrowScore(pointObject.getGrowScore() + 2);
            pointObject.setScoreTotal(pointObject.getScoreTotal() + 2);
            pointObject.setEvaluateReportBloodSugarCount(pointObject.getBloodSugarRecordCount());
            String content = JsonUtils.objectToJson(pointObject);
            FileUtils.writeFile("score", content);
            System.out.println("+++++evaluateReport积分计算方法执行+++++");
            System.out.println(pointObject);
        }
    }
}
