/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.color.item;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.client.color.item.ItemTintSource;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.core.component.DataComponents;
import net.mayaan.util.ARGB;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.CustomModelData;
import org.jspecify.annotations.Nullable;

public record CustomModelDataSource(int index, int defaultColor) implements ItemTintSource
{
    public static final MapCodec<CustomModelDataSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("index", (Object)0).forGetter(CustomModelDataSource::index), (App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(CustomModelDataSource::defaultColor)).apply((Applicative)i, CustomModelDataSource::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        Integer value;
        CustomModelData customModelData = itemStack.get(DataComponents.CUSTOM_MODEL_DATA);
        if (customModelData != null && (value = customModelData.getColor(this.index)) != null) {
            return ARGB.opaque(value);
        }
        return ARGB.opaque(this.defaultColor);
    }

    public MapCodec<CustomModelDataSource> type() {
        return MAP_CODEC;
    }
}

