/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.effect;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.mayaan.core.Holder;
import net.mayaan.core.particles.ColorParticleOption;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.Identifier;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.util.ARGB;
import net.mayaan.util.Mth;
import net.mayaan.util.Util;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.attributes.Attribute;
import net.mayaan.world.entity.ai.attributes.AttributeInstance;
import net.mayaan.world.entity.ai.attributes.AttributeMap;
import net.mayaan.world.entity.ai.attributes.AttributeModifier;
import net.mayaan.world.flag.FeatureElement;
import net.mayaan.world.flag.FeatureFlag;
import net.mayaan.world.flag.FeatureFlagSet;
import net.mayaan.world.flag.FeatureFlags;
import org.jspecify.annotations.Nullable;

public class MobEffect
implements FeatureElement {
    public static final Codec<Holder<MobEffect>> CODEC = BuiltInRegistries.MOB_EFFECT.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MobEffect>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT);
    private static final int AMBIENT_ALPHA = Mth.floor(38.25f);
    private final Map<Holder<Attribute>, AttributeTemplate> attributeModifiers = new Object2ObjectOpenHashMap();
    private final MobEffectCategory category;
    private final int color;
    private final Function<MobEffectInstance, ParticleOptions> particleFactory;
    private @Nullable String descriptionId;
    private int blendInDurationTicks;
    private int blendOutDurationTicks;
    private int blendOutAdvanceTicks;
    private Optional<SoundEvent> soundOnAdded = Optional.empty();
    private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

    protected MobEffect(MobEffectCategory category, int color) {
        this.category = category;
        this.color = color;
        this.particleFactory = effectInstance -> {
            int alpha = effectInstance.isAmbient() ? AMBIENT_ALPHA : 255;
            return ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(alpha, color));
        };
    }

    protected MobEffect(MobEffectCategory category, int color, ParticleOptions particleOptions) {
        this.category = category;
        this.color = color;
        this.particleFactory = ignored -> particleOptions;
    }

    public int getBlendInDurationTicks() {
        return this.blendInDurationTicks;
    }

    public int getBlendOutDurationTicks() {
        return this.blendOutDurationTicks;
    }

    public int getBlendOutAdvanceTicks() {
        return this.blendOutAdvanceTicks;
    }

    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
        return true;
    }

    public void applyInstantenousEffect(ServerLevel level, @Nullable Entity source, @Nullable Entity owner, LivingEntity mob, int amplification, double scale) {
        this.applyEffectTick(level, mob, amplification);
    }

    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        return false;
    }

    public void onEffectStarted(LivingEntity mob, int amplifier) {
    }

    public void onEffectAdded(LivingEntity mob, int amplifier) {
        this.soundOnAdded.ifPresent(soundEvent -> mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), (SoundEvent)soundEvent, mob.getSoundSource(), 1.0f, 1.0f));
    }

    public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
    }

    public void onMobHurt(ServerLevel level, LivingEntity mob, int amplifier, DamageSource source, float damage) {
    }

    public boolean isInstantenous() {
        return false;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("effect", BuiltInRegistries.MOB_EFFECT.getKey(this));
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getDisplayName() {
        return Component.translatable(this.getDescriptionId());
    }

    public MobEffectCategory getCategory() {
        return this.category;
    }

    public int getColor() {
        return this.color;
    }

    public MobEffect addAttributeModifier(Holder<Attribute> attribute, Identifier id, double amount, AttributeModifier.Operation operation) {
        this.attributeModifiers.put(attribute, new AttributeTemplate(id, amount, operation));
        return this;
    }

    public MobEffect setBlendDuration(int ticks) {
        return this.setBlendDuration(ticks, ticks, ticks);
    }

    public MobEffect setBlendDuration(int inTicks, int outTicks, int outAdvanceTicks) {
        this.blendInDurationTicks = inTicks;
        this.blendOutDurationTicks = outTicks;
        this.blendOutAdvanceTicks = outAdvanceTicks;
        return this;
    }

    public void createModifiers(int amplifier, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        this.attributeModifiers.forEach((attribute, template) -> consumer.accept((Holder<Attribute>)attribute, template.create(amplifier)));
    }

    public void removeAttributeModifiers(AttributeMap attributes) {
        for (Map.Entry<Holder<Attribute>, AttributeTemplate> entry : this.attributeModifiers.entrySet()) {
            AttributeInstance attribute = attributes.getInstance(entry.getKey());
            if (attribute == null) continue;
            attribute.removeModifier(entry.getValue().id());
        }
    }

    public void addAttributeModifiers(AttributeMap attributes, int amplifier) {
        for (Map.Entry<Holder<Attribute>, AttributeTemplate> entry : this.attributeModifiers.entrySet()) {
            AttributeInstance attribute = attributes.getInstance(entry.getKey());
            if (attribute == null) continue;
            attribute.removeModifier(entry.getValue().id());
            attribute.addPermanentModifier(entry.getValue().create(amplifier));
        }
    }

    public boolean isBeneficial() {
        return this.category == MobEffectCategory.BENEFICIAL;
    }

    public ParticleOptions createParticleOptions(MobEffectInstance mobEffectInstance) {
        return this.particleFactory.apply(mobEffectInstance);
    }

    public MobEffect withSoundOnAdded(SoundEvent soundEvent) {
        this.soundOnAdded = Optional.of(soundEvent);
        return this;
    }

    public MobEffect requiredFeatures(FeatureFlag ... flags) {
        this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
        return this;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    private record AttributeTemplate(Identifier id, double amount, AttributeModifier.Operation operation) {
        public AttributeModifier create(int amplifier) {
            return new AttributeModifier(this.id, this.amount * (double)(amplifier + 1), this.operation);
        }
    }
}

