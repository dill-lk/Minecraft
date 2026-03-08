/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.util.debug;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;

public record DebugGoalInfo(List<DebugGoal> goals) {
    public static final StreamCodec<ByteBuf, DebugGoalInfo> STREAM_CODEC = StreamCodec.composite(DebugGoal.STREAM_CODEC.apply(ByteBufCodecs.list()), DebugGoalInfo::goals, DebugGoalInfo::new);

    public record DebugGoal(int priority, boolean isRunning, String name) {
        public static final StreamCodec<ByteBuf, DebugGoal> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, DebugGoal::priority, ByteBufCodecs.BOOL, DebugGoal::isRunning, ByteBufCodecs.stringUtf8(255), DebugGoal::name, DebugGoal::new);
    }
}

