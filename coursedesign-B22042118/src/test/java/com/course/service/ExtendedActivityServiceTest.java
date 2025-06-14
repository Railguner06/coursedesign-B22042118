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
public class ExtendedActivityServiceTest {
    
    @Autowired
    private ExtendedActivityService extendedActivityService;

    @Before
    public void setUp() {
        PointObject pointObject = new PointObject();
        pointObject.setId(1);
        pointObject.setGrowScore(0);
        pointObject.setExchangeScore(0);
        pointObject.setScoreTotal(0);

        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
    }

    @Test
    public void testExtendedActivityService() {
        int initialScore = getCurrentTotalScore();
        int initialExchangeScore = getCurrentExchangeScore();
        assertEquals("Initial total score should be 0", 0, initialScore);
        assertEquals("Initial exchange score should be 0", 0, initialExchangeScore);
        
        assertNotNull("ExtendedActivityService should not be null", extendedActivityService);
        
        extendedActivityService.extendedActivity();
        
        int finalScore = getCurrentTotalScore();
        int finalExchangeScore = getCurrentExchangeScore();
        assertEquals("ExtendedActivity should add 5 points to total", 5, finalScore - initialScore);
        assertEquals("ExtendedActivity should add 5 points to exchange score", 5, finalExchangeScore - initialExchangeScore);
        
        // Test multiple executions
        extendedActivityService.extendedActivity();
        int scoreAfterSecond = getCurrentTotalScore();
        int exchangeScoreAfterSecond = getCurrentExchangeScore();
        assertEquals("Second extendedActivity should add another 5 points to total", 10, scoreAfterSecond);
        assertEquals("Second extendedActivity should add another 5 points to exchange", 10, exchangeScoreAfterSecond);
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

    private int getCurrentExchangeScore() {
        try {
            String file = FileUtils.readFile("score");
            PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
            return pointObject.getExchangeScore() != null ? pointObject.getExchangeScore() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
