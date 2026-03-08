/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.item.component;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Consumer;
import net.mayaan.ChatFormatting;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipProvider;
import net.mayaan.world.level.block.entity.BeehiveBlockEntity;

public record Bees(List<BeehiveBlockEntity.Occupant> bees) implements TooltipProvider
{
    public static final Codec<Bees> CODEC = BeehiveBlockEntity.Occupant.LIST_CODEC.xmap(Bees::new, Bees::bees);
    public static final StreamCodec<RegistryFriendlyByteBuf, Bees> STREAM_CODEC = BeehiveBlockEntity.Occupant.STREAM_CODEC.apply(ByteBufCodecs.list()).map(Bees::new, Bees::bees);
    public static final Bees EMPTY = new Bees(List.of());

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(Component.translatable("container.beehive.bees", this.bees.size(), 3).withStyle(ChatFormatting.GRAY));
    }
}

