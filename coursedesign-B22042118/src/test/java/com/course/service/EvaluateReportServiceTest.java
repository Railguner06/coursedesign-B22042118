package com.course.service;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EvaluateReportServiceTest {
    
    @Autowired
    private EvaluateReportService evaluateReportService;

    @Before
    public void setUp() {
        PointObject pointObject = new PointObject();
        pointObject.setId(1);
        pointObject.setGrowScore(0);
        pointObject.setExchangeScore(0);
        pointObject.setScoreTotal(0);
        pointObject.setFirstFillInfo(false);
        pointObject.setBloodSugarRecordCount(0);
        pointObject.setEvaluateReportBloodSugarCount(0);

        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
    }

    @Test
    public void testEvaluateReportService() {
        assertNotNull("EvaluateReportService should not be null", evaluateReportService);
        
        int initialScore = getCurrentTotalScore();
        evaluateReportService.evaluateReport();
        int scoreAfterFirst = getCurrentTotalScore();
        assertEquals("EvaluateReport should not add points when conditions not met", 0, scoreAfterFirst - initialScore);
        
        PointObject pointObject = getCurrentPointObject();
        pointObject.setFirstFillInfo(true);
        pointObject.setBloodSugarRecordCount(10);
        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
        
        int scoreBeforeValid = getCurrentTotalScore();
        evaluateReportService.evaluateReport();
        int scoreAfterValid = getCurrentTotalScore();
        assertEquals("EvaluateReport should add 2 points when conditions are met", 2, scoreAfterValid - scoreBeforeValid);
        
        pointObject = getCurrentPointObject();
        assertEquals("EvaluateReportBloodSugarCount should be 10", 10, pointObject.getEvaluateReportBloodSugarCount().intValue());
    }

    private int getCurrentTotalScore() {
        try {
            String file = FileUtils.readFile("score");
            PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
            return pointObject.getScoreTotal() != null ? pointObject.getScoreTotal() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private PointObject getCurrentPointObject() {
        try {
            String file = FileUtils.readFile("score");
            return JsonUtils.jsonToPojo(file, PointObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
