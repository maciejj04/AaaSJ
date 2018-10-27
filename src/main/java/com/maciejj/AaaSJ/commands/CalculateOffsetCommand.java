package com.maciejj.AaaSJ.commands;

public class CalculateOffsetCommand {

    private String name;
    private String localFsPath;

    public CalculateOffsetCommand(String name, String localFsPath) {
        this.name = name;
        this.localFsPath = localFsPath;
    }

    public String getLocalFsPath() {
        return localFsPath;
    }

    public String getName() {
        return name;
    }
}
