package com.course.controller;

import com.course.service.FollowUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 完成门诊随访
 */
@Component
public class FollowUpController {

    @Autowired
    private FollowUpService followUpService;
    public void followUp(){
        followUpService.followUp();
    }

}
