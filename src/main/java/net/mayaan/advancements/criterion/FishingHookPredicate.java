/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.advancements.criterion;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.advancements.criterion.EntitySubPredicate;
import net.mayaan.advancements.criterion.EntitySubPredicates;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.projectile.FishingHook;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record FishingHookPredicate(Optional<Boolean> inOpenWater) implements EntitySubPredicate
{
    public static final FishingHookPredicate ANY = new FishingHookPredicate(Optional.empty());
    public static final MapCodec<FishingHookPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("in_open_water").forGetter(FishingHookPredicate::inOpenWater)).apply((Applicative)i, FishingHookPredicate::new));

    public static FishingHookPredicate inOpenWater(boolean requirement) {
        return new FishingHookPredicate(Optional.of(requirement));
    }

    public MapCodec<FishingHookPredicate> codec() {
        return EntitySubPredicates.FISHING_HOOK;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        if (this.inOpenWater.isEmpty()) {
            return true;
        }
        if (entity instanceof FishingHook) {
            FishingHook hook = (FishingHook)entity;
            return this.inOpenWater.get().booleanValue() == hook.isOpenWaterFishing();
        }
        return false;
    }
}

