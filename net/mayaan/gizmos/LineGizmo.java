/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gizmos;

import net.mayaan.gizmos.Gizmo;
import net.mayaan.gizmos.GizmoPrimitives;
import net.mayaan.util.ARGB;
import net.mayaan.world.phys.Vec3;

public record LineGizmo(Vec3 start, Vec3 end, int color, float width) implements Gizmo
{
    public static final float DEFAULT_WIDTH = 3.0f;

    @Override
    public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
        primitives.addLine(this.start, this.end, ARGB.multiplyAlpha(this.color, alphaMultiplier), this.width);
    }
}

