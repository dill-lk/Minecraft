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
import net.mayaan.ChatFormatting;
import net.mayaan.client.color.item.ItemTintSource;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.util.ARGB;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.scores.PlayerTeam;
import net.mayaan.world.scores.Team;
import org.jspecify.annotations.Nullable;

public record TeamColor(int defaultColor) implements ItemTintSource
{
    public static final MapCodec<TeamColor> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(TeamColor::defaultColor)).apply((Applicative)i, TeamColor::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        ChatFormatting color;
        PlayerTeam team;
        if (owner != null && (team = owner.getTeam()) != null && (color = ((Team)team).getColor()).getColor() != null) {
            return ARGB.opaque(color.getColor());
        }
        return ARGB.opaque(this.defaultColor);
    }

    public MapCodec<TeamColor> type() {
        return MAP_CODEC;
    }
}

