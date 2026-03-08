/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.item.alchemy;

import com.mojang.serialization.Codec;
import java.util.List;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.flag.FeatureElement;
import net.mayaan.world.flag.FeatureFlag;
import net.mayaan.world.flag.FeatureFlagSet;
import net.mayaan.world.flag.FeatureFlags;

public class Potion
implements FeatureElement {
    public static final Codec<Holder<Potion>> CODEC = BuiltInRegistries.POTION.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Potion>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.POTION);
    private final String name;
    private final List<MobEffectInstance> effects;
    private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

    public Potion(String name, MobEffectInstance ... effects) {
        this.name = name;
        this.effects = List.of(effects);
    }

    public Potion requiredFeatures(FeatureFlag ... flags) {
        this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
        return this;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    public List<MobEffectInstance> getEffects() {
        return this.effects;
    }

    public String name() {
        return this.name;
    }

    public boolean hasInstantEffects() {
        for (MobEffectInstance effect : this.effects) {
            if (!effect.getEffect().value().isInstantenous()) continue;
            return true;
        }
        return false;
    }
}

