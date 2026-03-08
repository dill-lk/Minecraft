/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.item.crafting.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.flag.FeatureFlagSet;
import net.mayaan.world.item.crafting.display.SlotDisplay;

public interface RecipeDisplay {
    public static final Codec<RecipeDisplay> CODEC = BuiltInRegistries.RECIPE_DISPLAY.byNameCodec().dispatch(RecipeDisplay::type, Type::codec);
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeDisplay> STREAM_CODEC = ByteBufCodecs.registry(Registries.RECIPE_DISPLAY).dispatch(RecipeDisplay::type, Type::streamCodec);

    public SlotDisplay result();

    public SlotDisplay craftingStation();

    public Type<? extends RecipeDisplay> type();

    default public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return this.result().isEnabled(enabledFeatures) && this.craftingStation().isEnabled(enabledFeatures);
    }

    public record Type<T extends RecipeDisplay>(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
    }
}

