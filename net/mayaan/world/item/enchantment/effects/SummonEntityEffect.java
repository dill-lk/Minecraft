/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.registries.Registries;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LightningBolt;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.enchantment.EnchantedItemInUse;
import net.mayaan.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.mayaan.world.level.Level;
import net.mayaan.world.phys.Vec3;

public record SummonEntityEffect(HolderSet<EntityType<?>> entityTypes, boolean joinTeam) implements EnchantmentEntityEffect
{
    public static final MapCodec<SummonEntityEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("entity").forGetter(SummonEntityEffect::entityTypes), (App)Codec.BOOL.optionalFieldOf("join_team", (Object)false).forGetter(SummonEntityEffect::joinTeam)).apply((Applicative)i, SummonEntityEffect::new));

    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
        BlockPos blockPos = BlockPos.containing(position);
        if (!Level.isInSpawnableBounds(blockPos)) {
            return;
        }
        Optional<Holder<EntityType<?>>> entityType = this.entityTypes().getRandomElement(serverLevel.getRandom());
        if (entityType.isEmpty()) {
            return;
        }
        Object spawned = entityType.get().value().spawn(serverLevel, blockPos, EntitySpawnReason.TRIGGERED);
        if (spawned == null) {
            return;
        }
        if (spawned instanceof LightningBolt) {
            LightningBolt lightningBolt = (LightningBolt)spawned;
            LivingEntity livingEntity = item.owner();
            if (livingEntity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)livingEntity;
                lightningBolt.setCause(player);
            }
        }
        if (this.joinTeam && entity.getTeam() != null) {
            serverLevel.getScoreboard().addPlayerToTeam(((Entity)spawned).getScoreboardName(), entity.getTeam());
        }
        ((Entity)spawned).snapTo(position.x, position.y, position.z, ((Entity)spawned).getYRot(), ((Entity)spawned).getXRot());
    }

    public MapCodec<SummonEntityEffect> codec() {
        return CODEC;
    }
}

