/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.profiling.metrics;

import java.util.Set;
import java.util.function.Supplier;
import net.mayaan.util.profiling.ProfileCollector;
import net.mayaan.util.profiling.metrics.MetricSampler;

public interface MetricsSamplerProvider {
    public Set<MetricSampler> samplers(Supplier<ProfileCollector> var1);
}

