/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.object.projectile;

import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.PartPose;
import net.mayaan.client.model.geom.builders.CubeListBuilder;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.geom.builders.MeshDefinition;
import net.mayaan.client.model.geom.builders.PartDefinition;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Unit;

public class TridentModel
extends Model<Unit> {
    public static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/trident/trident.png");

    public TridentModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition pole = root.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(0, 6).addBox(-0.5f, 2.0f, -0.5f, 1.0f, 25.0f, 1.0f), PartPose.ZERO);
        pole.addOrReplaceChild("base", CubeListBuilder.create().texOffs(4, 0).addBox(-1.5f, 0.0f, -0.5f, 3.0f, 2.0f, 1.0f), PartPose.ZERO);
        pole.addOrReplaceChild("left_spike", CubeListBuilder.create().texOffs(4, 3).addBox(-2.5f, -3.0f, -0.5f, 1.0f, 4.0f, 1.0f), PartPose.ZERO);
        pole.addOrReplaceChild("middle_spike", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5f, -4.0f, -0.5f, 1.0f, 4.0f, 1.0f), PartPose.ZERO);
        pole.addOrReplaceChild("right_spike", CubeListBuilder.create().texOffs(4, 3).mirror().addBox(1.5f, -3.0f, -0.5f, 1.0f, 4.0f, 1.0f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 32);
    }
}

