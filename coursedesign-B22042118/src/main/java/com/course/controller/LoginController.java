package com.course.controller;

import com.course.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录平台
 */
@Component
public class LoginController {

    @Autowired
    private LoginService loginService;
    public void login(){
        //这里是login
       loginService.testDesign();
    }

}
