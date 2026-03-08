/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.monster;

import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.monster.Monster;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;

public class Giant
extends Monster {
    public Giant(EntityType<? extends Giant> type, Level level) {
        super((EntityType<? extends Monster>)type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.ATTACK_DAMAGE, 50.0).add(Attributes.CAMERA_DISTANCE, 16.0);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getPathfindingCostFromLightLevels(pos);
    }
}

