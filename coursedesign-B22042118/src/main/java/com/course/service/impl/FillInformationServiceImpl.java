package com.course.service.impl;

import com.course.service.FillInformationService;
import org.springframework.stereotype.Service;
/**
 * 填写个人资料
 */
@Service
public class FillInformationServiceImpl implements FillInformationService {
    public void fillInformation(){
        System.out.println("+++++fillInformation积分计算方法执行+++++");
    }
}
