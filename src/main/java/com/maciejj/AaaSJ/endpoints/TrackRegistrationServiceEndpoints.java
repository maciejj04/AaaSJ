package com.maciejj.AaaSJ.endpoints;

import com.maciejj.AaaSJ.commands.RegisterTrackRQ;
import com.maciejj.AaaSJ.services.TrackRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class TrackRegistrationServiceEndpoints {

    TrackRegistrationService trackRegistrationService;

    public TrackRegistrationServiceEndpoints(TrackRegistrationService trackRegistrationService) {
        this.trackRegistrationService = trackRegistrationService;
    }

    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public ResponseEntity trackRegistration(RegisterTrackRQ registerTrackRQ){
        try {
            trackRegistrationService.registerTrack(registerTrackRQ);
        } catch (InterruptedException e) {
            // TODO: exception to HTTP mapper.
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());//FIXME: Should not return message - return X-Request-Id.
        }
        return ok().build();
    }


}
