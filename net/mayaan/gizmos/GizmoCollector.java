/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gizmos;

import net.mayaan.gizmos.Gizmo;
import net.mayaan.gizmos.GizmoProperties;

public interface GizmoCollector {
    public static final GizmoProperties IGNORED = new GizmoProperties(){

        @Override
        public GizmoProperties setAlwaysOnTop() {
            return this;
        }

        @Override
        public GizmoProperties persistForMillis(int milliseconds) {
            return this;
        }

        @Override
        public GizmoProperties fadeOut() {
            return this;
        }
    };
    public static final GizmoCollector NOOP = gizmo -> IGNORED;

    public GizmoProperties add(Gizmo var1);
}

