package com.course.service.impl;

import com.course.service.FollowUpService;
import org.springframework.stereotype.Service;
/**
 * 完成门诊随访
 */
@Service
public class FollowUpServiceImpl implements FollowUpService {
    public void followUp(){
        System.out.println("+++++followUp积分计算方法执行+++++");
    }
}
