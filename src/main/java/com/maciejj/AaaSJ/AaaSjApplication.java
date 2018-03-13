package com.maciejj.AaaSJ;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.maciejj.AaaSJ.infrastructure.TimerProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
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

	@Bean
	@Profile("prod")
	AmazonS3ClientBuilder s3Client(@Value("${aaasj_s3_aws_access_key_id}") String accessKey, @Value("${}") String secretKey){
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));
	}

	public static void main(String[] args) {
		SpringApplication.run(AaaSjApplication.class, args);
	}
}
