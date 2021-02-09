package ru.geekbrains;

public class CloudUnitMessage implements Message{

    private final String fileName;
    private final String comm;
    private final byte[] fileByte;
    private final int fileSize;


    public CloudUnitMessage(String fileName, String comm, byte[] fileByte, int fileSize) {
        this.fileName = fileName;
        this.comm = comm;
        this.fileByte = fileByte;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileByte() {
        return fileByte;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getComm() {
        return comm;
    }
}
