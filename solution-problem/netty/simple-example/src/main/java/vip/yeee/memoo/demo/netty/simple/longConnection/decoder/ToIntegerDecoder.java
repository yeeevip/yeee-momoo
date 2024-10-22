package vip.yeee.memoo.demo.netty.simple.longConnection.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 *
 * @author https://www.yeee.vip
 * @since 2021/12/14 18:05
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {

    // decode 会根据接收的数据，被调用多次, [直到确定没有新的]元素被添加到list
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 4) {
            out.add(in.readInt());
        }
    }
}
