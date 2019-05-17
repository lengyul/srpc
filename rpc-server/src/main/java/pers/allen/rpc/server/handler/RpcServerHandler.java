package pers.allen.rpc.server.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.allen.rpc.server.dto.BuilderMsg;
import pers.allen.rpc.server.dto.RequestMsg;
import pers.allen.rpc.server.dto.RequestMsgBuilder;
import pers.allen.rpc.server.dto.ResponseMsg;


public class RpcServerHandler extends SimpleChannelInboundHandler<Object> {

    private final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("["+ctx.channel().remoteAddress()+"]："+ "客户端连接成功");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("接收到客户端请求数据：" + msg);
    //    RpcServerThreadExecutor.processRequest(ctx, msg);
        ResponseMsg responseMsg = null;
        RequestMsg requestMsg = JSON.parseObject(msg.toString(), RequestMsg.class);
        Object result = RequestLocalCall.handler(requestMsg);
        responseMsg = BuilderMsg.buildResponseMsg(requestMsg.getType(),requestMsg.getRequestId(), result);
        ctx.channel().writeAndFlush(responseMsg);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
