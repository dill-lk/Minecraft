/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item.properties.numeric;

import com.mojang.serialization.MapCodec;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.mayaan.world.entity.ItemOwner;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record Cooldown() implements RangeSelectItemModelProperty
{
    public static final MapCodec<Cooldown> MAP_CODEC = MapCodec.unit((Object)new Cooldown());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        float f;
        LivingEntity livingEntity;
        if (owner != null && (livingEntity = owner.asLivingEntity()) instanceof Player) {
            Player player = (Player)livingEntity;
            f = player.getCooldowns().getCooldownPercent(itemStack, 0.0f);
        } else {
            f = 0.0f;
        }
        return f;
    }

    public MapCodec<Cooldown> type() {
        return MAP_CODEC;
    }
}

