package com.maciejj.AaaSJ.infrastructure;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.maciejj.AaaSJ.Session.UserData;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.maciejj.AaaSJ.domain.DomainUtils.generateByteBufer;

public class S3AudioResourceLoader implements AudioResourceLoader {

    private Logger logger = Logger.getLogger(S3AudioResourceLoader.class);
    private AmazonS3 s3Client;
    private UserData userData;
    private String audioRepositoryPath;

    public S3AudioResourceLoader(AmazonS3ClientBuilder s3ClientBuilder, UserData userData, String audioRepositoryPath) {
        this.audioRepositoryPath = audioRepositoryPath;
        this.s3Client = s3ClientBuilder.withRegion(userData.getAwsRegion()).build();
        this.userData = userData;
    }

    @Override
    public void get(String resourceName) {

        String fullResourcePath = audioRepositoryPath + userData.getUserNickname() + "/simpleWhistle.wav";
        logger.info("Downloading \""+resourceName + "\" file from s3.");
        File file = new File(fullResourcePath);

        try{
            s3Client.getObject(new GetObjectRequest(userData.getBucketName(), resourceName), file);
            logger.info(resourceName + "properly saved in: " + fullResourcePath);
        } catch (AmazonServiceException e) {
            logAmazonClientException(e);
        } catch (AmazonClientException e) {
            logAmazonClientException(e);
        }

    }

    private String save3SObject(S3Object s3Object, String saveName){
        byte[] buffer = generateByteBufer((int) s3Object.getObjectMetadata().getContentLength());// IS it always good to cast to int?
        try {
            s3Object.getObjectContent().read(buffer);
            //assert == -1
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fullFilePath = audioRepositoryPath + saveName;

        try (OutputStream outputStream = new FileOutputStream(fullFilePath)){
            outputStream.write(buffer);
            s3Object.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullFilePath;
    }

    private void logAmazonClientException(AmazonServiceException e){
        System.out.println("Caught an AmazonServiceException, which" +
                " means your request made it to Amazon S3, but was rejected with an error response for some reason.");
        System.out.println("Error Message:    " + e.getMessage());
        System.out.println("HTTP Status Code: " + e.getStatusCode());
        System.out.println("AWS Error Code:   " + e.getErrorCode());
        System.out.println("Error Type:       " + e.getErrorType());
        System.out.println("Request ID:       " + e.getRequestId());
    }

    private void logAmazonClientException(AmazonClientException e){
        System.out.println("Caught an AmazonClientException, which means"+
                " the client encountered " +
                "an internal error while trying to " +
                "communicate with S3, " +
                "such as not being able to access the network.");
        System.out.println("Error Message: " + e.getMessage());
    }
}
