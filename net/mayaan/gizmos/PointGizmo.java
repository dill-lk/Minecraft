/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gizmos;

import net.mayaan.gizmos.Gizmo;
import net.mayaan.gizmos.GizmoPrimitives;
import net.mayaan.util.ARGB;
import net.mayaan.world.phys.Vec3;

public record PointGizmo(Vec3 pos, int color, float size) implements Gizmo
{
    @Override
    public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
        primitives.addPoint(this.pos, ARGB.multiplyAlpha(this.color, alphaMultiplier), this.size);
    }
}

