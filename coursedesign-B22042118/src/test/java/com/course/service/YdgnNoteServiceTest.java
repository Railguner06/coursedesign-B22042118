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
public class YdgnNoteServiceTest {
    
    @Autowired
    private YdgnNoteService ydgnNoteService;

    @Before
    public void setUp() {
        PointObject pointObject = new PointObject();
        pointObject.setId(1);
        pointObject.setGrowScore(0);
        pointObject.setExchangeScore(0);
        pointObject.setScoreTotal(0);
        pointObject.setLastYdgnNoteDate(null);

        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
    }

    @Test
    public void testYdgnNoteService() {
        int initialScore = getCurrentTotalScore();
        assertEquals("Initial score should be 0", 0, initialScore);
        
        assertNotNull("YdgnNoteService should not be null", ydgnNoteService);
        
        ydgnNoteService.ydgnNote();
        
        int finalScore = getCurrentTotalScore();
        assertEquals("YdgnNote should add 2 points", 2, finalScore - initialScore);
        
        PointObject pointObject = getCurrentPointObject();
        assertNotNull("LastYdgnNoteDate should be set", pointObject.getLastYdgnNoteDate());
        
        ydgnNoteService.ydgnNote();
        int scoreAfterSecond = getCurrentTotalScore();
        assertEquals("Second ydgn note within 3 months should not add points", finalScore, scoreAfterSecond);
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
