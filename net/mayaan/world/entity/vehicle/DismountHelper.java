/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.vehicle;

import java.util.function.Function;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.tags.BlockTags;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.CollisionGetter;
import net.mayaan.world.level.block.TrapDoorBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class DismountHelper {
    public static int[][] offsetsForDirection(Direction forward) {
        Direction right = forward.getClockWise();
        Direction left = right.getOpposite();
        Direction back = forward.getOpposite();
        return new int[][]{{right.getStepX(), right.getStepZ()}, {left.getStepX(), left.getStepZ()}, {back.getStepX() + right.getStepX(), back.getStepZ() + right.getStepZ()}, {back.getStepX() + left.getStepX(), back.getStepZ() + left.getStepZ()}, {forward.getStepX() + right.getStepX(), forward.getStepZ() + right.getStepZ()}, {forward.getStepX() + left.getStepX(), forward.getStepZ() + left.getStepZ()}, {back.getStepX(), back.getStepZ()}, {forward.getStepX(), forward.getStepZ()}};
    }

    public static boolean isBlockFloorValid(double blockFloorHeight) {
        return !Double.isInfinite(blockFloorHeight) && blockFloorHeight < 1.0;
    }

    public static boolean canDismountTo(CollisionGetter level, LivingEntity passenger, AABB box) {
        Iterable<VoxelShape> blockCollisions = level.getBlockCollisions(passenger, box);
        for (VoxelShape collision : blockCollisions) {
            if (collision.isEmpty()) continue;
            return false;
        }
        return level.getWorldBorder().isWithinBounds(box);
    }

    public static boolean canDismountTo(CollisionGetter level, Vec3 location, LivingEntity passenger, Pose dismountPose) {
        return DismountHelper.canDismountTo(level, passenger, passenger.getLocalBoundsForPose(dismountPose).move(location));
    }

    public static VoxelShape nonClimbableShape(BlockGetter level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.is(BlockTags.CLIMBABLE) || blockState.getBlock() instanceof TrapDoorBlock && blockState.getValue(TrapDoorBlock.OPEN).booleanValue()) {
            return Shapes.empty();
        }
        return blockState.getCollisionShape(level, pos);
    }

    public static double findCeilingFrom(BlockPos pos, int blocks, Function<BlockPos, VoxelShape> shapeGetter) {
        BlockPos.MutableBlockPos cursor = pos.mutable();
        for (int y = 0; y < blocks; ++y) {
            VoxelShape collisionShape = shapeGetter.apply(cursor);
            if (!collisionShape.isEmpty()) {
                return (double)(pos.getY() + y) + collisionShape.min(Direction.Axis.Y);
            }
            cursor.move(Direction.UP);
        }
        return Double.POSITIVE_INFINITY;
    }

    public static @Nullable Vec3 findSafeDismountLocation(EntityType<?> type, CollisionGetter level, BlockPos blockPos, boolean checkDangerous) {
        if (checkDangerous && type.isBlockDangerous(level.getBlockState(blockPos))) {
            return null;
        }
        double floorHeight = level.getBlockFloorHeight(DismountHelper.nonClimbableShape(level, blockPos), () -> DismountHelper.nonClimbableShape(level, blockPos.below()));
        if (!DismountHelper.isBlockFloorValid(floorHeight)) {
            return null;
        }
        if (checkDangerous && floorHeight <= 0.0 && type.isBlockDangerous(level.getBlockState(blockPos.below()))) {
            return null;
        }
        Vec3 position = Vec3.upFromBottomCenterOf(blockPos, floorHeight);
        AABB aabb = type.getDimensions().makeBoundingBox(position);
        Iterable<VoxelShape> worldCollisions = level.getBlockCollisions(null, aabb);
        for (VoxelShape shape : worldCollisions) {
            if (shape.isEmpty()) continue;
            return null;
        }
        if (type == EntityType.PLAYER && (level.getBlockState(blockPos).is(BlockTags.INVALID_SPAWN_INSIDE) || level.getBlockState(blockPos.above()).is(BlockTags.INVALID_SPAWN_INSIDE))) {
            return null;
        }
        if (!level.getWorldBorder().isWithinBounds(aabb)) {
            return null;
        }
        return position;
    }
}

