package com.avajjava.sample

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class AvajCoreMockApplicationStarter extends SpringBootServletInitializer {

	static void main(String[] args){
		SpringApplication.run(AvajCoreMockApplicationStarter, args)
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		application.sources(AvajCoreMockApplicationStarter)
	}

}
