/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.advancements;

import java.util.List;
import net.mayaan.advancements.Advancement;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.Identifier;

public record AdvancementHolder(Identifier id, Advancement value) {
    public static final StreamCodec<RegistryFriendlyByteBuf, AdvancementHolder> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, AdvancementHolder::id, Advancement.STREAM_CODEC, AdvancementHolder::value, AdvancementHolder::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, List<AdvancementHolder>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdvancementHolder)) return false;
        AdvancementHolder holder = (AdvancementHolder)obj;
        if (!this.id.equals(holder.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}

