/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.core.Direction;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.item.component.ResolvableProfile;
import org.jspecify.annotations.Nullable;

public class LivingEntityRenderState
extends EntityRenderState {
    public float bodyRot;
    public float yRot;
    public float xRot;
    public float deathTime;
    public float walkAnimationPos;
    public float walkAnimationSpeed;
    public float scale = 1.0f;
    public float ageScale = 1.0f;
    public float ticksSinceKineticHitFeedback;
    public boolean isUpsideDown;
    public boolean isFullyFrozen;
    public boolean isBaby;
    public boolean isInWater;
    public boolean isAutoSpinAttack;
    public boolean hasRedOverlay;
    public boolean isInvisibleToPlayer;
    public @Nullable Direction bedOrientation;
    public Pose pose = Pose.STANDING;
    public final ItemStackRenderState headItem = new ItemStackRenderState();
    public float wornHeadAnimationPos;
    public  @Nullable SkullBlock.Type wornHeadType;
    public @Nullable ResolvableProfile wornHeadProfile;

    public boolean hasPose(Pose pose) {
        return this.pose == pose;
    }
}

