/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.component;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.tags.TagKey;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.damagesource.DamageType;

public record DamageResistant(TagKey<DamageType> types) {
    public static final Codec<DamageResistant> CODEC = RecordCodecBuilder.create(i -> i.group((App)TagKey.hashedCodec(Registries.DAMAGE_TYPE).fieldOf("types").forGetter(DamageResistant::types)).apply((Applicative)i, DamageResistant::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, DamageResistant> STREAM_CODEC = StreamCodec.composite(TagKey.streamCodec(Registries.DAMAGE_TYPE), DamageResistant::types, DamageResistant::new);

    public boolean isResistantTo(DamageSource source) {
        return source.is(this.types);
    }
}

