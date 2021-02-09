package ru.geekbrains.servercloud;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geekbrains.CloudUnitMessage;

import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CloudUnitHandler extends SimpleChannelInboundHandler<CloudUnitMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(CloudUnitHandler.class);

    private static final ConcurrentLinkedDeque<ChannelHandlerContext> clients = new ConcurrentLinkedDeque<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx);
        LOG.debug("Client accept");

    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudUnitMessage msg) throws Exception {
        LOG.debug("Handler on server CloudUnitHandler");
        String cl = msg.getClass().toString();
        LOG.debug("имя класса" + cl);
        String fileName = msg.getFileName();
        byte[] receivedByteFile = new byte[(int) msg.getFileSize()];
        LOG.debug("receivedByteFile " + receivedByteFile.length);
        RandomAccessFile raf = new RandomAccessFile(CloudCommHandler.getServerPath().toString() + "/" + msg.getFileName(), "rw");
        if(msg.getComm().equals("START")){
            raf.seek(0);
            raf.write(msg.getFileByte());
            LOG.debug("Count " + msg.getFileSize());
            raf.close();
        }else if(msg.getComm().equals("CONTINUE")){
            raf.seek(((long) msg.getFileByte().length * msg.getFileSize())+1);
            raf.write(msg.getFileByte());
            LOG.debug("Count " + msg.getFileSize());
            raf.close();
        }else if(msg.getComm().equals("END")){
            LOG.debug("Комманда End = " + msg.getComm());
            raf.seek(((long) msg.getFileByte().length * msg.getFileSize())+1);
            byte[] last = msg.getFileByte();
            for(byte b: last){
                if(b != 0){
                    raf.write(b);
                    LOG.debug("b = " + b);
                }else {
                    break;
                }
            }
            LOG.debug("Count " + msg.getFileSize());
            raf.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clients.remove(ctx);
        LOG.debug("Client disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
