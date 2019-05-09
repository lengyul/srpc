package pers.allen.rpc.client.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.allen.rpc.client.channel.RpcChannelGroup;
import pers.allen.rpc.client.channel.RpcClient;
import pers.allen.rpc.server.dto.ResponseMsg;
import pers.allen.rpc.server.utils.RequestSyncQueueUtils;


public class RpcClientHandler extends SimpleChannelInboundHandler<Object> {

    private final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);
    private RpcChannelGroup channelGroup = RpcClient.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("已连接到服务提供者服务器：" + ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
          ResponseMsg responseMsg = JSON.parseObject(msg.toString(),ResponseMsg.class);
          RequestSyncQueueUtils.notifyRequest(responseMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("服务调用或接收发生异常：" + cause);
        channelGroup.removeChannel(ctx.channel());
        ctx.channel().close();
    }
}
