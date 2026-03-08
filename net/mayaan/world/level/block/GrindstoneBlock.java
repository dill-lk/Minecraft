/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.maayanlabs.math.OctahedralGroup;
import com.mojang.serialization.MapCodec;
import java.util.Map;
import java.util.function.Function;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.network.chat.Component;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.MenuProvider;
import net.mayaan.world.SimpleMenuProvider;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.inventory.ContainerLevelAccess;
import net.mayaan.world.inventory.GrindstoneMenu;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.AttachFace;
import net.mayaan.world.level.pathfinder.PathComputationType;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;

public class GrindstoneBlock
extends FaceAttachedHorizontalDirectionalBlock {
    public static final MapCodec<GrindstoneBlock> CODEC = GrindstoneBlock.simpleCodec(GrindstoneBlock::new);
    private static final Component CONTAINER_TITLE = Component.translatable("container.grindstone_title");
    private final Function<BlockState, VoxelShape> shapes;

    public MapCodec<GrindstoneBlock> codec() {
        return CODEC;
    }

    protected GrindstoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(FACE, AttachFace.WALL));
        this.shapes = this.makeShapes();
    }

    private Function<BlockState, VoxelShape> makeShapes() {
        VoxelShape leftLegs = Shapes.or(Block.box(2.0, 6.0, 7.0, 4.0, 10.0, 16.0), Block.box(2.0, 5.0, 3.0, 4.0, 11.0, 9.0));
        VoxelShape rightLegs = Shapes.rotate(leftLegs, OctahedralGroup.INVERT_X);
        VoxelShape north = Shapes.or(Block.boxZ(8.0, 2.0, 14.0, 0.0, 12.0), leftLegs, rightLegs);
        Map<AttachFace, Map<Direction, VoxelShape>> attachFace = Shapes.rotateAttachFace(north);
        return this.getShapeForEachState(state -> (VoxelShape)((Map)attachFace.get(state.getValue(FACE))).get(state.getValue(FACING)));
    }

    private VoxelShape getVoxelShape(BlockState state) {
        return this.shapes.apply(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getVoxelShape(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getVoxelShape(state);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.openMenu(state.getMenuProvider(level, pos));
            player.awardStat(Stats.INTERACT_WITH_GRINDSTONE);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((containerId, inventory, player) -> new GrindstoneMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}

