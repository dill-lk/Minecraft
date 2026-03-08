/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.item.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Consumer;
import net.mayaan.ChatFormatting;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;
import net.mayaan.world.item.component.TooltipProvider;

public record ChargedProjectiles(List<ItemStackTemplate> items) implements TooltipProvider
{
    public static final ChargedProjectiles EMPTY = new ChargedProjectiles(List.of());
    public static final Codec<ChargedProjectiles> CODEC = ItemStackTemplate.CODEC.listOf().xmap(ChargedProjectiles::new, projectiles -> projectiles.items);
    public static final StreamCodec<RegistryFriendlyByteBuf, ChargedProjectiles> STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ChargedProjectiles::new, projectiles -> projectiles.items);

    public static ChargedProjectiles of(ItemStackTemplate stack) {
        return new ChargedProjectiles(List.of(stack));
    }

    public static ChargedProjectiles ofNonEmpty(List<ItemStack> items) {
        return new ChargedProjectiles(List.copyOf(Lists.transform(items, ItemStackTemplate::fromNonEmptyStack)));
    }

    public boolean contains(Item item) {
        for (ItemStackTemplate projectile : this.items) {
            if (!projectile.is(item)) continue;
            return true;
        }
        return false;
    }

    public List<ItemStack> itemCopies() {
        return Lists.transform(this.items, ItemStackTemplate::create);
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        ItemStack current = null;
        int count = 0;
        for (ItemStackTemplate projectileTemplate : this.items) {
            ItemStack projectile = projectileTemplate.create();
            if (current == null) {
                current = projectile;
                count = 1;
                continue;
            }
            if (ItemStack.matches(current, projectile)) {
                ++count;
                continue;
            }
            ChargedProjectiles.addProjectileTooltip(context, consumer, current, count);
            current = projectile;
            count = 1;
        }
        if (current != null) {
            ChargedProjectiles.addProjectileTooltip(context, consumer, current, count);
        }
    }

    private static void addProjectileTooltip(Item.TooltipContext context, Consumer<Component> consumer, ItemStack projectile, int count) {
        if (count == 1) {
            consumer.accept(Component.translatable("item.minecraft.crossbow.projectile.single", projectile.getDisplayName()));
        } else {
            consumer.accept(Component.translatable("item.minecraft.crossbow.projectile.multiple", count, projectile.getDisplayName()));
        }
        TooltipDisplay projectileDisplay = projectile.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        projectile.addDetailsToTooltip(context, projectileDisplay, null, TooltipFlag.NORMAL, line -> consumer.accept(Component.literal("  ").append((Component)line).withStyle(ChatFormatting.GRAY)));
    }
}

