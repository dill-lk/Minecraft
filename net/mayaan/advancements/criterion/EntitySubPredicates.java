/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.advancements.criterion;

import com.mojang.serialization.MapCodec;
import net.mayaan.advancements.criterion.EntitySubPredicate;
import net.mayaan.advancements.criterion.FishingHookPredicate;
import net.mayaan.advancements.criterion.LightningBoltPredicate;
import net.mayaan.advancements.criterion.PlayerPredicate;
import net.mayaan.advancements.criterion.RaiderPredicate;
import net.mayaan.advancements.criterion.SheepPredicate;
import net.mayaan.advancements.criterion.SlimePredicate;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;

public class EntitySubPredicates {
    public static final MapCodec<LightningBoltPredicate> LIGHTNING = EntitySubPredicates.register("lightning", LightningBoltPredicate.CODEC);
    public static final MapCodec<FishingHookPredicate> FISHING_HOOK = EntitySubPredicates.register("fishing_hook", FishingHookPredicate.CODEC);
    public static final MapCodec<PlayerPredicate> PLAYER = EntitySubPredicates.register("player", PlayerPredicate.CODEC);
    public static final MapCodec<SlimePredicate> SLIME = EntitySubPredicates.register("slime", SlimePredicate.CODEC);
    public static final MapCodec<RaiderPredicate> RAIDER = EntitySubPredicates.register("raider", RaiderPredicate.CODEC);
    public static final MapCodec<SheepPredicate> SHEEP = EntitySubPredicates.register("sheep", SheepPredicate.CODEC);

    private static <T extends EntitySubPredicate> MapCodec<T> register(String id, MapCodec<T> value) {
        return Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, id, value);
    }

    public static MapCodec<? extends EntitySubPredicate> bootstrap(Registry<MapCodec<? extends EntitySubPredicate>> registry) {
        return LIGHTNING;
    }
}

