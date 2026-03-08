/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class ClientboundSelectAdvancementsTabPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSelectAdvancementsTabPacket> STREAM_CODEC = Packet.codec(ClientboundSelectAdvancementsTabPacket::write, ClientboundSelectAdvancementsTabPacket::new);
    private final @Nullable Identifier tab;

    public ClientboundSelectAdvancementsTabPacket(@Nullable Identifier tab) {
        this.tab = tab;
    }

    private ClientboundSelectAdvancementsTabPacket(FriendlyByteBuf input) {
        this.tab = input.readNullable(FriendlyByteBuf::readIdentifier);
    }

    private void write(FriendlyByteBuf output) {
        output.writeNullable(this.tab, FriendlyByteBuf::writeIdentifier);
    }

    @Override
    public PacketType<ClientboundSelectAdvancementsTabPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SELECT_ADVANCEMENTS_TAB;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSelectAdvancementsTab(this);
    }

    public @Nullable Identifier getTab() {
        return this.tab;
    }
}

