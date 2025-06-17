package com.course.service.impl;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.GrowScoreRatingService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class GrowScoreRatingServiceImpl implements GrowScoreRatingService {
    @Override
    public String getGrowScoreRating() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date monthlyStartDate = calendar.getTime();
        if (pointObject.getMonthlyGrowScoreStartDate() == null || pointObject.getMonthlyGrowScoreStartDate().before(monthlyStartDate)) {
            pointObject.setMonthlyGrowScoreStartDate(monthlyStartDate);
            pointObject.setGrowScore(0);
            String content = JsonUtils.objectToJson(pointObject);
            FileUtils.writeFile("score", content);
        }
        int growScore = pointObject.getGrowScore();
        if (growScore >= 0 && growScore <= 10) {
            return "C";
        } else if (growScore >= 11 && growScore <= 25) {
            return "B";
        } else {
            return "A";
        }
    }
}
