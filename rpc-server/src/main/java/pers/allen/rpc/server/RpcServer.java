package pers.allen.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.allen.rpc.server.codec.fastjson.JSONDecoder;
import pers.allen.rpc.server.codec.fastjson.JSONEncoder;
import pers.allen.rpc.server.handler.RpcServerHandler;

public class RpcServer {

    private final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup  = new NioEventLoopGroup();

    public void start(final String serverAddress) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        String[] arr = serverAddress.split(":");
        try {
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(arr[0],Integer.parseInt(arr[1]))
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new JSONEncoder());
                            pipeline.addLast(new JSONDecoder(1024,0,4));
                            pipeline.addLast(new RpcServerHandler());
                        }
                    });
            ChannelFuture cf = bootstrap.bind(); // 绑定端口，初始化监听
            logger.info("RPC服务启动成功：" + serverAddress + ", 已连接到注册中心");
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
