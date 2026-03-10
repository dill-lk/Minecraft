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
package net.mayaan.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.particles.ExplosionParticleInfo;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.core.registries.Registries;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.util.random.WeightedList;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.damagesource.DamageType;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.item.enchantment.EnchantedItemInUse;
import net.mayaan.world.item.enchantment.LevelBasedValue;
import net.mayaan.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.SimpleExplosionDamageCalculator;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record ExplodeEffect(boolean attributeToUser, Optional<Holder<DamageType>> damageType, Optional<LevelBasedValue> knockbackMultiplier, Optional<HolderSet<Block>> immuneBlocks, Vec3 offset, LevelBasedValue radius, boolean createFire, Level.ExplosionInteraction blockInteraction, ParticleOptions smallParticle, ParticleOptions largeParticle, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> sound) implements EnchantmentEntityEffect
{
    public static final MapCodec<ExplodeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("attribute_to_user", (Object)false).forGetter(ExplodeEffect::attributeToUser), (App)DamageType.CODEC.optionalFieldOf("damage_type").forGetter(ExplodeEffect::damageType), (App)LevelBasedValue.CODEC.optionalFieldOf("knockback_multiplier").forGetter(ExplodeEffect::knockbackMultiplier), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(ExplodeEffect::immuneBlocks), (App)Vec3.CODEC.optionalFieldOf("offset", (Object)Vec3.ZERO).forGetter(ExplodeEffect::offset), (App)LevelBasedValue.CODEC.fieldOf("radius").forGetter(ExplodeEffect::radius), (App)Codec.BOOL.optionalFieldOf("create_fire", (Object)false).forGetter(ExplodeEffect::createFire), (App)Level.ExplosionInteraction.CODEC.fieldOf("block_interaction").forGetter(ExplodeEffect::blockInteraction), (App)ParticleTypes.CODEC.fieldOf("small_particle").forGetter(ExplodeEffect::smallParticle), (App)ParticleTypes.CODEC.fieldOf("large_particle").forGetter(ExplodeEffect::largeParticle), (App)WeightedList.codec(ExplosionParticleInfo.CODEC).optionalFieldOf("block_particles", WeightedList.of()).forGetter(ExplodeEffect::blockParticles), (App)SoundEvent.CODEC.fieldOf("sound").forGetter(ExplodeEffect::sound)).apply((Applicative)i, ExplodeEffect::new));

    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
        Vec3 pos = position.add(this.offset);
        serverLevel.explode(this.attributeToUser ? entity : null, this.getDamageSource(entity, pos), new SimpleExplosionDamageCalculator(this.blockInteraction != Level.ExplosionInteraction.NONE, this.damageType.isPresent(), this.knockbackMultiplier.map(value -> Float.valueOf(value.calculate(enchantmentLevel))), this.immuneBlocks), pos.x(), pos.y(), pos.z(), Math.max(this.radius.calculate(enchantmentLevel), 0.0f), this.createFire, this.blockInteraction, this.smallParticle, this.largeParticle, this.blockParticles, this.sound);
    }

    private @Nullable DamageSource getDamageSource(Entity entity, Vec3 position) {
        if (this.damageType.isEmpty()) {
            return null;
        }
        if (this.attributeToUser) {
            return new DamageSource(this.damageType.get(), entity);
        }
        return new DamageSource(this.damageType.get(), position);
    }

    public MapCodec<ExplodeEffect> codec() {
        return CODEC;
    }
}

