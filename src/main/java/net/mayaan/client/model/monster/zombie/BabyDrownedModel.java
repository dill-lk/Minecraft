/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.monster.zombie;

import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.builders.CubeDeformation;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.monster.zombie.BabyZombieModel;
import net.mayaan.client.model.monster.zombie.DrownedModel;

public class BabyDrownedModel
extends DrownedModel {
    public BabyDrownedModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation g) {
        return BabyZombieModel.createBodyLayer(g);
    }
}

