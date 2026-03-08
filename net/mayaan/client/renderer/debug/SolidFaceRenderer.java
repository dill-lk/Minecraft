/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;
import net.mayaan.world.phys.shapes.VoxelShape;

public class SolidFaceRenderer
implements DebugRenderer.SimpleDebugRenderer {
    private final Mayaan minecraft;

    public SolidFaceRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        Level level = this.minecraft.player.level();
        BlockPos playerPos = BlockPos.containing(camX, camY, camZ);
        for (BlockPos blockPos : BlockPos.betweenClosed(playerPos.offset(-6, -6, -6), playerPos.offset(6, 6, 6))) {
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(Blocks.AIR)) continue;
            VoxelShape shape = blockState.getShape(level, blockPos);
            for (AABB outlineBox : shape.toAabbs()) {
                AABB aabb = outlineBox.move(blockPos).inflate(0.002);
                int color = -2130771968;
                Vec3 min = aabb.getMinPosition();
                Vec3 max = aabb.getMaxPosition();
                SolidFaceRenderer.addFaceIfSturdy(blockPos, blockState, level, Direction.WEST, min, max, -2130771968);
                SolidFaceRenderer.addFaceIfSturdy(blockPos, blockState, level, Direction.SOUTH, min, max, -2130771968);
                SolidFaceRenderer.addFaceIfSturdy(blockPos, blockState, level, Direction.EAST, min, max, -2130771968);
                SolidFaceRenderer.addFaceIfSturdy(blockPos, blockState, level, Direction.NORTH, min, max, -2130771968);
                SolidFaceRenderer.addFaceIfSturdy(blockPos, blockState, level, Direction.DOWN, min, max, -2130771968);
                SolidFaceRenderer.addFaceIfSturdy(blockPos, blockState, level, Direction.UP, min, max, -2130771968);
            }
        }
    }

    private static void addFaceIfSturdy(BlockPos blockPos, BlockState blockState, BlockGetter level, Direction direction, Vec3 cornerA, Vec3 cornerB, int color) {
        if (blockState.isFaceSturdy(level, blockPos, direction)) {
            Gizmos.rect(cornerA, cornerB, direction, GizmoStyle.fill(color));
        }
    }
}

