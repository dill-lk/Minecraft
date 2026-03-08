/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.network.config;

import java.util.function.Consumer;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.configuration.ClientboundFinishConfigurationPacket;
import net.mayaan.server.network.ConfigurationTask;

public class JoinWorldTask
implements ConfigurationTask {
    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("join_world");

    @Override
    public void start(Consumer<Packet<?>> connection) {
        connection.accept(ClientboundFinishConfigurationPacket.INSTANCE);
    }

    @Override
    public ConfigurationTask.Type type() {
        return TYPE;
    }
}

