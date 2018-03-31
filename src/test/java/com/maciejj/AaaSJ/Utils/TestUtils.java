package com.maciejj.AaaSJ.Utils;

import com.maciejj.AaaSJ.Session.UserData;

public class TestUtils {

    public static UserData mockUserData(){
        return new UserData();
    }

    public static String testAudioResource() { return "simpleWhistle.wav"; }
}
