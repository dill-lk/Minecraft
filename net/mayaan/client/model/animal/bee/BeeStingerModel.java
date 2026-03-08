/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.animal.bee;

import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.PartPose;
import net.mayaan.client.model.geom.builders.CubeListBuilder;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.geom.builders.MeshDefinition;
import net.mayaan.client.model.geom.builders.PartDefinition;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.util.Unit;

public class BeeStingerModel
extends Model<Unit> {
    public BeeStingerModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutCull);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder cross = CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, -0.5f, 0.0f, 2.0f, 1.0f, 0.0f);
        root.addOrReplaceChild("cross_1", cross, PartPose.rotation(0.7853982f, 0.0f, 0.0f));
        root.addOrReplaceChild("cross_2", cross, PartPose.rotation(2.3561945f, 0.0f, 0.0f));
        return LayerDefinition.create(mesh, 16, 16);
    }
}

