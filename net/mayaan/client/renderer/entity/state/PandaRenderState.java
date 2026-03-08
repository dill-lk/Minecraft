/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.HoldingEntityRenderState;
import net.mayaan.world.entity.animal.panda.Panda;

public class PandaRenderState
extends HoldingEntityRenderState {
    public Panda.Gene variant = Panda.Gene.NORMAL;
    public boolean isUnhappy;
    public boolean isSneezing;
    public int sneezeTime;
    public boolean isEating;
    public boolean isScared;
    public boolean isSitting;
    public float sitAmount;
    public float lieOnBackAmount;
    public float rollAmount;
    public float rollTime;
}

