package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.ResearchRecruitmentService;
import org.springframework.stereotype.Service;
/**
 * 参加科研招募
 */
@Service
public class ResearchRecruitmentServiceImpl implements ResearchRecruitmentService {
    @Override
    public void researchRecruitment() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        pointObject.setExchangeScore(pointObject.getExchangeScore() + 8);
        pointObject.setScoreTotal(pointObject.getScoreTotal() + 8);
        String content = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", content);
        System.out.println("+++++researchRecruitment积分计算方法执行+++++");
        System.out.println(pointObject);
    }
}