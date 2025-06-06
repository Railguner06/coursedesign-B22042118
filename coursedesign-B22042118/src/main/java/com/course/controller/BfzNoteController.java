package com.course.controller;

import com.course.service.BfzNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 并发症记录
 */
@Component
public class BfzNoteController {

    @Autowired
    private BfzNoteService bfzNoteService;
    public void bfzNote(){
        bfzNoteService.bfzNote();
    }

}
