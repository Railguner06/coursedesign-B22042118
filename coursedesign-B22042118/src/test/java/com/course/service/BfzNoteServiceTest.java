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

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BfzNoteServiceTest {
    
    @Autowired
    private BfzNoteService bfzNoteService;

    @Before
    public void setUp() {
        PointObject pointObject = new PointObject();
        pointObject.setId(1);
        pointObject.setGrowScore(0);
        pointObject.setExchangeScore(0);
        pointObject.setScoreTotal(0);
        pointObject.setLastBfzNoteYear(null);

        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
    }

    @Test
    public void testBfzNoteService() {
        int initialScore = getCurrentTotalScore();
        assertEquals("Initial score should be 0", 0, initialScore);
        
        assertNotNull("BfzNoteService should not be null", bfzNoteService);
        
        bfzNoteService.bfzNote();
        
        int finalScore = getCurrentTotalScore();
        assertEquals("BfzNote should add 3 points", 3, finalScore - initialScore);
        
        PointObject pointObject = getCurrentPointObject();
        assertNotNull("LastBfzNoteYear should be set", pointObject.getLastBfzNoteYear());
        
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        assertEquals("LastBfzNoteYear should be current year", currentYear, pointObject.getLastBfzNoteYear().intValue());
        
        bfzNoteService.bfzNote();
        int scoreAfterSecond = getCurrentTotalScore();
        assertEquals("Second bfz note in same year should not add points", finalScore, scoreAfterSecond);
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
