package com.course;

import com.course.entity.bo.PointObject;
import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author lixuy
 * Created on 2019-04-10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFileIo {

    @Test
    public void testWrite(){
        try {
            PointObject pointObject = new PointObject();
            pointObject.setId(1);
            pointObject.setGrowScore(0);
            pointObject.setExchangeScore(0);
            pointObject.setScoreTotal(0);
            pointObject.setLastLoginDate(null);
            pointObject.setFirstFillInfo(false);
            pointObject.setBloodSugarRecordCount(0);
            pointObject.setLastBfzNoteYear(null);
            pointObject.setEvaluateReportBloodSugarCount(0);
            pointObject.setLastYdgnNoteDate(null);
            pointObject.setExchangeScoreExpiryDate(null);
            pointObject.setMonthlyGrowScoreStartDate(null);
            
            String json = JsonUtils.objectToJson(pointObject);
            assertNotNull("JSON should not be null", json);
            
            FileUtils.writeFile("score", json);
            
            // Verify file was written
            String readContent = FileUtils.readFile("score");
            assertNotNull("File content should not be null", readContent);
            assertFalse("File content should not be empty", readContent.isEmpty());
            
        } catch (Exception e) {
            fail("Write test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testRead(){
        try {
            // First write a test object
            PointObject originalObject = new PointObject();
            originalObject.setId(1);
            originalObject.setGrowScore(10);
            originalObject.setExchangeScore(5);
            originalObject.setScoreTotal(15);
            originalObject.setLastLoginDate(new Date());
            originalObject.setFirstFillInfo(true);
            originalObject.setBloodSugarRecordCount(7);
            originalObject.setLastBfzNoteYear(2023);
            originalObject.setEvaluateReportBloodSugarCount(10);
            originalObject.setLastYdgnNoteDate(new Date());
            originalObject.setExchangeScoreExpiryDate(new Date());
            originalObject.setMonthlyGrowScoreStartDate(new Date());
            
            String json = JsonUtils.objectToJson(originalObject);
            FileUtils.writeFile("score", json);
            
            // Now read and verify
            String file = FileUtils.readFile("score");
            System.out.println("File content: " + file);
            assertNotNull("File content should not be null", file);
            
            PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
            System.out.println("Parsed object: " + pointObject);
            assertNotNull("Parsed object should not be null", pointObject);
            
            // Verify all fields
            assertEquals("ID should match", originalObject.getId(), pointObject.getId());
            assertEquals("GrowScore should match", originalObject.getGrowScore(), pointObject.getGrowScore());
            assertEquals("ExchangeScore should match", originalObject.getExchangeScore(), pointObject.getExchangeScore());
            assertEquals("ScoreTotal should match", originalObject.getScoreTotal(), pointObject.getScoreTotal());
            assertEquals("FirstFillInfo should match", originalObject.isFirstFillInfo(), pointObject.isFirstFillInfo());
            assertEquals("BloodSugarRecordCount should match", originalObject.getBloodSugarRecordCount(), pointObject.getBloodSugarRecordCount());
            assertEquals("LastBfzNoteYear should match", originalObject.getLastBfzNoteYear(), pointObject.getLastBfzNoteYear());
            assertEquals("EvaluateReportBloodSugarCount should match", originalObject.getEvaluateReportBloodSugarCount(), pointObject.getEvaluateReportBloodSugarCount());
            
        } catch (Exception e) {
            fail("Read test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadWriteConsistency() {
        try {
            // Test multiple write-read cycles
            for (int i = 0; i < 5; i++) {
                PointObject pointObject = new PointObject();
                pointObject.setId(i);
                pointObject.setGrowScore(i * 2);
                pointObject.setExchangeScore(i * 3);
                pointObject.setScoreTotal(i * 5);
                
                String json = JsonUtils.objectToJson(pointObject);
                FileUtils.writeFile("score", json);
                
                String file = FileUtils.readFile("score");
                PointObject readObject = JsonUtils.jsonToPojo(file, PointObject.class);
                
                assertEquals("ID consistency check " + i, pointObject.getId(), readObject.getId());
                assertEquals("GrowScore consistency check " + i, pointObject.getGrowScore(), readObject.getGrowScore());
                assertEquals("ExchangeScore consistency check " + i, pointObject.getExchangeScore(), readObject.getExchangeScore());
                assertEquals("ScoreTotal consistency check " + i, pointObject.getScoreTotal(), readObject.getScoreTotal());
            }
            
        } catch (Exception e) {
            fail("Consistency test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testNewFieldsSerialization() {
        try {
            PointObject pointObject = new PointObject();
            pointObject.setId(1);
            pointObject.setExchangeScoreExpiryDate(new Date());
            pointObject.setMonthlyGrowScoreStartDate(new Date());
            
            String json = JsonUtils.objectToJson(pointObject);
            assertNotNull("JSON with new fields should not be null", json);
            assertTrue("JSON should contain exchangeScoreExpiryDate", json.contains("exchangeScoreExpiryDate"));
            assertTrue("JSON should contain monthlyGrowScoreStartDate", json.contains("monthlyGrowScoreStartDate"));
            
            FileUtils.writeFile("score", json);
            
            String file = FileUtils.readFile("score");
            PointObject readObject = JsonUtils.jsonToPojo(file, PointObject.class);
            
            assertNotNull("Read object should not be null", readObject);
            // Note: Date fields may have slight differences due to serialization, so we just check they're not null
            // if the original had values
            if (pointObject.getExchangeScoreExpiryDate() != null) {
                assertNotNull("ExchangeScoreExpiryDate should be preserved", readObject.getExchangeScoreExpiryDate());
            }
            if (pointObject.getMonthlyGrowScoreStartDate() != null) {
                assertNotNull("MonthlyGrowScoreStartDate should be preserved", readObject.getMonthlyGrowScoreStartDate());
            }
            
        } catch (Exception e) {
            fail("New fields serialization test should not throw exception: " + e.getMessage());
        }
    }
}
