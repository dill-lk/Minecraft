/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core.dispenser;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.dispenser.BlockSource;
import net.mayaan.core.dispenser.DefaultDispenseItemBehavior;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.FluidTags;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.vehicle.boat.AbstractBoat;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.DispenserBlock;
import net.mayaan.world.phys.Vec3;

public class BoatDispenseItemBehavior
extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
    private final EntityType<? extends AbstractBoat> type;

    public BoatDispenseItemBehavior(EntityType<? extends AbstractBoat> type) {
        this.type = type;
    }

    @Override
    public ItemStack execute(BlockSource source, ItemStack dispensed) {
        double yOffset;
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        ServerLevel level = source.level();
        Vec3 center = source.center();
        double justOutsideDispenser = 0.5625 + (double)this.type.getWidth() / 2.0;
        double spawnX = center.x() + (double)direction.getStepX() * justOutsideDispenser;
        double spawnY = center.y() + (double)((float)direction.getStepY() * 1.125f);
        double spawnZ = center.z() + (double)direction.getStepZ() * justOutsideDispenser;
        BlockPos frontPos = source.pos().relative(direction);
        if (level.getFluidState(frontPos).is(FluidTags.WATER)) {
            yOffset = 1.0;
        } else if (level.getBlockState(frontPos).isAir() && level.getFluidState(frontPos.below()).is(FluidTags.WATER)) {
            yOffset = 0.0;
        } else {
            return this.defaultDispenseItemBehavior.dispense(source, dispensed);
        }
        AbstractBoat boat = this.type.create(level, EntitySpawnReason.DISPENSER);
        if (boat != null) {
            boat.setInitialPos(spawnX, spawnY + yOffset, spawnZ);
            EntityType.createDefaultStackConfig(level, dispensed, null).accept(boat);
            boat.setYRot(direction.toYRot());
            level.addFreshEntity(boat);
            dispensed.shrink(1);
        }
        return dispensed;
    }

    @Override
    protected void playSound(BlockSource source) {
        source.level().levelEvent(1000, source.pos(), 0);
    }
}

