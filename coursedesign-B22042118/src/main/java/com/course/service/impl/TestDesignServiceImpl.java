package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.TestDesignService;
import org.springframework.stereotype.Service;
/**
 * 测试案例
 */
@Service
public class TestDesignServiceImpl implements TestDesignService {
    public void testDesign(){
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        Integer grow = pointObject.getGrowScore();
        Integer total = pointObject.getScoreTotal();
        pointObject.setGrowScore(grow+1);
        pointObject.setScoreTotal(total+1);
        String content = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", content);
        System.out.println("+++++积分计算方法+++++");
    }
}
