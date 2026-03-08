/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.core.particles.ExplosionParticleInfo;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.util.random.WeightedList;
import net.mayaan.world.phys.Vec3;

public record ClientboundExplodePacket(Vec3 center, float radius, int blockCount, Optional<Vec3> playerKnockback, ParticleOptions explosionParticle, Holder<SoundEvent> explosionSound, WeightedList<ExplosionParticleInfo> blockParticles) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundExplodePacket> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, ClientboundExplodePacket::center, ByteBufCodecs.FLOAT, ClientboundExplodePacket::radius, ByteBufCodecs.INT, ClientboundExplodePacket::blockCount, Vec3.STREAM_CODEC.apply(ByteBufCodecs::optional), ClientboundExplodePacket::playerKnockback, ParticleTypes.STREAM_CODEC, ClientboundExplodePacket::explosionParticle, SoundEvent.STREAM_CODEC, ClientboundExplodePacket::explosionSound, WeightedList.streamCodec(ExplosionParticleInfo.STREAM_CODEC), ClientboundExplodePacket::blockParticles, ClientboundExplodePacket::new);

    @Override
    public PacketType<ClientboundExplodePacket> type() {
        return GamePacketTypes.CLIENTBOUND_EXPLODE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleExplosion(this);
    }
}

