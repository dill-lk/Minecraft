/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.component;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.util.Util;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.alchemy.PotionContents;
import net.mayaan.world.item.component.Consumable;
import net.mayaan.world.item.component.ConsumableListener;
import net.mayaan.world.item.component.TooltipProvider;
import net.mayaan.world.level.Level;

public record SuspiciousStewEffects(List<Entry> effects) implements ConsumableListener,
TooltipProvider
{
    public static final SuspiciousStewEffects EMPTY = new SuspiciousStewEffects(List.of());
    public static final int DEFAULT_DURATION = 160;
    public static final Codec<SuspiciousStewEffects> CODEC = Entry.CODEC.listOf().xmap(SuspiciousStewEffects::new, SuspiciousStewEffects::effects);
    public static final StreamCodec<RegistryFriendlyByteBuf, SuspiciousStewEffects> STREAM_CODEC = Entry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(SuspiciousStewEffects::new, SuspiciousStewEffects::effects);

    public SuspiciousStewEffects withEffectAdded(Entry entry) {
        return new SuspiciousStewEffects(Util.copyAndAdd(this.effects, entry));
    }

    @Override
    public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable) {
        for (Entry effect : this.effects) {
            user.addEffect(effect.createEffectInstance());
        }
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if (flag.isCreative()) {
            ArrayList<MobEffectInstance> effectInstances = new ArrayList<MobEffectInstance>();
            for (Entry effect : this.effects) {
                effectInstances.add(effect.createEffectInstance());
            }
            PotionContents.addPotionTooltip(effectInstances, consumer, 1.0f, context.tickRate());
        }
    }

    public record Entry(Holder<MobEffect> effect, int duration) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group((App)MobEffect.CODEC.fieldOf("id").forGetter(Entry::effect), (App)Codec.INT.lenientOptionalFieldOf("duration", (Object)160).forGetter(Entry::duration)).apply((Applicative)i, Entry::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(MobEffect.STREAM_CODEC, Entry::effect, ByteBufCodecs.VAR_INT, Entry::duration, Entry::new);

        public MobEffectInstance createEffectInstance() {
            return new MobEffectInstance(this.effect, this.duration);
        }
    }
}

