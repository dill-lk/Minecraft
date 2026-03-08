/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.debug;

import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.Vec3;

public record DebugGameEventInfo(Holder<GameEvent> event, Vec3 pos) {
    public static final StreamCodec<RegistryFriendlyByteBuf, DebugGameEventInfo> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.holderRegistry(Registries.GAME_EVENT), DebugGameEventInfo::event, Vec3.STREAM_CODEC, DebugGameEventInfo::pos, DebugGameEventInfo::new);
}

