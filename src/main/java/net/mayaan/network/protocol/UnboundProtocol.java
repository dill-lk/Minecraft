/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol;

import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import net.mayaan.network.PacketListener;
import net.mayaan.network.ProtocolInfo;

public interface UnboundProtocol<T extends PacketListener, B extends ByteBuf, C>
extends ProtocolInfo.DetailsProvider {
    public ProtocolInfo<T> bind(Function<ByteBuf, B> var1, C var2);
}

