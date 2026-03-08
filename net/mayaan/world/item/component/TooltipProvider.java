/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.component;

import java.util.function.Consumer;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.TooltipFlag;

public interface TooltipProvider {
    public void addToTooltip(Item.TooltipContext var1, Consumer<Component> var2, TooltipFlag var3, DataComponentGetter var4);
}

