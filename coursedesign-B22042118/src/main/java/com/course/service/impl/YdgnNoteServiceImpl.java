package com.course.service.impl;

import com.course.service.YdgnNoteService;
import org.springframework.stereotype.Service;
/**
 * 监测胰岛功能
 */
@Service
public class YdgnNoteServiceImpl implements YdgnNoteService {
    public void ydgnNote(){
        System.out.println("+++++ydgnNote积分计算方法执行+++++");
    }
}
