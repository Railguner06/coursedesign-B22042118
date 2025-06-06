package com.course.controller;

import com.course.service.EvaluateReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成评估报告
 */
@Component
public class EvaluateReportController {

    @Autowired
    private EvaluateReportService evaluateReportService;
    public void evaluateReport(){
        evaluateReportService.evaluateReport();
    }

}
