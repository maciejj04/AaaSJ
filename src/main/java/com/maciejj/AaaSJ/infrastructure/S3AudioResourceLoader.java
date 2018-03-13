package com.maciejj.AaaSJ.infrastructure;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Builder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.maciejj.AaaSJ.Session.UserData;
import com.maciejj.AaaSJ.domain.DomainUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.maciejj.AaaSJ.domain.DomainUtils.generateByteBufer;

@Profile("prod")
@Component
public class S3AudioResourceLoader implements AudioResourceLoader {

    private AmazonS3 s3Client;
    private UserData userData;
    @Value("${audio-repository-path}")
    private String audioRepositoryPath;

    public S3AudioResourceLoader(AmazonS3ClientBuilder s3ClientBuilder, UserData session) {
        this.s3Client = s3ClientBuilder.withRegion(userData.getAwsRegion()).build();
        this.userData = session;
    }

    @Override
    public void get(String resourceName) {
        S3Object s3Object = null;//Shieeet, going to sleep.
        try{
            s3Object = s3Client.getObject(userData.getBucketName(), resourceName);
        } catch (AmazonServiceException e) {
            System.out.println("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + e.getMessage());
            System.out.println("HTTP Status Code: " + e.getStatusCode());
            System.out.println("AWS Error Code:   " + e.getErrorCode());
            System.out.println("Error Type:       " + e.getErrorType());
            System.out.println("Request ID:       " + e.getRequestId());
        } catch (AmazonClientException e) {
            System.out.println("Caught an AmazonClientException, which means"+
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + e.getMessage());
        }

        save3SObject(s3Object, resourceName);
    }

    private String save3SObject(S3Object s3Object, String saveName){
        byte[] buffer = generateByteBufer((int) s3Object.getObjectMetadata().getContentLength());// IS it always good to cast to int?
        try {
            assert s3Object.getObjectContent().read(buffer) == -1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fullFilePath = audioRepositoryPath + "/" + saveName;

        try (OutputStream outputStream = new FileOutputStream(fullFilePath)){
            outputStream.write(buffer);
            s3Object.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullFilePath;
    }
}
