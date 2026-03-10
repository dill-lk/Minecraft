/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol;

import net.mayaan.network.PacketListener;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;

public abstract class BundlePacket<T extends PacketListener>
implements Packet<T> {
    private final Iterable<Packet<? super T>> packets;

    protected BundlePacket(Iterable<Packet<? super T>> packets) {
        this.packets = packets;
    }

    public final Iterable<Packet<? super T>> subPackets() {
        return this.packets;
    }

    @Override
    public abstract PacketType<? extends BundlePacket<T>> type();
}

