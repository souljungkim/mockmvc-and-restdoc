package com.avajjava.sample.home

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/*")
class HomeViewController {

    @RequestMapping("")
    String main(){
        return "redirect:/docs/"
    }

}
