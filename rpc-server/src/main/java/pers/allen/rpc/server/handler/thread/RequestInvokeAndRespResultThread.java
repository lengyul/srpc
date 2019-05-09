package pers.allen.rpc.server.handler.thread;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.allen.rpc.server.dto.RequestMsg;
import pers.allen.rpc.server.dto.RequestMsgBuilder;
import pers.allen.rpc.server.handler.RequestLocalCall;
import pers.allen.rpc.server.dto.BuilderMsg;
import pers.allen.rpc.server.dto.ResponseMsg;

/**
 * Created by lengyul on 2019/3/30 15:48
 */
public class RequestInvokeAndRespResultThread implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(RequestInvokeAndRespResultThread.class);
    Channel channel;
    RequestMsg msg;

    public RequestInvokeAndRespResultThread(Channel channel, RequestMsg msg) {
        this.channel = channel;
        this.msg = msg;
    }
    @Override
    public void run() {
        int type = msg.getType();
        if (1 == type) {
            Object result = RequestLocalCall.handler(msg);
            ResponseMsg responseMsg = BuilderMsg.buildResponseMsg(msg.getRequestId(), result);
            channel.writeAndFlush(responseMsg);
        }
    }
}
