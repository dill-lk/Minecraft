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
import net.mayaan.world.item.component.MapItemColor;
import org.jspecify.annotations.Nullable;

public record MapColor(int defaultColor) implements ItemTintSource
{
    public static final MapCodec<MapColor> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(MapColor::defaultColor)).apply((Applicative)i, MapColor::new));

    public MapColor() {
        this(MapItemColor.DEFAULT.rgb());
    }

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        MapItemColor component = itemStack.get(DataComponents.MAP_COLOR);
        if (component != null) {
            return ARGB.opaque(component.rgb());
        }
        return ARGB.opaque(this.defaultColor);
    }

    public MapCodec<MapColor> type() {
        return MAP_CODEC;
    }
}

