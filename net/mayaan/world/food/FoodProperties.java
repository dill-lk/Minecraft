/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.food;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.food.FoodConstants;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.Consumable;
import net.mayaan.world.item.component.ConsumableListener;
import net.mayaan.world.level.Level;

public record FoodProperties(int nutrition, float saturation, boolean canAlwaysEat) implements ConsumableListener
{
    public static final Codec<FoodProperties> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(FoodProperties::nutrition), (App)Codec.FLOAT.fieldOf("saturation").forGetter(FoodProperties::saturation), (App)Codec.BOOL.optionalFieldOf("can_always_eat", (Object)false).forGetter(FoodProperties::canAlwaysEat)).apply((Applicative)i, FoodProperties::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FoodProperties> DIRECT_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, FoodProperties::nutrition, ByteBufCodecs.FLOAT, FoodProperties::saturation, ByteBufCodecs.BOOL, FoodProperties::canAlwaysEat, FoodProperties::new);

    @Override
    public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable) {
        RandomSource random = user.getRandom();
        level.playSound(null, user.getX(), user.getY(), user.getZ(), consumable.sound().value(), SoundSource.NEUTRAL, 1.0f, random.triangle(1.0f, 0.4f));
        if (user instanceof Player) {
            Player player = (Player)user;
            player.getFoodData().eat(this);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5f, Mth.randomBetween(random, 0.9f, 1.0f));
        }
    }

    public static class Builder {
        private int nutrition;
        private float saturationModifier;
        private boolean canAlwaysEat;

        public Builder nutrition(int nutrition) {
            this.nutrition = nutrition;
            return this;
        }

        public Builder saturationModifier(float saturationModifier) {
            this.saturationModifier = saturationModifier;
            return this;
        }

        public Builder alwaysEdible() {
            this.canAlwaysEat = true;
            return this;
        }

        public FoodProperties build() {
            float saturation = FoodConstants.saturationByModifier(this.nutrition, this.saturationModifier);
            return new FoodProperties(this.nutrition, saturation, this.canAlwaysEat);
        }
    }
}

