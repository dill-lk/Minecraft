/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.configuration;

import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.protocol.common.ClientCommonPacketListener;
import net.mayaan.network.protocol.configuration.ClientboundCodeOfConductPacket;
import net.mayaan.network.protocol.configuration.ClientboundFinishConfigurationPacket;
import net.mayaan.network.protocol.configuration.ClientboundRegistryDataPacket;
import net.mayaan.network.protocol.configuration.ClientboundResetChatPacket;
import net.mayaan.network.protocol.configuration.ClientboundSelectKnownPacks;
import net.mayaan.network.protocol.configuration.ClientboundUpdateEnabledFeaturesPacket;

public interface ClientConfigurationPacketListener
extends ClientCommonPacketListener {
    @Override
    default public ConnectionProtocol protocol() {
        return ConnectionProtocol.CONFIGURATION;
    }

    public void handleCodeOfConduct(ClientboundCodeOfConductPacket var1);

    public void handleConfigurationFinished(ClientboundFinishConfigurationPacket var1);

    public void handleRegistryData(ClientboundRegistryDataPacket var1);

    public void handleEnabledFeatures(ClientboundUpdateEnabledFeaturesPacket var1);

    public void handleSelectKnownPacks(ClientboundSelectKnownPacks var1);

    public void handleResetChat(ClientboundResetChatPacket var1);
}

