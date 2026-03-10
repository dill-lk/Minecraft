/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.protocol.BundlePacket;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public class ClientboundBundlePacket
extends BundlePacket<ClientGamePacketListener> {
    public ClientboundBundlePacket(Iterable<Packet<? super ClientGamePacketListener>> packets) {
        super(packets);
    }

    @Override
    public PacketType<ClientboundBundlePacket> type() {
        return GamePacketTypes.CLIENTBOUND_BUNDLE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleBundlePacket(this);
    }
}

