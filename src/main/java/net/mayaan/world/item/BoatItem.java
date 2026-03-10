/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item;

import java.util.List;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySelector;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.vehicle.boat.AbstractBoat;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.ClipContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class BoatItem
extends Item {
    private final EntityType<? extends AbstractBoat> entityType;

    public BoatItem(EntityType<? extends AbstractBoat> entityType, Item.Properties properties) {
        super(properties);
        this.entityType = entityType;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult hitResult = BoatItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (((HitResult)hitResult).getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        }
        Vec3 viewVector = player.getViewVector(1.0f);
        double range = 5.0;
        List<Entity> entities = level.getEntities(player, player.getBoundingBox().expandTowards(viewVector.scale(5.0)).inflate(1.0), EntitySelector.CAN_BE_PICKED);
        if (!entities.isEmpty()) {
            Vec3 from = player.getEyePosition();
            for (Entity entity : entities) {
                AABB bb = entity.getBoundingBox().inflate(entity.getPickRadius());
                if (!bb.contains(from)) continue;
                return InteractionResult.PASS;
            }
        }
        if (((HitResult)hitResult).getType() == HitResult.Type.BLOCK) {
            AbstractBoat boat = this.getBoat(level, hitResult, itemStack, player);
            if (boat == null) {
                return InteractionResult.FAIL;
            }
            boat.setYRot(player.getYRot());
            if (!level.noCollision(boat, boat.getBoundingBox())) {
                return InteractionResult.FAIL;
            }
            if (!level.isClientSide()) {
                level.addFreshEntity(boat);
                level.gameEvent((Entity)player, GameEvent.ENTITY_PLACE, hitResult.getLocation());
                itemStack.consume(1, player);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private @Nullable AbstractBoat getBoat(Level level, HitResult hitResult, ItemStack itemStack, Player player) {
        AbstractBoat boat = this.entityType.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
        if (boat != null) {
            Vec3 location = hitResult.getLocation();
            boat.setInitialPos(location.x, location.y, location.z);
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                EntityType.createDefaultStackConfig(serverLevel, itemStack, player).accept(boat);
            }
        }
        return boat;
    }
}

