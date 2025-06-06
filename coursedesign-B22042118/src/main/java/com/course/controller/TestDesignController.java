package com.course.controller;

import com.course.service.TestDesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 测试案例
 */
@Component
public class TestDesignController {

    @Autowired
    private TestDesignService testDesignService;
    public void testDesign(){
        testDesignService.testDesign();
    }

}
