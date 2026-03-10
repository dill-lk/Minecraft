/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.timeline;

import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.util.KeyframeTrack;
import net.mayaan.util.KeyframeTrackSampler;
import net.mayaan.world.attribute.EnvironmentAttributeLayer;
import net.mayaan.world.attribute.LerpFunction;
import net.mayaan.world.attribute.modifier.AttributeModifier;
import net.mayaan.world.clock.ClockManager;
import net.mayaan.world.clock.WorldClock;
import org.jspecify.annotations.Nullable;

public class AttributeTrackSampler<Value, Argument>
implements EnvironmentAttributeLayer.TimeBased<Value> {
    private final Holder<WorldClock> clock;
    private final AttributeModifier<Value, Argument> modifier;
    private final KeyframeTrackSampler<Argument> argumentSampler;
    private final ClockManager clockManager;
    private int cachedTickId;
    private @Nullable Argument cachedArgument;

    public AttributeTrackSampler(Holder<WorldClock> clock, Optional<Integer> periodTicks, AttributeModifier<Value, Argument> modifier, KeyframeTrack<Argument> argumentTrack, LerpFunction<Argument> argumentLerp, ClockManager clockManager) {
        this.clock = clock;
        this.modifier = modifier;
        this.clockManager = clockManager;
        this.argumentSampler = argumentTrack.bakeSampler(periodTicks, argumentLerp);
    }

    @Override
    public Value applyTimeBased(Value baseValue, int cacheTickId) {
        if (this.cachedArgument == null || cacheTickId != this.cachedTickId) {
            this.cachedTickId = cacheTickId;
            this.cachedArgument = this.argumentSampler.sample(this.clockManager.getTotalTicks(this.clock));
        }
        return this.modifier.apply(baseValue, this.cachedArgument);
    }
}

