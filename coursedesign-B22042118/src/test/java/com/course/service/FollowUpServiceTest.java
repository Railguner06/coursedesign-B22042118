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
public class FollowUpServiceTest {
    
    @Autowired
    private FollowUpService followUpService;

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
    public void testFollowUpService() {
        int initialScore = getCurrentTotalScore();
        int initialExchangeScore = getCurrentExchangeScore();
        assertEquals("Initial total score should be 0", 0, initialScore);
        assertEquals("Initial exchange score should be 0", 0, initialExchangeScore);
        
        assertNotNull("FollowUpService should not be null", followUpService);
        
        followUpService.followUp();
        
        int finalScore = getCurrentTotalScore();
        int finalExchangeScore = getCurrentExchangeScore();
        assertEquals("FollowUp should add 3 points to total", 3, finalScore - initialScore);
        assertEquals("FollowUp should add 3 points to exchange score", 3, finalExchangeScore - initialExchangeScore);
        
        // Test multiple follow-ups
        followUpService.followUp();
        followUpService.followUp();
        int scoreAfterMultiple = getCurrentTotalScore();
        int exchangeScoreAfterMultiple = getCurrentExchangeScore();
        assertEquals("Multiple followUps should accumulate total points", 9, scoreAfterMultiple);
        assertEquals("Multiple followUps should accumulate exchange points", 9, exchangeScoreAfterMultiple);
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
