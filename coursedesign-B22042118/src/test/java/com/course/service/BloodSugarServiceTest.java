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
public class BloodSugarServiceTest {
    
    @Autowired
    private BloodSugarService bloodSugarService;

    @Before
    public void setUp() {
        PointObject pointObject = new PointObject();
        pointObject.setId(1);
        pointObject.setGrowScore(0);
        pointObject.setExchangeScore(0);
        pointObject.setScoreTotal(0);
        pointObject.setBloodSugarRecordCount(0);

        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
    }

    @Test
    public void testBloodSugarService() {
        int initialScore = getCurrentTotalScore();
        assertEquals("Initial score should be 0", 0, initialScore);
        
        assertNotNull("BloodSugarService should not be null", bloodSugarService);
        
        for (int i = 1; i <= 3; i++) {
            bloodSugarService.bloodSugar();
            int currentScore = getCurrentTotalScore();
            assertEquals("First 3 blood sugar records should not add points", 0, currentScore - initialScore);
            
            PointObject pointObject = getCurrentPointObject();
            assertEquals("Blood sugar record count should be " + i, i, pointObject.getBloodSugarRecordCount().intValue());
        }
        
        for (int i = 4; i <= 6; i++) {
            int scoreBefore = getCurrentTotalScore();
            bloodSugarService.bloodSugar();
            int scoreAfter = getCurrentTotalScore();
            assertEquals("Blood sugar record " + i + " should add 1 point", 1, scoreAfter - scoreBefore);
        }
        
        PointObject pointObject = getCurrentPointObject();
        assertEquals("Blood sugar record count should be 6", 6, pointObject.getBloodSugarRecordCount().intValue());
        assertEquals("Total score should be 3 (records 4,5,6)", 3, getCurrentTotalScore());
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
