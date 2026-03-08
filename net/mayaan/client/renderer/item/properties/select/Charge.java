/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.item.properties.select.SelectItemModelProperty;
import net.mayaan.core.component.DataComponents;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.CrossbowItem;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.component.ChargedProjectiles;
import org.jspecify.annotations.Nullable;

public record Charge() implements SelectItemModelProperty<CrossbowItem.ChargeType>
{
    public static final Codec<CrossbowItem.ChargeType> VALUE_CODEC = CrossbowItem.ChargeType.CODEC;
    public static final SelectItemModelProperty.Type<Charge, CrossbowItem.ChargeType> TYPE = SelectItemModelProperty.Type.create(MapCodec.unit((Object)new Charge()), VALUE_CODEC);

    @Override
    public CrossbowItem.ChargeType get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        ChargedProjectiles projectiles = itemStack.get(DataComponents.CHARGED_PROJECTILES);
        if (projectiles == null || projectiles.isEmpty()) {
            return CrossbowItem.ChargeType.NONE;
        }
        if (projectiles.contains(Items.FIREWORK_ROCKET)) {
            return CrossbowItem.ChargeType.ROCKET;
        }
        return CrossbowItem.ChargeType.ARROW;
    }

    @Override
    public SelectItemModelProperty.Type<Charge, CrossbowItem.ChargeType> type() {
        return TYPE;
    }

    @Override
    public Codec<CrossbowItem.ChargeType> valueCodec() {
        return VALUE_CODEC;
    }
}

