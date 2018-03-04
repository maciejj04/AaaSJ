package com.maciejj.AaaSJ;

import com.maciejj.AaaSJ.infrastructure.TimerProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AaaSjApplication implements WebMvcConfigurer {

	// Allow CORS for all. For AWS deployment.
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TimerProxy()).addPathPatterns("/v*/*");
	}

	public static void main(String[] args) {
		SpringApplication.run(AaaSjApplication.class, args);
	}
}
