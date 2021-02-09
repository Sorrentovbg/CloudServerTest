package ru.geekbrains;

import java.io.File;

public class CloudCommMessage implements Message{
    private final String command;
    private File[] fileListServer;

    public CloudCommMessage(String command) {
        this.command = command;
    }


    public CloudCommMessage(String command, File[] fileListServer) {
        this.command = command;
        this.fileListServer = fileListServer;
    }

    public String getCommand() {
        return command;
    }

    public File[] getFileListServer() {
        return fileListServer;
    }
}
