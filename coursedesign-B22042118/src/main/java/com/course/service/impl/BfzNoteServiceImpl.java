package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.BfzNoteService;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * 并发症记录
 */
@Service
public class BfzNoteServiceImpl implements BfzNoteService {
    @Override
    public void bfzNote() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        if (pointObject.getLastBfzNoteYear() == null || pointObject.getLastBfzNoteYear() != currentYear) {
            pointObject.setGrowScore(pointObject.getGrowScore() + 3);
            pointObject.setScoreTotal(pointObject.getScoreTotal() + 3);
            pointObject.setLastBfzNoteYear(currentYear);
            String content = JsonUtils.objectToJson(pointObject);
            FileUtils.writeFile("score", content);
            System.out.println("+++++bfzNote积分计算方法执行+++++");
            System.out.println(pointObject);
        }
    }
}