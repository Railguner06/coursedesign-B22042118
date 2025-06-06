package com.course.service.impl;

import com.course.service.EvaluateReportService;
import org.springframework.stereotype.Service;
/**
 * 生成评估报告
 */
@Service
public class EvaluateReportServiceImpl implements EvaluateReportService {
    public void evaluateReport(){
        System.out.println("+++++evaluateReport积分计算方法执行+++++");
    }
}
