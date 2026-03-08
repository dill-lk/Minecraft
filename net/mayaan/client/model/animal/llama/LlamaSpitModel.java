/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.animal.llama;

import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.PartPose;
import net.mayaan.client.model.geom.builders.CubeListBuilder;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.geom.builders.MeshDefinition;
import net.mayaan.client.model.geom.builders.PartDefinition;
import net.mayaan.client.renderer.entity.state.EntityRenderState;

public class LlamaSpitModel
extends EntityModel<EntityRenderState> {
    private static final String MAIN = "main";

    public LlamaSpitModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        int edge = 2;
        root.addOrReplaceChild(MAIN, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f).addBox(0.0f, -4.0f, 0.0f, 2.0f, 2.0f, 2.0f).addBox(0.0f, 0.0f, -4.0f, 2.0f, 2.0f, 2.0f).addBox(0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f).addBox(2.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f).addBox(0.0f, 2.0f, 0.0f, 2.0f, 2.0f, 2.0f).addBox(0.0f, 0.0f, 2.0f, 2.0f, 2.0f, 2.0f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }
}

