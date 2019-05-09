package pers.allen.rpc.server.codec.fastjson;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class JSONEncoder extends MessageToMessageEncoder {


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer();
        byte[] jsonBytes = JSON.toJSONBytes(msg);
        byteBuf.writeInt(jsonBytes.length);
        byteBuf.writeBytes(jsonBytes);
        out.add(byteBuf);
    }
}
