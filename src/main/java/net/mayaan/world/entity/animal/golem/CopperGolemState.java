/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.world.entity.animal.golem;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.util.ByIdMap;
import net.mayaan.util.StringRepresentable;

public enum CopperGolemState implements StringRepresentable
{
    IDLE("idle", 0),
    GETTING_ITEM("getting_item", 1),
    GETTING_NO_ITEM("getting_no_item", 2),
    DROPPING_ITEM("dropping_item", 3),
    DROPPING_NO_ITEM("dropping_no_item", 4);

    public static final Codec<CopperGolemState> CODEC;
    private static final IntFunction<CopperGolemState> BY_ID;
    public static final StreamCodec<ByteBuf, CopperGolemState> STREAM_CODEC;
    private final String name;
    private final int id;

    private CopperGolemState(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    private int id() {
        return this.id;
    }

    static {
        CODEC = StringRepresentable.fromEnum(CopperGolemState::values);
        BY_ID = ByIdMap.continuous(CopperGolemState::id, CopperGolemState.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CopperGolemState::id);
    }
}

