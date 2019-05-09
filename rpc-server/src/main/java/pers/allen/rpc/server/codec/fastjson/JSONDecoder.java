package pers.allen.rpc.server.codec.fastjson;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class JSONDecoder extends LengthFieldBasedFrameDecoder {

    public JSONDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null){
            return null;
        }
        frame.readInt();
        byte[] msgBytes = new byte[frame.readableBytes()];
        frame.readBytes(msgBytes);
        return JSON.parse(msgBytes);
    }
}
