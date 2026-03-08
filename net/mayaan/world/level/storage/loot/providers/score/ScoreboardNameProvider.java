/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.storage.loot.providers.score;

import com.mojang.serialization.MapCodec;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.LootContextUser;
import net.mayaan.world.scores.ScoreHolder;
import org.jspecify.annotations.Nullable;

public interface ScoreboardNameProvider
extends LootContextUser {
    public @Nullable ScoreHolder getScoreHolder(LootContext var1);

    public MapCodec<? extends ScoreboardNameProvider> codec();
}

