/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.item.consume_effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.mayaan.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.mayaan.world.item.consume_effects.PlaySoundConsumeEffect;
import net.mayaan.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import net.mayaan.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import net.mayaan.world.level.Level;

public interface ConsumeEffect {
    public static final Codec<ConsumeEffect> CODEC = BuiltInRegistries.CONSUME_EFFECT_TYPE.byNameCodec().dispatch(ConsumeEffect::getType, Type::codec);
    public static final StreamCodec<RegistryFriendlyByteBuf, ConsumeEffect> STREAM_CODEC = ByteBufCodecs.registry(Registries.CONSUME_EFFECT_TYPE).dispatch(ConsumeEffect::getType, Type::streamCodec);

    public Type<? extends ConsumeEffect> getType();

    public boolean apply(Level var1, ItemStack var2, LivingEntity var3);

    public record Type<T extends ConsumeEffect>(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        public static final Type<ApplyStatusEffectsConsumeEffect> APPLY_EFFECTS = Type.register("apply_effects", ApplyStatusEffectsConsumeEffect.CODEC, ApplyStatusEffectsConsumeEffect.STREAM_CODEC);
        public static final Type<RemoveStatusEffectsConsumeEffect> REMOVE_EFFECTS = Type.register("remove_effects", RemoveStatusEffectsConsumeEffect.CODEC, RemoveStatusEffectsConsumeEffect.STREAM_CODEC);
        public static final Type<ClearAllStatusEffectsConsumeEffect> CLEAR_ALL_EFFECTS = Type.register("clear_all_effects", ClearAllStatusEffectsConsumeEffect.CODEC, ClearAllStatusEffectsConsumeEffect.STREAM_CODEC);
        public static final Type<TeleportRandomlyConsumeEffect> TELEPORT_RANDOMLY = Type.register("teleport_randomly", TeleportRandomlyConsumeEffect.CODEC, TeleportRandomlyConsumeEffect.STREAM_CODEC);
        public static final Type<PlaySoundConsumeEffect> PLAY_SOUND = Type.register("play_sound", PlaySoundConsumeEffect.CODEC, PlaySoundConsumeEffect.STREAM_CODEC);

        private static <T extends ConsumeEffect> Type<T> register(String name, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
            return Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, name, new Type<T>(codec, streamCodec));
        }
    }
}

