/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.timers;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.function.Function;
import net.mayaan.resources.Identifier;
import net.mayaan.server.MayaanServer;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.world.level.timers.FunctionCallback;
import net.mayaan.world.level.timers.FunctionTagCallback;
import net.mayaan.world.level.timers.TimerCallback;

public class TimerCallbacks<C> {
    public static final TimerCallbacks<MayaanServer> SERVER_CALLBACKS = new TimerCallbacks().register(Identifier.withDefaultNamespace("function"), FunctionCallback.CODEC).register(Identifier.withDefaultNamespace("function_tag"), FunctionTagCallback.CODEC);
    private final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends TimerCallback<C>>> idMapper = new ExtraCodecs.LateBoundIdMapper();
    private final Codec<TimerCallback<C>> codec = this.idMapper.codec(Identifier.CODEC).dispatch("type", TimerCallback::codec, Function.identity());

    @VisibleForTesting
    public TimerCallbacks() {
    }

    public TimerCallbacks<C> register(Identifier id, MapCodec<? extends TimerCallback<C>> codec) {
        this.idMapper.put(id, codec);
        return this;
    }

    public Codec<TimerCallback<C>> codec() {
        return this.codec;
    }
}

