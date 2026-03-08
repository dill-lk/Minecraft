/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.protocol.BundleDelimiterPacket;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public class ClientboundBundleDelimiterPacket
extends BundleDelimiterPacket<ClientGamePacketListener> {
    @Override
    public PacketType<ClientboundBundleDelimiterPacket> type() {
        return GamePacketTypes.CLIENTBOUND_BUNDLE_DELIMITER;
    }
}

