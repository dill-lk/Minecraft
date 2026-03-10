/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import java.util.function.Consumer;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.Mth;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.decoration.ArmorStand;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;

public class ArmorStandItem
extends Item {
    public ArmorStandItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction clickedFace = context.getClickedFace();
        if (clickedFace == Direction.DOWN) {
            return InteractionResult.FAIL;
        }
        Level level = context.getLevel();
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        BlockPos blockPos = placeContext.getClickedPos();
        ItemStack itemStack = context.getItemInHand();
        Vec3 pos = Vec3.atBottomCenterOf(blockPos);
        AABB box = EntityType.ARMOR_STAND.getDimensions().makeBoundingBox(pos.x(), pos.y(), pos.z());
        if (!level.noCollision(null, box) || !level.getEntities(null, box).isEmpty()) {
            return InteractionResult.FAIL;
        }
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            Consumer entityConfig = EntityType.createDefaultStackConfig(serverLevel, itemStack, context.getPlayer());
            ArmorStand entity = EntityType.ARMOR_STAND.create(serverLevel, entityConfig, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);
            if (entity == null) {
                return InteractionResult.FAIL;
            }
            float yRot = (float)Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), yRot, 0.0f);
            serverLevel.addFreshEntityWithPassengers(entity);
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75f, 0.8f);
            entity.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
        }
        itemStack.shrink(1);
        return InteractionResult.SUCCESS;
    }
}

