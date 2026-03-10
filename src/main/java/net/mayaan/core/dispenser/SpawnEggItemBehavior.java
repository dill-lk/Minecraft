/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core.dispenser;

import net.mayaan.core.Direction;
import net.mayaan.core.dispenser.BlockSource;
import net.mayaan.core.dispenser.DefaultDispenseItemBehavior;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.SpawnEggItem;
import net.mayaan.world.level.block.DispenserBlock;
import net.mayaan.world.level.gameevent.GameEvent;

public class SpawnEggItemBehavior
extends DefaultDispenseItemBehavior {
    public static final SpawnEggItemBehavior INSTANCE = new SpawnEggItemBehavior();

    @Override
    public ItemStack execute(BlockSource source, ItemStack dispensed) {
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        EntityType<?> type = SpawnEggItem.getType(dispensed);
        if (type == null) {
            return dispensed;
        }
        try {
            type.spawn(source.level(), dispensed, null, source.pos().relative(direction), EntitySpawnReason.DISPENSER, direction != Direction.UP, false);
        }
        catch (Exception e) {
            LOGGER.error("Error while dispensing spawn egg from dispenser at {}", (Object)source.pos(), (Object)e);
            return ItemStack.EMPTY;
        }
        dispensed.shrink(1);
        source.level().gameEvent(null, GameEvent.ENTITY_PLACE, source.pos());
        return dispensed;
    }
}

