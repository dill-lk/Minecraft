/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.EntityRenderState;
import org.jspecify.annotations.Nullable;

public abstract class DisplayEntityRenderState
extends EntityRenderState {
    public  @Nullable Display.RenderState renderState;
    public float interpolationProgress;
    public float entityYRot;
    public float entityXRot;
    public float cameraYRot;
    public float cameraXRot;

    public abstract boolean hasSubState();
}

