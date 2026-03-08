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
import net.mayaan.client.player.LocalPlayer;
import net.mayaan.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record IsSelected() implements ConditionalItemModelProperty
{
    public static final MapCodec<IsSelected> MAP_CODEC = MapCodec.unit((Object)new IsSelected());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        LocalPlayer player;
        return owner instanceof LocalPlayer && (player = (LocalPlayer)owner).getInventory().getSelectedItem() == itemStack;
    }

    public MapCodec<IsSelected> type() {
        return MAP_CODEC;
    }
}

