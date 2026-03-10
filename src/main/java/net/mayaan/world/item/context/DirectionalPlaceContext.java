/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.context;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.Vec3;

public class DirectionalPlaceContext
extends BlockPlaceContext {
    private final Direction direction;

    public DirectionalPlaceContext(Level level, BlockPos pos, Direction direction, ItemStack dispensed, Direction clickedFace) {
        super(level, null, InteractionHand.MAIN_HAND, dispensed, new BlockHitResult(Vec3.atBottomCenterOf(pos), clickedFace, pos, false));
        this.direction = direction;
    }

    @Override
    public BlockPos getClickedPos() {
        return this.getHitResult().getBlockPos();
    }

    @Override
    public boolean canPlace() {
        return this.getLevel().getBlockState(this.getHitResult().getBlockPos()).canBeReplaced(this);
    }

    @Override
    public boolean replacingClickedOnBlock() {
        return this.canPlace();
    }

    @Override
    public Direction getNearestLookingDirection() {
        return Direction.DOWN;
    }

    @Override
    public Direction[] getNearestLookingDirections() {
        switch (this.direction) {
            default: {
                return new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP};
            }
            case UP: {
                return new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
            }
            case NORTH: {
                return new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.SOUTH};
            }
            case SOUTH: {
                return new Direction[]{Direction.DOWN, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.NORTH};
            }
            case WEST: {
                return new Direction[]{Direction.DOWN, Direction.WEST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.EAST};
            }
            case EAST: 
        }
        return new Direction[]{Direction.DOWN, Direction.EAST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.WEST};
    }

    @Override
    public Direction getHorizontalDirection() {
        return this.direction.getAxis() == Direction.Axis.Y ? Direction.NORTH : this.direction;
    }

    @Override
    public boolean isSecondaryUseActive() {
        return false;
    }

    @Override
    public float getRotation() {
        return this.direction.get2DDataValue() * 90;
    }
}

