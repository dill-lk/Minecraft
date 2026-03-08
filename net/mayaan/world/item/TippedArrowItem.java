/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.component.DataComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.ArrowItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.alchemy.PotionContents;
import net.mayaan.world.item.alchemy.Potions;

public class TippedArrowItem
extends ArrowItem {
    public TippedArrowItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemStack = super.getDefaultInstance();
        itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON));
        return itemStack;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        PotionContents potion = itemStack.get(DataComponents.POTION_CONTENTS);
        return potion != null ? potion.getName(this.descriptionId + ".effect.") : super.getName(itemStack);
    }
}

