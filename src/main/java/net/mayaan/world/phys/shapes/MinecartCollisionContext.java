/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.phys.shapes;

import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecart;
import net.mayaan.world.level.CollisionGetter;
import net.mayaan.world.level.block.BaseRailBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.RailShape;
import net.mayaan.world.phys.shapes.EntityCollisionContext;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class MinecartCollisionContext
extends EntityCollisionContext {
    private @Nullable BlockPos ingoreBelow;
    private @Nullable BlockPos slopeIgnore;

    protected MinecartCollisionContext(AbstractMinecart entity, boolean alwaysStandOnFluid) {
        super(entity, alwaysStandOnFluid, false);
        this.setupContext(entity);
    }

    private void setupContext(AbstractMinecart entity) {
        BlockPos currentRailPos = entity.getCurrentBlockPosOrRailBelow();
        BlockState currentState = entity.level().getBlockState(currentRailPos);
        boolean onRails = BaseRailBlock.isRail(currentState);
        if (onRails) {
            this.ingoreBelow = currentRailPos.below();
            RailShape shape = currentState.getValue(((BaseRailBlock)currentState.getBlock()).getShapeProperty());
            if (shape.isSlope()) {
                this.slopeIgnore = switch (shape) {
                    case RailShape.ASCENDING_EAST -> currentRailPos.east();
                    case RailShape.ASCENDING_WEST -> currentRailPos.west();
                    case RailShape.ASCENDING_NORTH -> currentRailPos.north();
                    case RailShape.ASCENDING_SOUTH -> currentRailPos.south();
                    default -> null;
                };
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, CollisionGetter collisionGetter, BlockPos pos) {
        if (pos.equals(this.ingoreBelow) || pos.equals(this.slopeIgnore)) {
            return Shapes.empty();
        }
        return super.getCollisionShape(state, collisionGetter, pos);
    }
}

