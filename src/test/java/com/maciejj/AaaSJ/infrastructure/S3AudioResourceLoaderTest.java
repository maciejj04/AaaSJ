package com.maciejj.AaaSJ.infrastructure;

import com.maciejj.AaaSJ.Session.UserData;
import com.maciejj.AaaSJ.Utils.TestUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.io.File;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")
public class S3AudioResourceLoaderTest {

    @Autowired
    S3AudioResourceLoaderFactory s3ServiceFactory;

    @Value("${audio-repository-path}")
    String audioRepositoryPath;

    S3AudioResourceLoader s3Service;
    UserData userData = TestUtils.mockUserData();
    String resourceFileName = TestUtils.testAudioResource();

    @AfterClass
    public static void cleanup(){
        // TODO: delete all downloaded files
    }

    @PostConstruct
    public void postConstruct(){
        s3Service = s3ServiceFactory.s3AudioResourceLoader(userData);
    }

    @Test
    public void should_download_file(){
        // when
        s3Service.get(resourceFileName);

        // then
        assertTrue(fileExists(resourceFileName));
    }

    private boolean fileExists(String filename){
        File file = new File(audioRepositoryPath + userData.getUserNickname() + "/" + filename);
        return file.exists();
    }

}