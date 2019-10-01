package com.mufeng.skydog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping(value = "/config")
    public String config(){
        return "index.html";
    }
}
