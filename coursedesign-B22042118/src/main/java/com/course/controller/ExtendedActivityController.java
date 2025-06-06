package com.course.controller;

import com.course.service.ExtendedActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 参加扩展活动
 */
@Component
public class ExtendedActivityController {

    @Autowired
    private ExtendedActivityService extendedActivityService;
    public void extendedActivity(){
        extendedActivityService.extendedActivity();
    }

}
