package com.arusland.bozor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ruslan on 29.09.2014.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(){
        return "home";
    }

    @RequestMapping("/show")
    public String show(){
        return "show";
    }
}
