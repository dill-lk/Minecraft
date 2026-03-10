/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.item.component;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Consumer;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.alchemy.PotionContents;
import net.mayaan.world.item.component.Consumable;
import net.mayaan.world.item.component.ConsumableListener;
import net.mayaan.world.item.component.TooltipProvider;
import net.mayaan.world.level.Level;

public record OminousBottleAmplifier(int value) implements ConsumableListener,
TooltipProvider
{
    public static final int EFFECT_DURATION = 120000;
    public static final int MIN_AMPLIFIER = 0;
    public static final int MAX_AMPLIFIER = 4;
    public static final Codec<OminousBottleAmplifier> CODEC = ExtraCodecs.intRange(0, 4).xmap(OminousBottleAmplifier::new, OminousBottleAmplifier::value);
    public static final StreamCodec<RegistryFriendlyByteBuf, OminousBottleAmplifier> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, OminousBottleAmplifier::value, OminousBottleAmplifier::new);

    @Override
    public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable) {
        user.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 120000, this.value, false, false, true));
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        List<MobEffectInstance> effects = List.of(new MobEffectInstance(MobEffects.BAD_OMEN, 120000, this.value, false, false, true));
        PotionContents.addPotionTooltip(effects, consumer, 1.0f, context.tickRate());
    }
}

