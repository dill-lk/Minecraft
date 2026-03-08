/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.item.crafting;

import com.mojang.serialization.MapCodec;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.crafting.Recipe;

public record RecipeSerializer<T extends Recipe<?>>(MapCodec<T> codec, @Deprecated StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
}

