package com.maciejj.AaaSJ.infrastructure;

import com.maciejj.AaaSJ.Session.UserData;
import com.maciejj.AaaSJ.Utils.TestUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.io.File;

import static com.maciejj.AaaSJ.Utils.TestUtils.testAudioResource;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")
public class S3AudioResourceLoaderTest {

    @Autowired
    private S3AudioResourceLoaderFactory s3ServiceFactory;

    @Value("${audio-repository-path}")
    private String audioRepositoryPath;

    private S3AudioResourceLoader s3Service;
    private UserData userData = TestUtils.mockUserData();
    private static String resourceFileName = testAudioResource();

    @After
    public void cleanup(){
        boolean wasDeleted = new File(audioRepositoryPath + userData.getUsersNickname() + "/" + resourceFileName).delete();
        assertTrue(wasDeleted);
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
        File file = new File(audioRepositoryPath + userData.getUsersNickname() + "/" + filename);
        return file.exists();
    }

}