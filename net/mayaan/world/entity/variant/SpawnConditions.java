/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.entity.variant;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.world.entity.variant.BiomeCheck;
import net.mayaan.world.entity.variant.MoonBrightnessCheck;
import net.mayaan.world.entity.variant.SpawnCondition;
import net.mayaan.world.entity.variant.StructureCheck;

public class SpawnConditions {
    public static MapCodec<? extends SpawnCondition> bootstrap(Registry<MapCodec<? extends SpawnCondition>> registry) {
        Registry.register(registry, "structure", StructureCheck.MAP_CODEC);
        Registry.register(registry, "moon_brightness", MoonBrightnessCheck.MAP_CODEC);
        return Registry.register(registry, "biome", BiomeCheck.MAP_CODEC);
    }
}

