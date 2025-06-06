package com.course.service.impl;

import com.course.service.ExtendedActivityService;
import org.springframework.stereotype.Service;
/**
 * 参加扩展活动
 */
@Service
public class ExtendedActivityServiceImpl implements ExtendedActivityService {
    public void extendedActivity(){
        System.out.println("+++++extendedActivity积分计算方法执行+++++");
    }

}
