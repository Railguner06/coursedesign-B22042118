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
public class ResearchRecruitmentServiceTest {
    
    @Autowired
    private ResearchRecruitmentService researchRecruitmentService;

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
    public void testResearchRecruitmentService() {
        int initialScore = getCurrentTotalScore();
        int initialExchangeScore = getCurrentExchangeScore();
        assertEquals("Initial total score should be 0", 0, initialScore);
        assertEquals("Initial exchange score should be 0", 0, initialExchangeScore);
        
        assertNotNull("ResearchRecruitmentService should not be null", researchRecruitmentService);
        
        researchRecruitmentService.researchRecruitment();
        
        int finalScore = getCurrentTotalScore();
        int finalExchangeScore = getCurrentExchangeScore();
        assertEquals("ResearchRecruitment should add 8 points to total", 8, finalScore - initialScore);
        assertEquals("ResearchRecruitment should add 8 points to exchange score", 8, finalExchangeScore - initialExchangeScore);
        
        // Test high-value activity
        researchRecruitmentService.researchRecruitment();
        int scoreAfterSecond = getCurrentTotalScore();
        int exchangeScoreAfterSecond = getCurrentExchangeScore();
        assertEquals("Second researchRecruitment should add another 8 points to total", 16, scoreAfterSecond);
        assertEquals("Second researchRecruitment should add another 8 points to exchange", 16, exchangeScoreAfterSecond);
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
