/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelInboundHandlerAdapter
 */
package net.mayaan.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.mayaan.network.BandwidthDebugMonitor;
import net.mayaan.network.HiddenByteBuf;

public class MonitoredLocalFrameDecoder
extends ChannelInboundHandlerAdapter {
    private final BandwidthDebugMonitor monitor;

    public MonitoredLocalFrameDecoder(BandwidthDebugMonitor monitor) {
        this.monitor = monitor;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if ((msg = HiddenByteBuf.unpack(msg)) instanceof ByteBuf) {
            ByteBuf in = (ByteBuf)msg;
            this.monitor.onReceive(in.readableBytes());
        }
        ctx.fireChannelRead(msg);
    }
}

