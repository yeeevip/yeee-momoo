package vip.yeee.memoo.demo.netty.simple.longConnection.kit;

import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.HashMap;

/**
 *
 * @author https://www.yeee.vip
 * @since 2021/12/14 18:27
 */
public class LiveChannelCache {

    private Channel channel;

    private ScheduledFuture<?> scheduledFuture;

    public LiveChannelCache(Channel channel, ScheduledFuture<?> scheduledFuture) {
        this.channel = channel;
        this.scheduledFuture = scheduledFuture;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}
