/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.vehicle.boat;

import java.util.function.Supplier;
import net.mayaan.world.entity.EntityDimensions;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.vehicle.boat.AbstractBoat;
import net.mayaan.world.item.Item;
import net.mayaan.world.level.Level;

public class Raft
extends AbstractBoat {
    public Raft(EntityType<? extends Raft> type, Level level, Supplier<Item> dropItem) {
        super(type, level, dropItem);
    }

    @Override
    protected double rideHeight(EntityDimensions dimensions) {
        return dimensions.height() * 0.8888889f;
    }
}

