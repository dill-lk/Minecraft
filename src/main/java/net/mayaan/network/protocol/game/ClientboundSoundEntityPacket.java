/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.core.Holder;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.entity.Entity;

public class ClientboundSoundEntityPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSoundEntityPacket> STREAM_CODEC = Packet.codec(ClientboundSoundEntityPacket::write, ClientboundSoundEntityPacket::new);
    private final Holder<SoundEvent> sound;
    private final SoundSource source;
    private final int id;
    private final float volume;
    private final float pitch;
    private final long seed;

    public ClientboundSoundEntityPacket(Holder<SoundEvent> sound, SoundSource source, Entity sourceEntity, float volume, float pitch, long seed) {
        this.sound = sound;
        this.source = source;
        this.id = sourceEntity.getId();
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }

    private ClientboundSoundEntityPacket(RegistryFriendlyByteBuf input) {
        this.sound = (Holder)SoundEvent.STREAM_CODEC.decode(input);
        this.source = input.readEnum(SoundSource.class);
        this.id = input.readVarInt();
        this.volume = input.readFloat();
        this.pitch = input.readFloat();
        this.seed = input.readLong();
    }

    private void write(RegistryFriendlyByteBuf output) {
        SoundEvent.STREAM_CODEC.encode(output, this.sound);
        output.writeEnum(this.source);
        output.writeVarInt(this.id);
        output.writeFloat(this.volume);
        output.writeFloat(this.pitch);
        output.writeLong(this.seed);
    }

    @Override
    public PacketType<ClientboundSoundEntityPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SOUND_ENTITY;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSoundEntityEvent(this);
    }

    public Holder<SoundEvent> getSound() {
        return this.sound;
    }

    public SoundSource getSource() {
        return this.source;
    }

    public int getId() {
        return this.id;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public long getSeed() {
        return this.seed;
    }
}

