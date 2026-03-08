/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.util.profiling;

import java.util.Set;
import net.mayaan.util.profiling.ActiveProfiler;
import net.mayaan.util.profiling.ProfileResults;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.util.profiling.metrics.MetricCategory;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;

public interface ProfileCollector
extends ProfilerFiller {
    public ProfileResults getResults();

    public @Nullable ActiveProfiler.PathEntry getEntry(String var1);

    public Set<Pair<String, MetricCategory>> getChartedPaths();
}

