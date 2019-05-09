package pers.allen.rpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import pers.allen.rpc.server.dto.RequestMsg;
import pers.allen.rpc.server.handler.thread.RequestInvokeAndRespResultThread;
import pers.allen.rpc.server.dto.RequestMsgBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lengyul on 2019/3/30 15:55
 */
public class RpcServerThreadExecutor {

    private static final ExecutorService serviceThread = Executors.newCachedThreadPool();

    protected static void processRequest(ChannelHandlerContext ctx, Object msg) {
        serviceThread.execute(new RequestInvokeAndRespResultThread(ctx.channel(),(RequestMsg) msg));
    }

    protected static void processResponse(ChannelHandlerContext ctx, Object msg) {

    }

}
