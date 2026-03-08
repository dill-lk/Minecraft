/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.component.DataComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;

public class ShieldItem
extends Item {
    public ShieldItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        DyeColor baseColor = itemStack.get(DataComponents.BASE_COLOR);
        if (baseColor != null) {
            return Component.translatable(this.descriptionId + "." + baseColor.getName());
        }
        return super.getName(itemStack);
    }
}

