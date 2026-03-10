/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.object.skull;

import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.builders.CubeDeformation;
import net.mayaan.client.model.geom.builders.MeshDefinition;
import net.mayaan.client.model.monster.piglin.PiglinModel;
import net.mayaan.client.model.object.skull.SkullModelBase;

public class PiglinHeadModel
extends SkullModelBase {
    private final ModelPart head;
    private final ModelPart leftEar;
    private final ModelPart rightEar;

    public PiglinHeadModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.leftEar = this.head.getChild("left_ear");
        this.rightEar = this.head.getChild("right_ear");
    }

    public static MeshDefinition createHeadModel() {
        MeshDefinition mesh = new MeshDefinition();
        PiglinModel.addHead(CubeDeformation.NONE, mesh);
        return mesh;
    }

    @Override
    public void setupAnim(SkullModelBase.State state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * ((float)Math.PI / 180);
        this.head.xRot = state.xRot * ((float)Math.PI / 180);
        float asymmetry = 1.2f;
        this.leftEar.zRot = (float)(-(Math.cos(state.animationPos * (float)Math.PI * 0.2f * 1.2f) + 2.5)) * 0.2f;
        this.rightEar.zRot = (float)(Math.cos(state.animationPos * (float)Math.PI * 0.2f) + 2.5) * 0.2f;
    }
}

