package com.maciejj.AaaSJ.domain;

import com.maciejj.AaaSJ.AaaSjApplication;
import com.maciejj.AaaSJ.commands.CentroidRQ;
import com.maciejj.AaaSJ.services.CentroidService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AaaSjApplication.class, webEnvironment = RANDOM_PORT, properties = {"aaasj_s3_aws_access_key_id=123","aaasj_s3_aws_secret_access_key=123"})
public class DomainExceptionMapperTest {

    @LocalServerPort
    int port;

    @MockBean
    private CentroidService service;

    private String url = "http://127.0.0.1:";

    @PostConstruct
    public void postConstuct() {
        url += port;
    }

    @Test
    public void should_map_domain_exceptions() throws InterruptedException, UnsupportedAudioFileException, ExecutionException, IOException {
        // given
        when(service.centoids(any())).thenThrow(UnsupportedAudioFileException.class);
        TestRestTemplate cli = new TestRestTemplate();
        String path = "/v1/spectralCentroids";

        // when
        ResponseEntity<String> response = cli.postForEntity(url + path, new CentroidRQ(), String.class, (Object) null);

        // then
        assertTrue(HttpStatus.BAD_REQUEST.value() == response.getStatusCode().value());
    }

}