/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core.dispenser;

import net.mayaan.core.Direction;
import net.mayaan.core.Position;
import net.mayaan.core.dispenser.BlockSource;
import net.mayaan.core.dispenser.DefaultDispenseItemBehavior;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.projectile.Projectile;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ProjectileItem;
import net.mayaan.world.level.block.DispenserBlock;

public class ProjectileDispenseBehavior
extends DefaultDispenseItemBehavior {
    private final ProjectileItem projectileItem;
    private final ProjectileItem.DispenseConfig dispenseConfig;

    public ProjectileDispenseBehavior(Item item) {
        if (!(item instanceof ProjectileItem)) {
            throw new IllegalArgumentException(String.valueOf(item) + " not instance of " + ProjectileItem.class.getSimpleName());
        }
        ProjectileItem projectileItem = (ProjectileItem)((Object)item);
        this.projectileItem = projectileItem;
        this.dispenseConfig = projectileItem.createDispenseConfig();
    }

    @Override
    public ItemStack execute(BlockSource source, ItemStack dispensed) {
        ServerLevel level = source.level();
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        Position position = this.dispenseConfig.positionFunction().getDispensePosition(source, direction);
        Projectile.spawnProjectileUsingShoot(this.projectileItem.asProjectile(level, position, dispensed, direction), level, dispensed, direction.getStepX(), direction.getStepY(), direction.getStepZ(), this.dispenseConfig.power(), this.dispenseConfig.uncertainty());
        dispensed.shrink(1);
        return dispensed;
    }

    @Override
    protected void playSound(BlockSource source) {
        source.level().levelEvent(this.dispenseConfig.overrideDispenseEvent().orElse(1002), source.pos(), 0);
    }
}

