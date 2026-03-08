/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.profiling.metrics;

import java.util.List;
import net.mayaan.util.profiling.metrics.MetricSampler;

public interface ProfilerMeasured {
    public List<MetricSampler> profiledMetrics();
}

