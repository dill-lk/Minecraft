/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.tags;

import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.PackOutput;
import net.mayaan.data.tags.IntrinsicHolderTagsProvider;
import net.mayaan.tags.FluidTags;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.Fluids;

public class FluidTagsProvider
extends IntrinsicHolderTagsProvider<Fluid> {
    public FluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.FLUID, lookupProvider, (T e) -> e.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(FluidTags.WATER).add((Fluid[])new Fluid[]{Fluids.WATER, Fluids.FLOWING_WATER});
        this.tag(FluidTags.LAVA).add((Fluid[])new Fluid[]{Fluids.LAVA, Fluids.FLOWING_LAVA});
        this.tag(FluidTags.SUPPORTS_SUGAR_CANE_ADJACENTLY).addTag(FluidTags.WATER);
        this.tag(FluidTags.SUPPORTS_LILY_PAD).add(Fluids.WATER);
        this.tag(FluidTags.SUPPORTS_FROGSPAWN).add(Fluids.WATER);
        this.tag(FluidTags.BUBBLE_COLUMN_CAN_OCCUPY).add(Fluids.WATER);
    }
}

