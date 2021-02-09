package ru.geekbrains.servercloud;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geekbrains.CloudAuthMessage;
import ru.geekbrains.servercloud.authService.AuthService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class CloudAuthHandler extends SimpleChannelInboundHandler<CloudAuthMessage> {
    AuthService authService = new AuthService();


    private static final Logger LOG = LoggerFactory.getLogger(CloudAuthHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudAuthMessage msg) throws Exception {
        authService.startService();
        if(msg.getCmd().equals("AUTH")){
            LOG.debug("Проверяем что пришло msg.getcmd " + msg.getCmd());
            LOG.debug("Проверяем что пришло msg.msg.getLogi " + msg.getLogin());
            LOG.debug("Проверяем что пришло msg.getPassword() " + msg.getPassword());
            CloudAuthMessage userData  =  authService.fillUserData(msg.getLogin(),msg.getPassword());
            if (userData.getResult()){
                ctx.write(userData);
                ctx.flush();
                System.out.println("ctx.write ClientAuthMessage");
            }else{
                System.out.println("Ошибка авторизации");
            }
        }else if(msg.getCmd().equals("REG")){
            LOG.debug("Запрос на регистрацию");
        }
    }
}
