package vip.yeee.memoo.demo.netty.simple.longConnection.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author https://www.yeee.vip
 * @since 2021/12/14 18:03
 */
@Slf4j
public class StringToByteEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        log.debug("StringToByteEncoder encode msg = " + msg);
        out.writeCharSequence(msg, StandardCharsets.UTF_8);
    }
}
