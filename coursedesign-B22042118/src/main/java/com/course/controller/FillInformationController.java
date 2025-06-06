package com.course.controller;

import com.course.service.FillInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 填写个人资料
 */
@Component
public class FillInformationController {

    @Autowired
    private FillInformationService fillInformationService;
    public void fillInformation(){
        fillInformationService.fillInformation();
    }

}
