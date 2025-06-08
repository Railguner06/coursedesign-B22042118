package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.LoginService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * 登录平台
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public void testDesign() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();
        if (pointObject.getLastLoginDate() == null ||!pointObject.getLastLoginDate().equals(today)) {
            pointObject.setGrowScore(pointObject.getGrowScore() + 1);
            pointObject.setScoreTotal(pointObject.getScoreTotal() + 1);
            pointObject.setLastLoginDate(today);
            String content = JsonUtils.objectToJson(pointObject);
            FileUtils.writeFile("score", content);
            System.out.println("+++++login积分计算方法执行+++++");
            System.out.println(pointObject);
        }
    }
}
