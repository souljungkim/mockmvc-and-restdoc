package com.avajjava.sample.docs.controller


import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/docs/*")
class DocsViewController {


    @RequestMapping("")
    String main(){
        return "docs/index"
    }


    @RequestMapping("mock-and-rest-docs-test") //For test
    String mockAndRestDocsTest(){
        return "docs/mock-and-rest-docs-test"
    }

    @RequestMapping("other-module-test") //For test
    String data(){
        return "docs/other-module-test"
    }

}
