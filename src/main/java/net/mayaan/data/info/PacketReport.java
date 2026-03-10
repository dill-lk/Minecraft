/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package net.mayaan.data.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.mayaan.data.CachedOutput;
import net.mayaan.data.DataProvider;
import net.mayaan.data.PackOutput;
import net.mayaan.network.ProtocolInfo;
import net.mayaan.network.protocol.configuration.ConfigurationProtocols;
import net.mayaan.network.protocol.game.GameProtocols;
import net.mayaan.network.protocol.handshake.HandshakeProtocols;
import net.mayaan.network.protocol.login.LoginProtocols;
import net.mayaan.network.protocol.status.StatusProtocols;

public class PacketReport
implements DataProvider {
    private final PackOutput output;

    public PacketReport(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("packets.json");
        return DataProvider.saveStable(cache, this.serializePackets(), path);
    }

    private JsonElement serializePackets() {
        JsonObject protocols = new JsonObject();
        Stream.of(HandshakeProtocols.SERVERBOUND_TEMPLATE, StatusProtocols.CLIENTBOUND_TEMPLATE, StatusProtocols.SERVERBOUND_TEMPLATE, LoginProtocols.CLIENTBOUND_TEMPLATE, LoginProtocols.SERVERBOUND_TEMPLATE, ConfigurationProtocols.CLIENTBOUND_TEMPLATE, ConfigurationProtocols.SERVERBOUND_TEMPLATE, GameProtocols.CLIENTBOUND_TEMPLATE, GameProtocols.SERVERBOUND_TEMPLATE).map(ProtocolInfo.DetailsProvider::details).collect(Collectors.groupingBy(ProtocolInfo.Details::id)).forEach((protocolId, flows) -> {
            JsonObject protocolData = new JsonObject();
            protocols.add(protocolId.id(), (JsonElement)protocolData);
            flows.forEach(flow -> {
                JsonObject protocolFlowData = new JsonObject();
                protocolData.add(flow.flow().id(), (JsonElement)protocolFlowData);
                flow.listPackets((type, networkId) -> {
                    JsonObject packetInfo = new JsonObject();
                    packetInfo.addProperty("protocol_id", (Number)networkId);
                    protocolFlowData.add(type.id().toString(), (JsonElement)packetInfo);
                });
            });
        });
        return protocols;
    }

    @Override
    public String getName() {
        return "Packet Report";
    }
}

