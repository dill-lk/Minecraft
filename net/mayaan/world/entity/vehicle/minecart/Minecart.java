/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.vehicle.minecart;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.Mth;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecart;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.phys.Vec3;

public class Minecart
extends AbstractMinecart {
    private float rotationOffset;
    private float playerRotationOffset;

    public Minecart(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (!player.isSecondaryUseActive() && !this.isVehicle() && (this.level().isClientSide() || player.startRiding(this))) {
            this.playerRotationOffset = this.rotationOffset;
            if (!this.level().isClientSide()) {
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected Item getDropItem() {
        return Items.MINECART;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.MINECART);
    }

    @Override
    public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
        if (state) {
            if (this.isVehicle()) {
                this.ejectPassengers();
            }
            if (this.getHurtTime() == 0) {
                this.setHurtDir(-this.getHurtDir());
                this.setHurtTime(10);
                this.setDamage(50.0f);
                this.markHurt();
            }
        }
    }

    @Override
    public boolean isRideable() {
        return true;
    }

    @Override
    public void tick() {
        double lastKnownYRot = this.getYRot();
        Vec3 lastKnownPos = this.position();
        super.tick();
        double tickDiff = ((double)this.getYRot() - lastKnownYRot) % 360.0;
        if (this.level().isClientSide() && lastKnownPos.distanceTo(this.position()) > 0.01) {
            this.rotationOffset += (float)tickDiff;
            this.rotationOffset %= 360.0f;
        }
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        Player player;
        super.positionRider(passenger, moveFunction);
        if (this.level().isClientSide() && passenger instanceof Player && (player = (Player)passenger).shouldRotateWithMinecart() && Minecart.useExperimentalMovement(this.level())) {
            float yRot = (float)Mth.rotLerp(0.5, (double)this.playerRotationOffset, (double)this.rotationOffset);
            player.setYRot(player.getYRot() - (yRot - this.playerRotationOffset));
            this.playerRotationOffset = yRot;
        }
    }
}

