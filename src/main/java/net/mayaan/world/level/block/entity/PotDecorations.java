/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.block.entity;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.mayaan.ChatFormatting;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipProvider;

public record PotDecorations(Optional<Item> back, Optional<Item> left, Optional<Item> right, Optional<Item> front) implements TooltipProvider
{
    public static final PotDecorations EMPTY = new PotDecorations(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    public static final Codec<PotDecorations> CODEC = BuiltInRegistries.ITEM.byNameCodec().sizeLimitedListOf(4).xmap(PotDecorations::new, PotDecorations::ordered);
    public static final StreamCodec<RegistryFriendlyByteBuf, PotDecorations> STREAM_CODEC = ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list(4)).map(PotDecorations::new, PotDecorations::ordered);

    private PotDecorations(List<Item> items) {
        this(PotDecorations.getItem(items, 0), PotDecorations.getItem(items, 1), PotDecorations.getItem(items, 2), PotDecorations.getItem(items, 3));
    }

    public PotDecorations(Item back, Item left, Item right, Item front) {
        this(List.of(back, left, right, front));
    }

    private static Optional<Item> getItem(List<Item> sherds, int i) {
        if (i >= sherds.size()) {
            return Optional.empty();
        }
        Item item = sherds.get(i);
        return item == Items.BRICK ? Optional.empty() : Optional.of(item);
    }

    public List<Item> ordered() {
        return Stream.of(this.back, this.left, this.right, this.front).map(item -> item.orElse(Items.BRICK)).toList();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if (this.equals(EMPTY)) {
            return;
        }
        consumer.accept(CommonComponents.EMPTY);
        PotDecorations.addSideDetailsToTooltip(consumer, this.front);
        PotDecorations.addSideDetailsToTooltip(consumer, this.left);
        PotDecorations.addSideDetailsToTooltip(consumer, this.right);
        PotDecorations.addSideDetailsToTooltip(consumer, this.back);
    }

    private static void addSideDetailsToTooltip(Consumer<Component> consumer, Optional<Item> side) {
        consumer.accept(new ItemStack(side.orElse(Items.BRICK), 1).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY));
    }
}

