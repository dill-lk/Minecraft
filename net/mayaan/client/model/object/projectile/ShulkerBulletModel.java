/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.object.projectile;

import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.PartPose;
import net.mayaan.client.model.geom.builders.CubeListBuilder;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.geom.builders.MeshDefinition;
import net.mayaan.client.model.geom.builders.PartDefinition;
import net.mayaan.client.renderer.entity.state.ShulkerBulletRenderState;

public class ShulkerBulletModel
extends EntityModel<ShulkerBulletRenderState> {
    private static final String MAIN = "main";
    private final ModelPart main;

    public ShulkerBulletModel(ModelPart root) {
        super(root);
        this.main = root.getChild(MAIN);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(MAIN, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -1.0f, 8.0f, 8.0f, 2.0f).texOffs(0, 10).addBox(-1.0f, -4.0f, -4.0f, 2.0f, 8.0f, 8.0f).texOffs(20, 0).addBox(-4.0f, -1.0f, -4.0f, 8.0f, 2.0f, 8.0f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(ShulkerBulletRenderState state) {
        super.setupAnim(state);
        this.main.yRot = state.yRot * ((float)Math.PI / 180);
        this.main.xRot = state.xRot * ((float)Math.PI / 180);
    }
}

