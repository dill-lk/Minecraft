/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.network.chat.numbers;

import com.mojang.serialization.MapCodec;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.network.chat.numbers.NumberFormat;
import net.mayaan.network.chat.numbers.NumberFormatType;
import net.mayaan.network.codec.StreamCodec;

public record FixedFormat(Component value) implements NumberFormat
{
    public static final NumberFormatType<FixedFormat> TYPE = new NumberFormatType<FixedFormat>(){
        private static final MapCodec<FixedFormat> CODEC = ComponentSerialization.CODEC.fieldOf("value").xmap(FixedFormat::new, FixedFormat::value);
        private static final StreamCodec<RegistryFriendlyByteBuf, FixedFormat> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, FixedFormat::value, FixedFormat::new);

        @Override
        public MapCodec<FixedFormat> mapCodec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FixedFormat> streamCodec() {
            return STREAM_CODEC;
        }
    };

    @Override
    public MutableComponent format(int value) {
        return this.value.copy();
    }

    public NumberFormatType<FixedFormat> type() {
        return TYPE;
    }
}

