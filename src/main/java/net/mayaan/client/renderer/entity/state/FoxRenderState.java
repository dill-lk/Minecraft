/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.HoldingEntityRenderState;
import net.mayaan.world.entity.animal.fox.Fox;

public class FoxRenderState
extends HoldingEntityRenderState {
    public float headRollAngle;
    public float crouchAmount;
    public boolean isCrouching;
    public boolean isSleeping;
    public boolean isSitting;
    public boolean isFaceplanted;
    public boolean isPouncing;
    public Fox.Variant variant = Fox.Variant.DEFAULT;
}

