/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.material;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.phys.Vec3;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;

public class EmptyFluid
extends Fluid {
    @Override
    public Item getBucket() {
        return Items.AIR;
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
        return true;
    }

    @Override
    public Vec3 getFlow(BlockGetter level, BlockPos pos, FluidState fluidState) {
        return Vec3.ZERO;
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 0;
    }

    @Override
    protected boolean isEmpty() {
        return true;
    }

    @Override
    protected float getExplosionResistance() {
        return 0.0f;
    }

    @Override
    public float getHeight(FluidState fluidState, BlockGetter level, BlockPos pos) {
        return 0.0f;
    }

    @Override
    public float getOwnHeight(FluidState fluidState) {
        return 0.0f;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState fluidState) {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return false;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return 0;
    }

    @Override
    public VoxelShape getShape(FluidState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }
}

