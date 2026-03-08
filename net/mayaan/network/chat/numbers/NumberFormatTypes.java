/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.network.chat.numbers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.numbers.BlankFormat;
import net.mayaan.network.chat.numbers.FixedFormat;
import net.mayaan.network.chat.numbers.NumberFormat;
import net.mayaan.network.chat.numbers.NumberFormatType;
import net.mayaan.network.chat.numbers.StyledFormat;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;

public class NumberFormatTypes {
    public static final MapCodec<NumberFormat> MAP_CODEC = BuiltInRegistries.NUMBER_FORMAT_TYPE.byNameCodec().dispatchMap(NumberFormat::type, NumberFormatType::mapCodec);
    public static final Codec<NumberFormat> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, NumberFormat> STREAM_CODEC = ByteBufCodecs.registry(Registries.NUMBER_FORMAT_TYPE).dispatch(NumberFormat::type, NumberFormatType::streamCodec);
    public static final StreamCodec<RegistryFriendlyByteBuf, Optional<NumberFormat>> OPTIONAL_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs::optional);

    public static NumberFormatType<?> bootstrap(Registry<NumberFormatType<?>> registry) {
        Registry.register(registry, "blank", BlankFormat.TYPE);
        Registry.register(registry, "styled", StyledFormat.TYPE);
        return Registry.register(registry, "fixed", FixedFormat.TYPE);
    }
}

