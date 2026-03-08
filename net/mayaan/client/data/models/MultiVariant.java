/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.data.models;

import java.util.List;
import net.mayaan.client.renderer.block.dispatch.BlockStateModel;
import net.mayaan.client.renderer.block.dispatch.SingleVariant;
import net.mayaan.client.renderer.block.dispatch.Variant;
import net.mayaan.client.renderer.block.dispatch.VariantMutator;
import net.mayaan.client.renderer.block.dispatch.WeightedVariants;
import net.mayaan.util.random.Weighted;
import net.mayaan.util.random.WeightedList;

public record MultiVariant(WeightedList<Variant> variants) {
    public MultiVariant {
        if (variants.isEmpty()) {
            throw new IllegalArgumentException("Variant list must contain at least one element");
        }
    }

    public MultiVariant with(VariantMutator mutator) {
        return new MultiVariant(this.variants.map(mutator));
    }

    public BlockStateModel.Unbaked toUnbaked() {
        List<Weighted<Variant>> entries = this.variants.unwrap();
        return entries.size() == 1 ? new SingleVariant.Unbaked((Variant)((Weighted)entries.getFirst()).value()) : new WeightedVariants.Unbaked(this.variants.map(SingleVariant.Unbaked::new));
    }
}

