package com.course.service.impl;

import com.course.service.BloodSugarService;
import org.springframework.stereotype.Service;
/**
 * 记录血糖
 */
@Service
public class BloodSugarServiceImpl implements BloodSugarService {
    public void bloodSugar(){
        System.out.println("+++++bloodSugar积分计算方法执行+++++");
    }
}
