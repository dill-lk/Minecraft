/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.monster.piglin;

import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.monster.piglin.BabyPiglinModel;
import net.mayaan.client.model.monster.piglin.ZombifiedPiglinModel;

public class BabyZombifiedPiglinModel
extends ZombifiedPiglinModel {
    public BabyZombifiedPiglinModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        return BabyPiglinModel.createBodyLayer();
    }

    @Override
    float getDefaultEarAngleInDegrees() {
        return 5.0f;
    }
}

