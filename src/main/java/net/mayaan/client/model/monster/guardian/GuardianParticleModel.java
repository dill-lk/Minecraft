/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.monster.guardian;

import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.util.Unit;

public class GuardianParticleModel
extends Model<Unit> {
    public GuardianParticleModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
    }
}

