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

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GrowScoreRatingServiceTest {
    
    @Autowired
    private GrowScoreRatingService growScoreRatingService;

    @Before
    public void setUp() {
        PointObject pointObject = new PointObject();
        pointObject.setId(1);
        pointObject.setGrowScore(0);
        pointObject.setExchangeScore(0);
        pointObject.setScoreTotal(0);
        pointObject.setMonthlyGrowScoreStartDate(null);

        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
    }

    @Test
    public void testGrowScoreRatingC() {
        // Test C rating (0-10 points)
        setGrowScore(5);
        String rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Grow score 5 should get C rating", "C", rating);
        
        setGrowScore(0);
        rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Grow score 0 should get C rating", "C", rating);
        
        setGrowScore(10);
        rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Grow score 10 should get C rating", "C", rating);
    }

    @Test
    public void testGrowScoreRatingB() {
        // Test B rating (11-25 points)
        setGrowScore(11);
        String rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Grow score 11 should get B rating", "B", rating);
        
        setGrowScore(20);
        rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Grow score 20 should get B rating", "B", rating);
        
        setGrowScore(25);
        rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Grow score 25 should get B rating", "B", rating);
    }

    @Test
    public void testGrowScoreRatingA() {
        // Test A rating (26+ points)
        setGrowScore(26);
        String rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Grow score 26 should get A rating", "A", rating);
        
        setGrowScore(50);
        rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Grow score 50 should get A rating", "A", rating);
        
        setGrowScore(100);
        rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Grow score 100 should get A rating", "A", rating);
    }

    @Test
    public void testMonthlyReset() {
        // Test monthly grow score reset functionality
        setGrowScore(15);
        
        // Set monthly start date to last month
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.add(Calendar.MONTH, -1);
        Date lastMonthDate = lastMonth.getTime();
        
        PointObject pointObject = getCurrentPointObject();
        pointObject.setMonthlyGrowScoreStartDate(lastMonthDate);
        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
        
        // Call rating service should reset grow score
        String rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("After monthly reset, score should be 0 (C rating)", "C", rating);
        
        // Verify the grow score was reset to 0
        pointObject = getCurrentPointObject();
        assertEquals("Grow score should be reset to 0", 0, pointObject.getGrowScore().intValue());
        assertNotNull("Monthly start date should be updated", pointObject.getMonthlyGrowScoreStartDate());
    }

    @Test
    public void testNoResetWithinMonth() {
        // Test that grow score is not reset within the same month
        setGrowScore(20);
        
        // Set monthly start date to beginning of current month
        Calendar currentMonth = Calendar.getInstance();
        currentMonth.set(Calendar.DAY_OF_MONTH, 1);
        currentMonth.set(Calendar.HOUR_OF_DAY, 0);
        currentMonth.set(Calendar.MINUTE, 0);
        currentMonth.set(Calendar.SECOND, 0);
        currentMonth.set(Calendar.MILLISECOND, 0);
        Date currentMonthDate = currentMonth.getTime();
        
        PointObject pointObject = getCurrentPointObject();
        pointObject.setMonthlyGrowScoreStartDate(currentMonthDate);
        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
        
        // Call rating service should not reset grow score
        String rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Within same month, score should remain 20 (B rating)", "B", rating);
        
        // Verify the grow score was not reset
        pointObject = getCurrentPointObject();
        assertEquals("Grow score should remain 20", 20, pointObject.getGrowScore().intValue());
    }

    private void setGrowScore(int score) {
        try {
            String file = FileUtils.readFile("score");
            PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
            pointObject.setGrowScore(score);
            String json = JsonUtils.objectToJson(pointObject);
            FileUtils.writeFile("score", json);
        } catch (Exception e) {
            e.printStackTrace();
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
