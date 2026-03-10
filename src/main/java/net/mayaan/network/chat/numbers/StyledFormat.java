/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.network.chat.numbers;

import com.mojang.serialization.MapCodec;
import net.mayaan.ChatFormatting;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.network.chat.Style;
import net.mayaan.network.chat.numbers.NumberFormat;
import net.mayaan.network.chat.numbers.NumberFormatType;
import net.mayaan.network.codec.StreamCodec;

public record StyledFormat(Style style) implements NumberFormat
{
    public static final NumberFormatType<StyledFormat> TYPE = new NumberFormatType<StyledFormat>(){
        private static final MapCodec<StyledFormat> CODEC = Style.Serializer.MAP_CODEC.xmap(StyledFormat::new, StyledFormat::style);
        private static final StreamCodec<RegistryFriendlyByteBuf, StyledFormat> STREAM_CODEC = StreamCodec.composite(Style.Serializer.TRUSTED_STREAM_CODEC, StyledFormat::style, StyledFormat::new);

        @Override
        public MapCodec<StyledFormat> mapCodec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, StyledFormat> streamCodec() {
            return STREAM_CODEC;
        }
    };
    public static final StyledFormat NO_STYLE = new StyledFormat(Style.EMPTY);
    public static final StyledFormat SIDEBAR_DEFAULT = new StyledFormat(Style.EMPTY.withColor(ChatFormatting.RED));
    public static final StyledFormat PLAYER_LIST_DEFAULT = new StyledFormat(Style.EMPTY.withColor(ChatFormatting.YELLOW));

    @Override
    public MutableComponent format(int value) {
        return Component.literal(Integer.toString(value)).withStyle(this.style);
    }

    public NumberFormatType<StyledFormat> type() {
        return TYPE;
    }
}

