/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.monster.piglin;

import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.PartPose;
import net.mayaan.client.model.geom.builders.CubeDeformation;
import net.mayaan.client.model.geom.builders.CubeListBuilder;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.geom.builders.MeshDefinition;
import net.mayaan.client.model.geom.builders.PartDefinition;
import net.mayaan.client.model.monster.piglin.PiglinModel;
import net.mayaan.client.model.player.PlayerModel;

public class AdultPiglinModel
extends PiglinModel {
    public AdultPiglinModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f), PartPose.ZERO);
        PartDefinition head = AdultPiglinModel.addHead(CubeDeformation.NONE, mesh);
        head.clearChild("hat");
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    float getDefaultEarAngleInDegrees() {
        return 30.0f;
    }
}

