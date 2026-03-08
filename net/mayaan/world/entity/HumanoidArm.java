/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  io.netty.buffer.ByteBuf
 *  java.lang.MatchException
 */
package net.mayaan.world.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.util.ByIdMap;
import net.mayaan.util.StringRepresentable;

public enum HumanoidArm implements StringRepresentable
{
    LEFT(0, "left", "options.mainHand.left"),
    RIGHT(1, "right", "options.mainHand.right");

    public static final Codec<HumanoidArm> CODEC;
    private static final IntFunction<HumanoidArm> BY_ID;
    public static final StreamCodec<ByteBuf, HumanoidArm> STREAM_CODEC;
    private final int id;
    private final String name;
    private final Component caption;

    private HumanoidArm(int id, String name, String translationKey) {
        this.id = id;
        this.name = name;
        this.caption = Component.translatable(translationKey);
    }

    public HumanoidArm getOpposite() {
        return switch (this.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> RIGHT;
            case 1 -> LEFT;
        };
    }

    public Component caption() {
        return this.caption;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    static {
        CODEC = StringRepresentable.fromEnum(HumanoidArm::values);
        BY_ID = ByIdMap.continuous(a -> a.id, HumanoidArm.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, a -> a.id);
    }
}

