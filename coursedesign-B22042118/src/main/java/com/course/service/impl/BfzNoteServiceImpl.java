package com.course.service.impl;

import com.course.service.BfzNoteService;
import org.springframework.stereotype.Service;
/**
 * 并发症记录
 */
@Service
public class BfzNoteServiceImpl implements BfzNoteService {
    public void bfzNote() {
        System.out.println("+++++bfzNote积分计算方法执行+++++");
    }
}