/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.network.config;

import java.util.Optional;
import java.util.function.Consumer;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.common.ClientboundResourcePackPushPacket;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.network.ConfigurationTask;

public class ServerResourcePackConfigurationTask
implements ConfigurationTask {
    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("server_resource_pack");
    private final MayaanServer.ServerResourcePackInfo info;

    public ServerResourcePackConfigurationTask(MayaanServer.ServerResourcePackInfo info) {
        this.info = info;
    }

    @Override
    public void start(Consumer<Packet<?>> connection) {
        connection.accept(new ClientboundResourcePackPushPacket(this.info.id(), this.info.url(), this.info.hash(), this.info.isRequired(), Optional.ofNullable(this.info.prompt())));
    }

    @Override
    public ConfigurationTask.Type type() {
        return TYPE;
    }
}

