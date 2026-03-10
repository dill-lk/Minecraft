/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.animal.feline;

import net.mayaan.client.model.animal.feline.AdultFelineModel;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.builders.CubeDeformation;
import net.mayaan.client.model.geom.builders.MeshTransformer;
import net.mayaan.client.renderer.entity.state.CatRenderState;

public class AdultCatModel
extends AdultFelineModel<CatRenderState> {
    public static final MeshTransformer CAT_TRANSFORMER = MeshTransformer.scaling(0.8f);
    public static final CubeDeformation COLLAR_DEFORMATION = new CubeDeformation(0.01f);

    public AdultCatModel(ModelPart root) {
        super(root);
    }
}

