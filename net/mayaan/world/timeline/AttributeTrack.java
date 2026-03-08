/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.timeline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.util.KeyframeTrack;
import net.mayaan.util.Util;
import net.mayaan.world.attribute.EnvironmentAttribute;
import net.mayaan.world.attribute.modifier.AttributeModifier;
import net.mayaan.world.clock.ClockManager;
import net.mayaan.world.clock.WorldClock;
import net.mayaan.world.timeline.AttributeTrackSampler;

public record AttributeTrack<Value, Argument>(AttributeModifier<Value, Argument> modifier, KeyframeTrack<Argument> argumentTrack) {
    public static <Value> Codec<AttributeTrack<Value, ?>> createCodec(EnvironmentAttribute<Value> attribute) {
        MapCodec modifierCodec = attribute.type().modifierCodec().optionalFieldOf("modifier", AttributeModifier.override());
        return modifierCodec.dispatch(AttributeTrack::modifier, Util.memoize(modifier -> AttributeTrack.createCodecWithModifier(attribute, modifier)));
    }

    private static <Value, Argument> MapCodec<AttributeTrack<Value, Argument>> createCodecWithModifier(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier) {
        return KeyframeTrack.mapCodec(modifier.argumentCodec(attribute)).xmap(track -> new AttributeTrack(modifier, track), AttributeTrack::argumentTrack);
    }

    public AttributeTrackSampler<Value, Argument> bakeSampler(EnvironmentAttribute<Value> attribute, Holder<WorldClock> clock, Optional<Integer> periodTicks, ClockManager clockManager) {
        return new AttributeTrackSampler<Value, Argument>(clock, periodTicks, this.modifier, this.argumentTrack, this.modifier.argumentKeyframeLerp(attribute), clockManager);
    }

    public static DataResult<AttributeTrack<?, ?>> validatePeriod(AttributeTrack<?, ?> track, int periodTicks) {
        return KeyframeTrack.validatePeriod(track.argumentTrack(), periodTicks).map(ignored -> track);
    }
}

