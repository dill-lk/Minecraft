/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.levelgen.material;

import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.DensityFunction;
import net.mayaan.world.level.levelgen.NoiseChunk;
import org.jspecify.annotations.Nullable;

public record MaterialRuleList(NoiseChunk.BlockStateFiller[] materialRuleList) implements NoiseChunk.BlockStateFiller
{
    @Override
    public @Nullable BlockState calculate(DensityFunction.FunctionContext context) {
        for (NoiseChunk.BlockStateFiller rule : this.materialRuleList) {
            BlockState state = rule.calculate(context);
            if (state == null) continue;
            return state;
        }
        return null;
    }
}

