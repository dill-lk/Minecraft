/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.object.statue;

import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.util.Unit;

public class CopperGolemStatueModel
extends Model<Unit> {
    public CopperGolemStatueModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
    }

    @Override
    public void setupAnim(Unit ignored) {
        this.root.y = 0.0f;
        this.root.zRot = (float)Math.PI;
    }
}

