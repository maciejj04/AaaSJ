package com.maciejj.AaaSJ.infrastructure;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.maciejj.AaaSJ.Session.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
public class S3AudioResourceLoaderFactory {

    @Autowired
    private AmazonS3ClientBuilder s3ClientBuilder;

    @Value("${audio-repository-path}")
    private String audioRepositoryPath;

    public S3AudioResourceLoader s3AudioResourceLoader(UserData userData){
        return new S3AudioResourceLoader(s3ClientBuilder, userData, audioRepositoryPath);
    }


}
