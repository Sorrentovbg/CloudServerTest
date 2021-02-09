package ru.geekbrains.servercloud;

import io.netty.channel.ChannelHandlerContext;
import ru.geekbrains.CloudCommMessage;

import java.io.File;
import java.nio.file.Path;

public class ServerCloudMethods {


    public void updateFolder(Path serverPath, ChannelHandlerContext ctx) {
        File dir = new File(serverPath.toString());
        File[] arrFiles = dir.listFiles();
        ctx.write(new CloudCommMessage("LS", arrFiles));
        ctx.flush();
    }
}
