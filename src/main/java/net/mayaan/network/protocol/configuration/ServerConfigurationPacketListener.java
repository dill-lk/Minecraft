/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.configuration;

import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.protocol.common.ServerCommonPacketListener;
import net.mayaan.network.protocol.configuration.ServerboundAcceptCodeOfConductPacket;
import net.mayaan.network.protocol.configuration.ServerboundFinishConfigurationPacket;
import net.mayaan.network.protocol.configuration.ServerboundSelectKnownPacks;

public interface ServerConfigurationPacketListener
extends ServerCommonPacketListener {
    @Override
    default public ConnectionProtocol protocol() {
        return ConnectionProtocol.CONFIGURATION;
    }

    public void handleConfigurationFinished(ServerboundFinishConfigurationPacket var1);

    public void handleSelectKnownPacks(ServerboundSelectKnownPacks var1);

    public void handleAcceptCodeOfConduct(ServerboundAcceptCodeOfConductPacket var1);
}

