/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import net.mayaan.core.Holder;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public record ClientboundRemoveMobEffectPacket(int entityId, Holder<MobEffect> effect) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRemoveMobEffectPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundRemoveMobEffectPacket::entityId, MobEffect.STREAM_CODEC, ClientboundRemoveMobEffectPacket::effect, ClientboundRemoveMobEffectPacket::new);

    @Override
    public PacketType<ClientboundRemoveMobEffectPacket> type() {
        return GamePacketTypes.CLIENTBOUND_REMOVE_MOB_EFFECT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleRemoveMobEffect(this);
    }

    public @Nullable Entity getEntity(Level level) {
        return level.getEntity(this.entityId);
    }
}

