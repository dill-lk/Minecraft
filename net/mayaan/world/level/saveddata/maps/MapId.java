/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.world.level.saveddata.maps;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import net.mayaan.ChatFormatting;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.MapPostProcessing;
import net.mayaan.world.item.component.TooltipProvider;
import net.mayaan.world.level.saveddata.maps.MapItemSavedData;

public record MapId(int id) implements TooltipProvider
{
    public static final Codec<MapId> CODEC = Codec.INT.xmap(MapId::new, MapId::id);
    public static final StreamCodec<ByteBuf, MapId> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(MapId::new, MapId::id);
    private static final Component LOCKED_TEXT = Component.translatable("filled_map.locked").withStyle(ChatFormatting.GRAY);

    public String key() {
        return "maps/" + this.id;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        MapItemSavedData data = context.mapData(this);
        if (data == null) {
            consumer.accept(Component.translatable("filled_map.unknown").withStyle(ChatFormatting.GRAY));
            return;
        }
        MapPostProcessing postProcessing = components.get(DataComponents.MAP_POST_PROCESSING);
        if (components.get(DataComponents.CUSTOM_NAME) == null && postProcessing == null) {
            consumer.accept(Component.translatable("filled_map.id", this.id).withStyle(ChatFormatting.GRAY));
        }
        if (data.locked || postProcessing == MapPostProcessing.LOCK) {
            consumer.accept(LOCKED_TEXT);
        }
        if (flag.isAdvanced()) {
            byte scaleToAdd = postProcessing == MapPostProcessing.SCALE ? (byte)1 : 0;
            int scale = Math.min(data.scale + scaleToAdd, 4);
            consumer.accept(Component.translatable("filled_map.scale", 1 << scale).withStyle(ChatFormatting.GRAY));
            consumer.accept(Component.translatable("filled_map.level", scale, 4).withStyle(ChatFormatting.GRAY));
        }
    }
}

