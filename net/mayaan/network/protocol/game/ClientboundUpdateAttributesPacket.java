/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.mayaan.core.Holder;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.ai.attributes.Attribute;
import net.mayaan.world.entity.ai.attributes.AttributeInstance;
import net.mayaan.world.entity.ai.attributes.AttributeModifier;

public class ClientboundUpdateAttributesPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateAttributesPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundUpdateAttributesPacket::getEntityId, AttributeSnapshot.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundUpdateAttributesPacket::getValues, ClientboundUpdateAttributesPacket::new);
    private final int entityId;
    private final List<AttributeSnapshot> attributes;

    public ClientboundUpdateAttributesPacket(int entityId, Collection<AttributeInstance> values) {
        this.entityId = entityId;
        this.attributes = Lists.newArrayList();
        for (AttributeInstance value : values) {
            this.attributes.add(new AttributeSnapshot(value.getAttribute(), value.getBaseValue(), value.getModifiers()));
        }
    }

    private ClientboundUpdateAttributesPacket(int entityId, List<AttributeSnapshot> attributes) {
        this.entityId = entityId;
        this.attributes = attributes;
    }

    @Override
    public PacketType<ClientboundUpdateAttributesPacket> type() {
        return GamePacketTypes.CLIENTBOUND_UPDATE_ATTRIBUTES;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleUpdateAttributes(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public List<AttributeSnapshot> getValues() {
        return this.attributes;
    }

    public record AttributeSnapshot(Holder<Attribute> attribute, double base, Collection<AttributeModifier> modifiers) {
        public static final StreamCodec<ByteBuf, AttributeModifier> MODIFIER_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, AttributeModifier::id, ByteBufCodecs.DOUBLE, AttributeModifier::amount, AttributeModifier.Operation.STREAM_CODEC, AttributeModifier::operation, AttributeModifier::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, AttributeSnapshot> STREAM_CODEC = StreamCodec.composite(Attribute.STREAM_CODEC, AttributeSnapshot::attribute, ByteBufCodecs.DOUBLE, AttributeSnapshot::base, MODIFIER_STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)), AttributeSnapshot::modifiers, AttributeSnapshot::new);
    }
}

