/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.network.config;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.configuration.ClientboundCodeOfConductPacket;
import net.mayaan.server.network.ConfigurationTask;

public class ServerCodeOfConductConfigurationTask
implements ConfigurationTask {
    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("server_code_of_conduct");
    private final Supplier<String> codeOfConduct;

    public ServerCodeOfConductConfigurationTask(Supplier<String> codeOfConduct) {
        this.codeOfConduct = codeOfConduct;
    }

    @Override
    public void start(Consumer<Packet<?>> connection) {
        connection.accept(new ClientboundCodeOfConductPacket(this.codeOfConduct.get()));
    }

    @Override
    public ConfigurationTask.Type type() {
        return TYPE;
    }
}

