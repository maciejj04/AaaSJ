package com.maciejj.AaaSJ.endpoints;

import com.maciejj.AaaSJ.commands.CentroidRQ;
import com.maciejj.AaaSJ.domain.Centroid;
import com.maciejj.AaaSJ.services.CentroidService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class CentroidServiceEndpoints {

    CentroidService centroidService;

    public CentroidServiceEndpoints(CentroidService centroidService) {
        this.centroidService = centroidService;
    }

    @RequestMapping(path = "/v1/spectralCentroids", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Centroid>> centoids(@RequestBody CentroidRQ request) throws InterruptedException, UnsupportedAudioFileException, ExecutionException, IOException {
        return ok(centroidService.centoids(request));
    }
}
