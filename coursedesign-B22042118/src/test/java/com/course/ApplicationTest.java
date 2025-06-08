package com.course;

import com.course.common.utils.FileUtils;
import com.course.common.utils.JsonUtils;
import com.course.entity.bo.PointObject;
import com.course.service.BfzNoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {
    @Autowired
    private BfzNoteService bfzNoteService;

    @Test
    public void testBfzNoteService() {
        PointObject pointObject = new PointObject();
        pointObject.setId(1);
        pointObject.setGrowScore(0);
        pointObject.setExchangeScore(0);
        pointObject.setScoreTotal(0);
        pointObject.setLastLoginDate(new Date());

        // 将积分对象转换为 JSON 字符串
        String json = JsonUtils.objectToJson(pointObject);

        // 将 JSON 字符串写入 score 文件
        FileUtils.writeFile("score", json);
        if (bfzNoteService != null) {
            bfzNoteService.bfzNote();
        } else {
            System.err.println("bfzNoteService is null");
        }
    }
}
