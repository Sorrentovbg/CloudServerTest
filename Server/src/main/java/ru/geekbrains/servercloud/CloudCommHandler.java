package ru.geekbrains.servercloud;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geekbrains.CloudCommMessage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CloudCommHandler extends SimpleChannelInboundHandler<CloudCommMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(CloudCommHandler.class);
    public ServerCloudMethods scm = new ServerCloudMethods();

    private static Path serverPath = Paths.get("CloudServer/src/main/resources/userFolder");

    public static Path getServerPath() {
        return serverPath;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudCommMessage msg) throws Exception {
        LOG.debug("Я в CloudCommMessage");
        LOG.debug("получаю cmd " + msg.getCommand());
        String cmd = msg.getCommand();

//  Обработка команд которые приходят от клиента
//  Обработка команда LS обновление списка файлов и папок.

        if (cmd.equals("LS")){
            LOG.debug("Попал в If ClodCommMessage");
            LOG.debug("You directory: " + serverPath.toString()+ ">");
            scm.updateFolder(serverPath, ctx);

        }
//  Обработка команда CD переход в указанную папку а так же проверка что это папка.

        else if(cmd.startsWith("CD")){
            String[] getPath = cmd.split("_",2);
            LOG.debug("Ищю причину не перехода в созданную папку Folder2 getPath[0] " + getPath[0]);
            LOG.debug("Ищю причину не перехода в созданную папку Folder2 getPath[1] " + getPath[1]);
            Path goTo = Paths.get(serverPath + "/" + getPath[1]);
            LOG.debug("Path goTo " + goTo);
            LOG.debug("Path goTo.toString " + goTo.toString());
            File obj = new File(goTo.toString());
            if(obj.isDirectory()){
                File obj1 = new File("CloudServer/src/main/resources/userFolder/Folder2");
                LOG.debug("Проверяю folder2 " + obj1.isDirectory());
                serverPath = goTo;
                LOG.debug("You directory: " + serverPath.toString()+ ">");
                scm.updateFolder(serverPath, ctx);
            }else {
                LOG.debug("Проверяю folder2 попал в Else");
                LOG.debug("Вывод сообщения что такой папки нет");
            }
        }
//  Обработка команда .. переход в папку уровнем выше.

        else if(cmd.equals("..")){
            String delimeter = "\\\\";
            String[] comeBack = serverPath.toString().split(delimeter);
            StringBuilder s = new StringBuilder();
            if(!comeBack[comeBack.length-1].equals("userFolder")){
                for (int i = 0; i < comeBack.length-1; i++){
                        LOG.debug("Вывод ошибки о том что корневая папка");
                        s.append(comeBack[i]+"/");
                }
                serverPath = Paths.get(s.toString());
            }else {
                LOG.debug("Already in root folder");
            }
            LOG.debug("ServerPath после ..  = " + serverPath.toString());
            scm.updateFolder(serverPath, ctx);
        }
//  Обработка команда MKDIR создание папки на сервере.

        else if(cmd.startsWith("MKDIR")){
            String[] getFolderName = cmd.split("_",2);
            String directoryName = getFolderName[1];

            File folder = new File(serverPath + "/" + directoryName);
            if(folder.exists()){
                LOG.debug("infoMessage = Directory already exist");
                ctx.write(new CloudCommMessage("ERROR"));
            }else {
                Files.createDirectory(Paths.get(serverPath + "/" + directoryName));
                LOG.debug("infoMessage = Directory created");

            }
            scm.updateFolder(serverPath, ctx);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
