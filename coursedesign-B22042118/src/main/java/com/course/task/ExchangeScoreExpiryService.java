package com.course.task;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class ExchangeScoreExpiryService {
    @Scheduled(cron = "0 0 0 1 * ?") // 每月1号凌晨执行
    public void handleExchangeScoreExpiry() {
        String file = FileUtils.readFile("score");
        PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date oneYearAgo = calendar.getTime();
        if (pointObject.getExchangeScoreExpiryDate() != null && pointObject.getExchangeScoreExpiryDate().before(oneYearAgo)) {
            pointObject.setExchangeScore(0);
            pointObject.setExchangeScoreExpiryDate(new Date());
            String content = JsonUtils.objectToJson(pointObject);
            FileUtils.writeFile("score", content);
            System.out.println("+++++可兑换积分已清零+++++");
        }
    }
}
