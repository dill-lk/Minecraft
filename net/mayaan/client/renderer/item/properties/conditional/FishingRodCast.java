/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.entity.FishingHookRenderer;
import net.mayaan.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.mayaan.world.entity.HumanoidArm;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record FishingRodCast() implements ConditionalItemModelProperty
{
    public static final MapCodec<FishingRodCast> MAP_CODEC = MapCodec.unit((Object)new FishingRodCast());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        if (owner instanceof Player) {
            Player player = (Player)owner;
            if (player.fishing != null) {
                HumanoidArm holdingArm = FishingHookRenderer.getHoldingArm(player);
                return owner.getItemHeldByArm(holdingArm) == itemStack;
            }
        }
        return false;
    }

    public MapCodec<FishingRodCast> type() {
        return MAP_CODEC;
    }
}

