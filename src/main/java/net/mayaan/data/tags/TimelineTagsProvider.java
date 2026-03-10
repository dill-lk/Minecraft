/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.tags;

import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.PackOutput;
import net.mayaan.data.tags.KeyTagProvider;
import net.mayaan.tags.TimelineTags;
import net.mayaan.world.timeline.Timeline;
import net.mayaan.world.timeline.Timelines;

public class TimelineTagsProvider
extends KeyTagProvider<Timeline> {
    public TimelineTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.TIMELINE, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(TimelineTags.UNIVERSAL).add(Timelines.VILLAGER_SCHEDULE);
        this.tag(TimelineTags.IN_OVERWORLD).addTag(TimelineTags.UNIVERSAL).add(Timelines.OVERWORLD_DAY, Timelines.MOON, Timelines.EARLY_GAME);
        this.tag(TimelineTags.IN_NETHER).addTag(TimelineTags.UNIVERSAL);
        this.tag(TimelineTags.IN_END).addTag(TimelineTags.UNIVERSAL);
    }
}

