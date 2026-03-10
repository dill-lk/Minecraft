/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.storage.loot.providers.nbt;

import com.mojang.serialization.MapCodec;
import net.mayaan.nbt.Tag;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.LootContextUser;
import org.jspecify.annotations.Nullable;

public interface NbtProvider
extends LootContextUser {
    public @Nullable Tag get(LootContext var1);

    public MapCodec<? extends NbtProvider> codec();
}

