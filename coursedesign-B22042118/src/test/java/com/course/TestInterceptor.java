package com.course;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.controller.TestDesignController;
import com.course.entity.bo.PointObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author lixuy
 * Created on 2019-04-10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestInterceptor {

    @Autowired
    TestDesignController testDesign;

    //检验当前积分情况
    private int assertScore(){
        try {
            String file = FileUtils.readFile("score");
            PointObject pointObject = JsonUtils.jsonToPojo(file,PointObject.class);
            System.out.println("成长积分："+pointObject.getGrowScore());
            System.out.println("可交换积分："+pointObject.getExchangeScore());
            System.out.println("总积分："+pointObject.getScoreTotal());

            return pointObject.getScoreTotal();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("this is setUpBeforeClass...");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("this is tearDownAfterClass...");
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("this is setUp...");
        // Initialize score file before each test
        PointObject pointObject = new PointObject();
        pointObject.setId(1);
        pointObject.setGrowScore(0);
        pointObject.setExchangeScore(0);
        pointObject.setScoreTotal(0);
        String json = JsonUtils.objectToJson(pointObject);
        FileUtils.writeFile("score", json);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("this is tearDown...");
    }

    @Test
    public void testDesign() {
        try {
            assertNotNull("TestDesignController should not be null", testDesign);
            
            int score1 = assertScore();
            assertEquals("Initial score should be 0", 0, score1);
            
            testDesign.testDesign();
            int score2 = assertScore();

            assertEquals("TestDesign should add 1 point", 1, score2 - score1);
            
            // Test that grow score and total score are consistent
            String file = FileUtils.readFile("score");
            PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
            assertEquals("Grow score should equal total score", pointObject.getGrowScore(), pointObject.getScoreTotal());
            
        } catch (Exception e) {
            fail("Test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testInterceptorMultipleCalls() {
        try {
            int initialScore = assertScore();
            
            // Call multiple times to test interceptor consistency
            for (int i = 1; i <= 3; i++) {
                testDesign.testDesign();
                int currentScore = assertScore();
                assertEquals("After " + i + " calls, score should be " + i, i, currentScore - initialScore);
            }
            
        } catch (Exception e) {
            fail("Multiple calls test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testScoreObjectIntegrity() {
        try {
            testDesign.testDesign();
            
            String file = FileUtils.readFile("score");
            PointObject pointObject = JsonUtils.jsonToPojo(file, PointObject.class);
            
            assertNotNull("PointObject should not be null after operation", pointObject);
            assertNotNull("GrowScore should not be null", pointObject.getGrowScore());
            assertNotNull("ExchangeScore should not be null", pointObject.getExchangeScore());
            assertNotNull("ScoreTotal should not be null", pointObject.getScoreTotal());
            
            assertTrue("GrowScore should be positive", pointObject.getGrowScore() > 0);
            assertEquals("ExchangeScore should be 0 for TestDesign", 0, pointObject.getExchangeScore().intValue());
            assertEquals("ScoreTotal should equal GrowScore", pointObject.getGrowScore(), pointObject.getScoreTotal());
            
        } catch (Exception e) {
            fail("Score object integrity test should not throw exception: " + e.getMessage());
        }
    }
}
