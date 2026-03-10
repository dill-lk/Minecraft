/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import java.util.function.Consumer;
import net.mayaan.ChatFormatting;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;

public class DiscFragmentItem
extends Item {
    public DiscFragmentItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(this.getDisplayName().withStyle(ChatFormatting.GRAY));
    }

    public MutableComponent getDisplayName() {
        return Component.translatable(this.descriptionId + ".desc");
    }
}

