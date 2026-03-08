/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.monster.piglin;

import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.monster.piglin.AdultPiglinModel;
import net.mayaan.client.model.monster.piglin.ZombifiedPiglinModel;

public class AdultZombifiedPiglinModel
extends ZombifiedPiglinModel {
    public AdultZombifiedPiglinModel(ModelPart root) {
        super(root);
    }

    @Override
    float getDefaultEarAngleInDegrees() {
        return 30.0f;
    }

    public static LayerDefinition createBodyLayer() {
        return AdultPiglinModel.createBodyLayer();
    }
}

