/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.entity.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.world.entity.variant.PriorityProvider;
import net.mayaan.world.entity.variant.SpawnContext;

public interface SpawnCondition
extends PriorityProvider.SelectorCondition<SpawnContext> {
    public static final Codec<SpawnCondition> CODEC = BuiltInRegistries.SPAWN_CONDITION_TYPE.byNameCodec().dispatch(SpawnCondition::codec, c -> c);

    public MapCodec<? extends SpawnCondition> codec();
}

