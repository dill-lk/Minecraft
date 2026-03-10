/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.util.ByIdMap;
import net.mayaan.util.StringRepresentable;
import org.jspecify.annotations.Nullable;

public enum Difficulty implements StringRepresentable
{
    PEACEFUL(0, "peaceful"),
    EASY(1, "easy"),
    NORMAL(2, "normal"),
    HARD(3, "hard");

    public static final StringRepresentable.EnumCodec<Difficulty> CODEC;
    private static final IntFunction<Difficulty> BY_ID;
    public static final StreamCodec<ByteBuf, Difficulty> STREAM_CODEC;
    private final int id;
    private final String key;

    private Difficulty(int id, String key) {
        this.id = id;
        this.key = key;
    }

    public int getId() {
        return this.id;
    }

    public Component getDisplayName() {
        return Component.translatable("options.difficulty." + this.key);
    }

    public Component getInfo() {
        return Component.translatable("options.difficulty." + this.key + ".info");
    }

    @Deprecated
    public static Difficulty byId(int id) {
        return BY_ID.apply(id);
    }

    public static @Nullable Difficulty byName(String name) {
        return CODEC.byName(name);
    }

    @Override
    public String getSerializedName() {
        return this.key;
    }

    static {
        CODEC = StringRepresentable.fromEnum(Difficulty::values);
        BY_ID = ByIdMap.continuous(Difficulty::getId, Difficulty.values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Difficulty::getId);
    }
}

