/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol;

import net.mayaan.network.PacketListener;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;

public abstract class BundleDelimiterPacket<T extends PacketListener>
implements Packet<T> {
    @Override
    public final void handle(T listener) {
        throw new AssertionError((Object)"This packet should be handled by pipeline");
    }

    @Override
    public abstract PacketType<? extends BundleDelimiterPacket<T>> type();
}

