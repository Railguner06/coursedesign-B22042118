package com.course;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {
    @Autowired
    private BfzNoteService bfzNoteService;
    
    @Autowired
    private BloodSugarService bloodSugarService;
    
    @Autowired
    private EvaluateReportService evaluateReportService;
    
    @Autowired
    private ExtendedActivityService extendedActivityService;
    
    @Autowired
    private FillInformationService fillInformationService;
    
    @Autowired
    private FollowUpService followUpService;
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private ResearchRecruitmentService researchRecruitmentService;
    
    @Autowired
    private TestDesignService testDesignService;
    
    @Autowired
    private YdgnNoteService ydgnNoteService;
    
    @Autowired
    private GrowScoreRatingService growScoreRatingService;

    @Before
    public void setUp() {
        // 初始化积分对象
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
        FileUtils.writeFile("score", json);
    }

    @Test
    public void testCompleteWorkflowWithExchangePoints() {
        int initialScore = getCurrentTotalScore();
        int initialExchangeScore = getCurrentExchangeScore();
        assertEquals("Initial score should be 0", 0, initialScore);
        assertEquals("Initial exchange score should be 0", 0, initialExchangeScore);
        
        // 完整的工作流程测试，包括成长积分和兑换积分
        // 1. 登录 +1 成长积分
        loginService.testDesign();
        assertEquals("After login", 1, getCurrentTotalScore());
        assertEquals("After login exchange", 0, getCurrentExchangeScore());
        
        // 2. 填写个人资料 +2 成长积分
        fillInformationService.fillInformation();
        assertEquals("After fill information", 3, getCurrentTotalScore());
        assertEquals("After fill information exchange", 0, getCurrentExchangeScore());
        
        // 3. 记录血糖10次 +7 成长积分 (前3次不计分)
        for (int i = 0; i < 10; i++) {
            bloodSugarService.bloodSugar();
        }
        assertEquals("After 10 blood sugar records", 10, getCurrentTotalScore());
        assertEquals("After blood sugar exchange", 0, getCurrentExchangeScore());
        
        // 4. 生成评估报告 +2 成长积分
        evaluateReportService.evaluateReport();
        assertEquals("After evaluate report", 12, getCurrentTotalScore());
        assertEquals("After evaluate report exchange", 0, getCurrentExchangeScore());
        
        // 5. 并发症记录 +3 成长积分
        bfzNoteService.bfzNote();
        assertEquals("After bfz note", 15, getCurrentTotalScore());
        assertEquals("After bfz note exchange", 0, getCurrentExchangeScore());
        
        // 6. 胰岛功能监测 +2 成长积分
        ydgnNoteService.ydgnNote();
        assertEquals("After ydgn note", 17, getCurrentTotalScore());
        assertEquals("After ydgn note exchange", 0, getCurrentExchangeScore());
        
        // 7. 测试设计案例 +1 成长积分
        testDesignService.testDesign();
        assertEquals("After test design", 18, getCurrentTotalScore());
        assertEquals("After test design exchange", 0, getCurrentExchangeScore());
        
        // 8. 扩展活动 +5 兑换积分
        extendedActivityService.extendedActivity();
        assertEquals("After extended activity", 23, getCurrentTotalScore());
        assertEquals("After extended activity exchange", 5, getCurrentExchangeScore());
        
        // 9. 随访 +3 兑换积分
        followUpService.followUp();
        assertEquals("After follow up", 26, getCurrentTotalScore());
        assertEquals("After follow up exchange", 8, getCurrentExchangeScore());
        
        // 10. 研究招募 +8 兑换积分
        researchRecruitmentService.researchRecruitment();
        assertEquals("Final total score", 34, getCurrentTotalScore());
        assertEquals("Final exchange score", 16, getCurrentExchangeScore());
        
        // 验证最终状态
        PointObject pointObject = getCurrentPointObject();
        assertEquals("Final grow score should be 18", 18, pointObject.getGrowScore().intValue());
        assertEquals("Final exchange score should be 16", 16, pointObject.getExchangeScore().intValue());
        assertEquals("Final total score should be 34", 34, pointObject.getScoreTotal().intValue());
    }

    @Test
    public void testGrowScoreRatingIntegration() {
        // 测试成长积分评级与实际积分累积
        assertNotNull("GrowScoreRatingService should not be null", growScoreRatingService);
        
        // 初始应为C级
        String rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("Initial rating should be C", "C", rating);
        
        // 累积积分达到B级
        loginService.testDesign(); // +1
        fillInformationService.fillInformation(); // +2
        for (int i = 0; i < 10; i++) {
            bloodSugarService.bloodSugar(); // +7 (前3次不计)
        }
        testDesignService.testDesign(); // +1
        // 总成长积分: 11
        
        rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("With 11 grow points should be B rating", "B", rating);
        
        // 再次累积积分达到A级
        evaluateReportService.evaluateReport(); // +2
        bfzNoteService.bfzNote(); // +3
        ydgnNoteService.ydgnNote(); // +2
        for (int i = 0; i < 10; i++) {
            testDesignService.testDesign(); // +10
        }
        // 总成长积分: 28
        
        rating = growScoreRatingService.getGrowScoreRating();
        assertEquals("With 28 grow points should be A rating", "A", rating);
    }

    @Test
    public void testExchangePointsAccumulation() {
        // 测试兑换积分的正确累积
        int initialExchange = getCurrentExchangeScore();
        
        // 高价值兑换活动
        researchRecruitmentService.researchRecruitment(); // +8
        assertEquals("After research recruitment", 8, getCurrentExchangeScore() - initialExchange);
        
        extendedActivityService.extendedActivity(); // +5
        assertEquals("After extended activity", 13, getCurrentExchangeScore() - initialExchange);
        
        followUpService.followUp(); // +3
        assertEquals("After follow up", 16, getCurrentExchangeScore() - initialExchange);
        
        // 多次随访应累积
        followUpService.followUp(); // +3
        followUpService.followUp(); // +3
        assertEquals("After multiple follow-ups", 22, getCurrentExchangeScore() - initialExchange);
        
        // 验证总积分包含成长积分和兑换积分
        int totalScore = getCurrentTotalScore();
        int exchangeScore = getCurrentExchangeScore();
        assertTrue("Total score should include exchange points", totalScore >= exchangeScore);
    }

    @Test
    public void testScoreConsistency() {
        // 测试积分数据一致性
        PointObject pointObject = getCurrentPointObject();
        
        assertNotNull("PointObject should not be null", pointObject);
        assertEquals("GrowScore should equal ScoreTotal initially", 
                    pointObject.getGrowScore(), pointObject.getScoreTotal());
        
        // 执行一些操作，涉及成长积分和兑换积分
        loginService.testDesign(); // 成长积分
        extendedActivityService.extendedActivity(); // 兑换积分
        bloodSugarService.bloodSugar();
        bloodSugarService.bloodSugar();
        bloodSugarService.bloodSugar();
        bloodSugarService.bloodSugar(); // 第4次应增加成长积分
        
        pointObject = getCurrentPointObject();
        int expectedTotal = pointObject.getGrowScore() + pointObject.getExchangeScore();
        assertEquals("ScoreTotal should equal GrowScore + ExchangeScore", 
                    expectedTotal, pointObject.getScoreTotal().intValue());
    }

    @Test
    public void testServiceIntegration() {
        // 测试服务间的集成
        assertNotNull("All services should be autowired", bfzNoteService);
        assertNotNull("All services should be autowired", bloodSugarService);
        assertNotNull("All services should be autowired", evaluateReportService);
        assertNotNull("All services should be autowired", extendedActivityService);
        assertNotNull("All services should be autowired", fillInformationService);
        assertNotNull("All services should be autowired", followUpService);
        assertNotNull("All services should be autowired", loginService);
        assertNotNull("All services should be autowired", researchRecruitmentService);
        assertNotNull("All services should be autowired", testDesignService);
        assertNotNull("All services should be autowired", ydgnNoteService);
        assertNotNull("All services should be autowired", growScoreRatingService);
        
        // 验证所有服务可以正常执行
        int initialScore = getCurrentTotalScore();
        testDesignService.testDesign();
        extendedActivityService.extendedActivity();
        int finalScore = getCurrentTotalScore();
        assertTrue("Service integration should work", finalScore > initialScore);
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
