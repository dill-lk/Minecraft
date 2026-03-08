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
package net.mayaan.client.renderer.item.properties.numeric;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.world.entity.ItemOwner;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record UseCycle(float period) implements RangeSelectItemModelProperty
{
    public static final MapCodec<UseCycle> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("period", (Object)Float.valueOf(1.0f)).forGetter(UseCycle::period)).apply((Applicative)i, UseCycle::new));

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        LivingEntity entity;
        LivingEntity livingEntity = entity = owner == null ? null : owner.asLivingEntity();
        if (entity == null || entity.getUseItem() != itemStack) {
            return 0.0f;
        }
        return (float)entity.getUseItemRemainingTicks() % this.period;
    }

    public MapCodec<UseCycle> type() {
        return MAP_CODEC;
    }
}

