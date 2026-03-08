/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.monster.illager;

import java.util.Objects;
import net.mayaan.tags.EntityTypeTags;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.goal.OpenDoorGoal;
import net.mayaan.world.entity.npc.villager.AbstractVillager;
import net.mayaan.world.entity.raid.Raider;
import net.mayaan.world.level.Level;

public abstract class AbstractIllager
extends Raider {
    protected AbstractIllager(EntityType<? extends AbstractIllager> type, Level level) {
        super((EntityType<? extends Raider>)type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public IllagerArmPose getArmPose() {
        return IllagerArmPose.CROSSED;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target instanceof AbstractVillager && target.isBaby()) {
            return false;
        }
        return super.canAttack(target);
    }

    @Override
    protected boolean considersEntityAsAlly(Entity other) {
        if (super.considersEntityAsAlly(other)) {
            return true;
        }
        if (other.is(EntityTypeTags.ILLAGER_FRIENDS)) {
            return this.getTeam() == null && other.getTeam() == null;
        }
        return false;
    }

    public static enum IllagerArmPose {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        CELEBRATING,
        NEUTRAL;

    }

    protected class RaiderOpenDoorGoal
    extends OpenDoorGoal {
        final /* synthetic */ AbstractIllager this$0;

        public RaiderOpenDoorGoal(AbstractIllager this$0, Raider raider) {
            AbstractIllager abstractIllager = this$0;
            Objects.requireNonNull(abstractIllager);
            this.this$0 = abstractIllager;
            super(raider, false);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.this$0.hasActiveRaid();
        }
    }
}

