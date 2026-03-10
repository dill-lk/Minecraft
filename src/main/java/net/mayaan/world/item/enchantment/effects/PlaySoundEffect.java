/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.mayaan.core.Holder;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.FloatProvider;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.item.enchantment.EnchantedItemInUse;
import net.mayaan.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.mayaan.world.phys.Vec3;

public record PlaySoundEffect(List<Holder<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) implements EnchantmentEntityEffect
{
    public static final MapCodec<PlaySoundEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.compactListCodec(SoundEvent.CODEC, SoundEvent.CODEC.sizeLimitedListOf(255)).fieldOf("sound").forGetter(PlaySoundEffect::soundEvents), (App)FloatProvider.codec(1.0E-5f, 10.0f).fieldOf("volume").forGetter(PlaySoundEffect::volume), (App)FloatProvider.codec(1.0E-5f, 2.0f).fieldOf("pitch").forGetter(PlaySoundEffect::pitch)).apply((Applicative)i, PlaySoundEffect::new));

    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
        if (entity.isSilent()) {
            return;
        }
        RandomSource random = entity.getRandom();
        int index = Mth.clamp(enchantmentLevel - 1, 0, this.soundEvents.size() - 1);
        serverLevel.playSound(null, position.x(), position.y(), position.z(), this.soundEvents.get(index), entity.getSoundSource(), this.volume.sample(random), this.pitch.sample(random));
    }

    public MapCodec<PlaySoundEffect> codec() {
        return CODEC;
    }
}

