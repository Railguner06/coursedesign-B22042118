package com.course.controller;

import com.course.service.YdgnNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监测胰岛功能
 */
@Component
public class YdgnNoteController {

    @Autowired
    private YdgnNoteService ydgnNoteService;
    public void ydgnNote(){
        ydgnNoteService.ydgnNote();
    }

}
