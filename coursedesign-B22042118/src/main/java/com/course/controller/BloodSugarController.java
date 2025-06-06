package com.course.controller;

import com.course.service.BloodSugarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 记录血糖
 */
@Component
public class BloodSugarController {

    @Autowired
    private BloodSugarService bloodSugarService;
    public void bloodSugar(){
        bloodSugarService.bloodSugar();
    }

}
