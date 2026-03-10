/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EndCrystalRenderState
extends EntityRenderState {
    public boolean showsBottom = true;
    public @Nullable Vec3 beamOffset;
}

