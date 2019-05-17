package pers.allen.rpc.client.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.allen.rpc.client.discovery.ServiceDiscovery;
import pers.allen.rpc.client.discovery.ServiceDiscoveryImpl;
import pers.allen.rpc.client.handler.RpcClientHandler;
import pers.allen.rpc.server.codec.fastjson.JSONDecoder;
import pers.allen.rpc.server.codec.fastjson.JSONEncoder;
import pers.allen.rpc.server.dto.RequestMsg;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RpcClient extends RpcChannelGroup {

    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    private final ServiceDiscovery serviceDiscovery = ServiceDiscoveryImpl.getInstance();
    private EventLoopGroup group = null;
    private Bootstrap bootstrap = null;
    private ChannelFuture future = null;
    private Channel channel = null;

    private RpcClient(){ }
    private static final RpcClient rpcClient = new RpcClient();
    public static RpcClient getInstance() {
        return rpcClient;
    }
    private final Object mutex = new Object();

    /**
     * 获取Channel，如果不存在则创建，使用 synchronized 防止同一时刻请求创建多个
     * @param serviceName
     * @return
     */
    protected Channel getChannel(String serviceName) {
        SocketAddress socketAddress = serviceDiscovery.getServiceAddress(serviceName);// 获取服务请求地址
            if(channelMap.containsKey(socketAddress)) {
                return channelMap.get(socketAddress);
            }
        try {
            synchronized (mutex) {
                if(channelMap.containsKey(socketAddress)) {
                    return channelMap.get(socketAddress);
                }
                bootstrap = new Bootstrap();
                group = new NioEventLoopGroup(4);
                future = bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .remoteAddress(socketAddress)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new JSONEncoder());
                                pipeline.addLast(new JSONDecoder(1024, 0, 4));
                                pipeline.addLast(new RpcClientHandler());
                            }
                        })
                        .connect().sync();
                channel = future.channel();
                channelMap.put(socketAddress, channel);
                future = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public void remoteRequest(RequestMsg msg) {
        String serviceName = msg.getClassName();
        Channel channel = rpcClient.getChannel(serviceName);
        channel.writeAndFlush(msg);
    }

}
