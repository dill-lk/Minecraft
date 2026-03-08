/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.damagesource.DamageType;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.phys.Vec3;

public record ClientboundDamageEventPacket(int entityId, Holder<DamageType> sourceType, int sourceCauseId, int sourceDirectId, Optional<Vec3> sourcePosition) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDamageEventPacket> STREAM_CODEC = Packet.codec(ClientboundDamageEventPacket::write, ClientboundDamageEventPacket::new);

    public ClientboundDamageEventPacket(Entity entity, DamageSource source) {
        this(entity.getId(), source.typeHolder(), source.getEntity() != null ? source.getEntity().getId() : -1, source.getDirectEntity() != null ? source.getDirectEntity().getId() : -1, Optional.ofNullable(source.sourcePositionRaw()));
    }

    private ClientboundDamageEventPacket(RegistryFriendlyByteBuf input) {
        this(input.readVarInt(), (Holder)DamageType.STREAM_CODEC.decode(input), ClientboundDamageEventPacket.readOptionalEntityId(input), ClientboundDamageEventPacket.readOptionalEntityId(input), input.readOptional(i -> new Vec3(i.readDouble(), i.readDouble(), i.readDouble())));
    }

    private static void writeOptionalEntityId(FriendlyByteBuf output, int id) {
        output.writeVarInt(id + 1);
    }

    private static int readOptionalEntityId(FriendlyByteBuf input) {
        return input.readVarInt() - 1;
    }

    private void write(RegistryFriendlyByteBuf output) {
        output.writeVarInt(this.entityId);
        DamageType.STREAM_CODEC.encode(output, this.sourceType);
        ClientboundDamageEventPacket.writeOptionalEntityId(output, this.sourceCauseId);
        ClientboundDamageEventPacket.writeOptionalEntityId(output, this.sourceDirectId);
        output.writeOptional(this.sourcePosition, (o, pos) -> {
            o.writeDouble(pos.x());
            o.writeDouble(pos.y());
            o.writeDouble(pos.z());
        });
    }

    @Override
    public PacketType<ClientboundDamageEventPacket> type() {
        return GamePacketTypes.CLIENTBOUND_DAMAGE_EVENT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleDamageEvent(this);
    }

    public DamageSource getSource(Level level) {
        if (this.sourcePosition.isPresent()) {
            return new DamageSource(this.sourceType, this.sourcePosition.get());
        }
        Entity cause = level.getEntity(this.sourceCauseId);
        Entity direct = level.getEntity(this.sourceDirectId);
        return new DamageSource(this.sourceType, direct, cause);
    }
}

