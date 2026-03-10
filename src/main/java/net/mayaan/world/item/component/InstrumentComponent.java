/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.item.component;

import com.mojang.serialization.Codec;
import java.util.function.Consumer;
import net.mayaan.ChatFormatting;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentUtils;
import net.mayaan.network.chat.Style;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.Instrument;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipProvider;

public record InstrumentComponent(Holder<Instrument> instrument) implements TooltipProvider
{
    public static final Codec<InstrumentComponent> CODEC = Instrument.CODEC.xmap(InstrumentComponent::new, InstrumentComponent::instrument);
    public static final StreamCodec<RegistryFriendlyByteBuf, InstrumentComponent> STREAM_CODEC = Instrument.STREAM_CODEC.map(InstrumentComponent::new, InstrumentComponent::instrument);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(ComponentUtils.mergeStyles(this.instrument.value().description(), Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }
}

