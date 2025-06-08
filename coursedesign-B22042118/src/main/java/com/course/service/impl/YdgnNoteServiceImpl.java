package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.YdgnNoteService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * 监测胰岛功能
 */
@Service
public class YdgnNoteServiceImpl implements YdgnNoteService {
    @Override
    public void ydgnNote() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        Date threeMonthsAgo = calendar.getTime();
        if (pointObject.getLastYdgnNoteDate() == null || pointObject.getLastYdgnNoteDate().before(threeMonthsAgo)) {
            pointObject.setGrowScore(pointObject.getGrowScore() + 2);
            pointObject.setScoreTotal(pointObject.getScoreTotal() + 2);
            pointObject.setLastYdgnNoteDate(new Date());
            String content = JsonUtils.objectToJson(pointObject);
            FileUtils.writeFile("score", content);
            System.out.println("+++++ydgnNote积分计算方法执行+++++");
            System.out.println(pointObject);
        }
    }
}

