package com.avajjava.sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebConfig
    //TODO: 현재 WebMvcConfigurerAdapter를 상속하면 Thymeleaf가 정적자원들이 안 먹음
    extends WebMvcConfigurerAdapter
    //TODO: 현재 WebMvcConfigurationSupport를 상속하면 Thymeleaf에 정적자원들이 잘 먹음
//    extends WebMvcConfigurationSupport
{

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler(
                //허용하는 패턴들
                "/webjars/**",
                "/img/**",
                "/css/**",
                "/font/**",
                "/lib/**",
                "/js/**",
//                "/templates/**"
                "/docs/**"
            )
            .addResourceLocations(
                "classpath:/META-INF/resources/webjars/",
                "classpath:/static/img/",
                "classpath:/static/css/",
                "classpath:/static/font/",
                "classpath:/static/js/",
                "classpath:/static/lib/",
                "classpath:/templates/"
//                "classpath:/docs/"
            );
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8).ignoreAcceptHeader(true);
    }

}