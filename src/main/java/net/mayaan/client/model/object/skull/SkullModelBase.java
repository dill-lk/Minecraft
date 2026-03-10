/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.object.skull;

import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.rendertype.RenderTypes;

public abstract class SkullModelBase
extends Model<State> {
    public SkullModelBase(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
    }

    public static class State {
        public float animationPos;
        public float yRot;
        public float xRot;
    }
}

