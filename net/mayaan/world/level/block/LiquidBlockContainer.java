/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

public interface LiquidBlockContainer {
    public boolean canPlaceLiquid(@Nullable LivingEntity var1, BlockGetter var2, BlockPos var3, BlockState var4, Fluid var5);

    public boolean placeLiquid(LevelAccessor var1, BlockPos var2, BlockState var3, FluidState var4);
}

