/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.VegetationBlock;
import net.mayaan.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;

public class DryVegetationBlock
extends VegetationBlock {
    public static final MapCodec<DryVegetationBlock> CODEC = DryVegetationBlock.simpleCodec(DryVegetationBlock::new);
    private static final VoxelShape SHAPE = Block.column(12.0, 0.0, 13.0);

    public MapCodec<? extends DryVegetationBlock> codec() {
        return CODEC;
    }

    protected DryVegetationBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.SUPPORTS_DRY_VEGETATION);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        AmbientDesertBlockSoundsPlayer.playAmbientDeadBushSounds(level, pos, random);
    }
}

