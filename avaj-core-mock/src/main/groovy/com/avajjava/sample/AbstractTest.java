package com.avajjava.sample;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AvajCoreMockApplicationStarter.class)
@AutoConfigureMockMvc(secure = false)
//@ContextConfiguration(locations = {
//        "/spring/test-common-config.xml"
//        ,"/spring/test-datasource-config.xml"
//})
abstract class AbstractTest{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

}