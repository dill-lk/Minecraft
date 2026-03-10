/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.animal.feline;

import net.mayaan.client.model.animal.feline.BabyFelineModel;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.builders.MeshTransformer;
import net.mayaan.client.renderer.entity.state.CatRenderState;

public class BabyCatModel
extends BabyFelineModel<CatRenderState> {
    public static final MeshTransformer COLLAR_TRANSFORMER = MeshTransformer.scaling(1.01f);

    public BabyCatModel(ModelPart root) {
        super(root);
    }
}

