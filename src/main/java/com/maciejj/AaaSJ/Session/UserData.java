package com.maciejj.AaaSJ.Session;

import lombok.Getter;

public class UserData {

    // TODO: These valuesShould be populated from session-service, hardcoded for now.
    @Getter String userNickname = "WarCriminal";
    @Getter String bucketName = "maciejj-audio";
    @Getter String awsRegion = "eu-west-2"; // TMP

}