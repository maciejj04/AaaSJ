package com.maciejj.AaaSJ.Session;

import lombok.Getter;

public class UserData {

    // TODO: These valuesShould be populated from some kind of session-service returned token (JWT), hardcoded for now.
    @Getter String usersNickname = "WarCriminal";
    @Getter String bucketName = "maciejj-audio";
    @Getter String awsRegion = "eu-west-2"; // TMP

}