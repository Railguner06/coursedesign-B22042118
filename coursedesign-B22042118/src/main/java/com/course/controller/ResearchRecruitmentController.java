package com.course.controller;

import com.course.service.ResearchRecruitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 参加科研招募
 */
@Component
public class ResearchRecruitmentController {

    @Autowired
    private ResearchRecruitmentService researchRecruitmentService;
    public void researchRecruitment(){
        researchRecruitmentService.researchRecruitment();
    }

}
