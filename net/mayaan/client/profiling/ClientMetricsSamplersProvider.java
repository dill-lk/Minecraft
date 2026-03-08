/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 */
package net.mayaan.client.profiling;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.LevelRenderer;
import net.mayaan.client.renderer.chunk.SectionRenderDispatcher;
import net.mayaan.util.profiling.ProfileCollector;
import net.mayaan.util.profiling.metrics.MetricCategory;
import net.mayaan.util.profiling.metrics.MetricSampler;
import net.mayaan.util.profiling.metrics.MetricsSamplerProvider;
import net.mayaan.util.profiling.metrics.profiling.ProfilerSamplerAdapter;
import net.mayaan.util.profiling.metrics.profiling.ServerMetricsSamplersProvider;

public class ClientMetricsSamplersProvider
implements MetricsSamplerProvider {
    private final LevelRenderer levelRenderer;
    private final Set<MetricSampler> samplers = new ObjectOpenHashSet();
    private final ProfilerSamplerAdapter samplerFactory = new ProfilerSamplerAdapter();

    public ClientMetricsSamplersProvider(LongSupplier wallTimeSource, LevelRenderer levelRenderer) {
        this.levelRenderer = levelRenderer;
        this.samplers.add(ServerMetricsSamplersProvider.tickTimeSampler(wallTimeSource));
        this.registerStaticSamplers();
    }

    private void registerStaticSamplers() {
        this.samplers.addAll(ServerMetricsSamplersProvider.runtimeIndependentSamplers());
        this.samplers.add(MetricSampler.create("totalChunks", MetricCategory.CHUNK_RENDERING, this.levelRenderer, LevelRenderer::getTotalSections));
        this.samplers.add(MetricSampler.create("renderedChunks", MetricCategory.CHUNK_RENDERING, this.levelRenderer, LevelRenderer::countRenderedSections));
        this.samplers.add(MetricSampler.create("lastViewDistance", MetricCategory.CHUNK_RENDERING, this.levelRenderer, LevelRenderer::getLastViewDistance));
        SectionRenderDispatcher sectionRenderDispatcher = this.levelRenderer.getSectionRenderDispatcher();
        if (sectionRenderDispatcher != null) {
            this.samplers.add(MetricSampler.create("freeBufferCount", MetricCategory.CHUNK_RENDERING_DISPATCHING, sectionRenderDispatcher, SectionRenderDispatcher::getFreeBufferCount));
            this.samplers.add(MetricSampler.create("compileQueueSize", MetricCategory.CHUNK_RENDERING_DISPATCHING, sectionRenderDispatcher, SectionRenderDispatcher::getCompileQueueSize));
        }
        this.samplers.add(MetricSampler.create("gpuUtilization", MetricCategory.GPU, Mayaan.getInstance(), Mayaan::getGpuUtilization));
    }

    @Override
    public Set<MetricSampler> samplers(Supplier<ProfileCollector> singleTickProfiler) {
        this.samplers.addAll(this.samplerFactory.newSamplersFoundInProfiler(singleTickProfiler));
        return this.samplers;
    }
}

