package com.maciejj.AaaSJ;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.maciejj.AaaSJ.infrastructure.TimerProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RestController
@Configuration
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
	@Profile("prod")	//TODO: aws_access_key etc. should be retrived from user-service.(Revisit using other AWS media service for that!)
	AmazonS3ClientBuilder s3Client(@Value("${aaasj_s3_aws_access_key_id}") String s3AccessKey,
								   @Value("${aaasj_s3_aws_secret_access_key}") String s3SecretKey) {
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials(s3AccessKey, s3SecretKey))
				);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/status")
	public String status(){
		return "Hello, this is AaaSJ running on "; //+ System.getProperty()
	}

	public static void main(String[] args) {
		SpringApplication.run(AaaSjApplication.class, args);
	}
}
