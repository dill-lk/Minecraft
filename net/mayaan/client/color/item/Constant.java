/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.color.item;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.client.color.item.ItemTintSource;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.util.ARGB;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record Constant(int value) implements ItemTintSource
{
    public static final MapCodec<Constant> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("value").forGetter(Constant::value)).apply((Applicative)i, Constant::new));

    public Constant(int value) {
        this.value = value = ARGB.opaque(value);
    }

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return this.value;
    }

    public MapCodec<Constant> type() {
        return MAP_CODEC;
    }
}

