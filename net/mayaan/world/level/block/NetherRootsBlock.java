/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.block;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.BlockPos;
import net.mayaan.core.registries.Registries;
import net.mayaan.tags.TagKey;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.VegetationBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;

public class NetherRootsBlock
extends VegetationBlock {
    public static final MapCodec<NetherRootsBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TagKey.codec(Registries.BLOCK).fieldOf("support_blocks").forGetter(b -> b.supportBlocks), NetherRootsBlock.propertiesCodec()).apply((Applicative)i, NetherRootsBlock::new));
    private static final VoxelShape SHAPE = Block.column(12.0, 0.0, 13.0);
    private final TagKey<Block> supportBlocks;

    public MapCodec<NetherRootsBlock> codec() {
        return CODEC;
    }

    protected NetherRootsBlock(TagKey<Block> supportBlocks, BlockBehaviour.Properties properties) {
        super(properties);
        this.supportBlocks = supportBlocks;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(this.supportBlocks);
    }
}

