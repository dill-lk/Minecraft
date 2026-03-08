/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block.dispatch;

import java.util.List;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;

public class WeightedVariants
implements BlockStateModel {
    private final WeightedList<BlockStateModel> list;
    private final Material.Baked particleMaterial;
    private final boolean hasTranslucency;

    public WeightedVariants(WeightedList<BlockStateModel> list) {
        this.list = list;
        BlockStateModel firstModel = (BlockStateModel)((Weighted)list.unwrap().getFirst()).value();
        this.particleMaterial = firstModel.particleMaterial();
        this.hasTranslucency = WeightedVariants.hasTranslucency(list);
    }

    private static boolean hasTranslucency(WeightedList<BlockStateModel> list) {
        for (Weighted<BlockStateModel> entry : list.unwrap()) {
            if (!entry.value().hasTranslucency()) continue;
            return true;
        }
        return false;
    }

    @Override
    public Material.Baked particleMaterial() {
        return this.particleMaterial;
    }

    @Override
    public boolean hasTranslucency() {
        return this.hasTranslucency;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
        this.list.getRandomOrThrow(random).collectParts(random, output);
    }

    public record Unbaked(WeightedList<BlockStateModel.Unbaked> entries) implements BlockStateModel.Unbaked
    {
        @Override
        public BlockStateModel bake(ModelBaker modelBakery) {
            return new WeightedVariants(this.entries.map(m -> m.bake(modelBakery)));
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            this.entries.unwrap().forEach(v -> ((BlockStateModel.Unbaked)v.value()).resolveDependencies(resolver));
        }
    }
}

