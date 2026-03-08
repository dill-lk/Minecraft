/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core.dispenser;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.dispenser.BlockSource;
import net.mayaan.core.dispenser.DefaultDispenseItemBehavior;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.BlockTags;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecart;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.BaseRailBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.DispenserBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.RailShape;
import net.mayaan.world.phys.Vec3;

public class MinecartDispenseItemBehavior
extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
    private final EntityType<? extends AbstractMinecart> entityType;

    public MinecartDispenseItemBehavior(EntityType<? extends AbstractMinecart> entityType) {
        this.entityType = entityType;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public ItemStack execute(BlockSource source, ItemStack dispensed) {
        double yOffset;
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        ServerLevel level = source.level();
        Vec3 center = source.center();
        double spawnX = center.x() + (double)direction.getStepX() * 1.125;
        double spawnY = Math.floor(center.y()) + (double)direction.getStepY();
        double spawnZ = center.z() + (double)direction.getStepZ() * 1.125;
        BlockPos front = source.pos().relative(direction);
        BlockState blockFront = level.getBlockState(front);
        if (blockFront.is(BlockTags.RAILS)) {
            yOffset = MinecartDispenseItemBehavior.getRailShape(blockFront).isSlope() ? 0.6 : 0.1;
        } else {
            if (!blockFront.isAir()) return this.defaultDispenseItemBehavior.dispense(source, dispensed);
            BlockState blockBelow = level.getBlockState(front.below());
            if (!blockBelow.is(BlockTags.RAILS)) return this.defaultDispenseItemBehavior.dispense(source, dispensed);
            yOffset = direction == Direction.DOWN || !MinecartDispenseItemBehavior.getRailShape(blockBelow).isSlope() ? -0.9 : -0.4;
        }
        Vec3 spawnPos = new Vec3(spawnX, spawnY + yOffset, spawnZ);
        AbstractMinecart minecart = AbstractMinecart.createMinecart(level, spawnPos.x, spawnPos.y, spawnPos.z, this.entityType, EntitySpawnReason.DISPENSER, dispensed, null);
        if (minecart == null) return dispensed;
        level.addFreshEntity(minecart);
        dispensed.shrink(1);
        return dispensed;
    }

    private static RailShape getRailShape(BlockState blockFront) {
        RailShape railShape;
        Block block = blockFront.getBlock();
        if (block instanceof BaseRailBlock) {
            BaseRailBlock railBlock = (BaseRailBlock)block;
            railShape = blockFront.getValue(railBlock.getShapeProperty());
        } else {
            railShape = RailShape.NORTH_SOUTH;
        }
        return railShape;
    }

    @Override
    protected void playSound(BlockSource source) {
        source.level().levelEvent(1000, source.pos(), 0);
    }
}

