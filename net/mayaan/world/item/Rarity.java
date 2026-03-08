/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.world.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.mayaan.ChatFormatting;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.util.ByIdMap;
import net.mayaan.util.StringRepresentable;

public enum Rarity implements StringRepresentable
{
    COMMON(0, "common", ChatFormatting.WHITE),
    UNCOMMON(1, "uncommon", ChatFormatting.YELLOW),
    RARE(2, "rare", ChatFormatting.AQUA),
    EPIC(3, "epic", ChatFormatting.LIGHT_PURPLE);

    public static final Codec<Rarity> CODEC;
    public static final IntFunction<Rarity> BY_ID;
    public static final StreamCodec<ByteBuf, Rarity> STREAM_CODEC;
    private final int id;
    private final String name;
    private final ChatFormatting color;

    private Rarity(int id, String name, ChatFormatting color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public ChatFormatting color() {
        return this.color;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    static {
        CODEC = StringRepresentable.fromValues(Rarity::values);
        BY_ID = ByIdMap.continuous(r -> r.id, Rarity.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, r -> r.id);
    }
}

